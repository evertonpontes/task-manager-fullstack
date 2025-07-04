"use client";

import { FlattenedItem, NodeType } from "@/types/node";
import { arrayMove } from "@dnd-kit/sortable";

export function getSubtree(
    items: FlattenedItem[],
    rootId: string
): FlattenedItem[] {
    const result: FlattenedItem[] = [];
    const walk = (id: string) => {
        const node = items.find((i) => i.id === id);
        if (!node) return;
        result.push(node);
        items
            .filter((i) => i.parentNodeId === id)
            .forEach((child) => walk(child.id));
    };
    walk(rootId);
    return result;
}

function getDragDepth(offset: number, indentationWidth: number) {
    return Math.round(offset / indentationWidth);
}

export function getProjection(
    items: FlattenedItem[],
    activeId: string,
    overId: string,
    dragOffset: number,
    indentationWidth: number
) {
    const overItemIndex = items.findIndex((item) => item.id === overId);
    const activeItemIndex = items.findIndex((item) => item.id === activeId);
    const activeItem = items[activeItemIndex];
    const newItems = arrayMove(items, activeItemIndex, overItemIndex);
    const previousItem = newItems[overItemIndex - 1];
    const nextItem = newItems[overItemIndex + 1];
    const dragDepth = getDragDepth(dragOffset, indentationWidth);
    const projectedDepth = activeItem?.depth + dragDepth;
    const maxDepth = getMaxDepth({ activeItem, previousItem });
    const minDepth = getMinDepth({ activeItem, previousItem, nextItem });
    let depth = projectedDepth;

    if (projectedDepth >= maxDepth) {
        depth = maxDepth;
    } else if (projectedDepth < minDepth) {
        depth = minDepth;
    }

    newItems[overItemIndex].depth = depth;
    newItems[overItemIndex].parentNodeId = getParentId();

    return {
        depth,
        maxDepth,
        minDepth,
        parentId: getParentId(),
        rank: getNewRank(getParentId()),
    };

    function getParentId() {
        if (depth === 0 || !previousItem) {
            return null;
        }

        if (depth === previousItem.depth) {
            return previousItem.parentNodeId;
        }

        if (depth > previousItem.depth) {
            return previousItem.id;
        }

        const newParent = newItems
            .slice(0, overItemIndex)
            .reverse()
            .find((item) => item.depth === depth)?.parentNodeId;

        return newParent ?? null;
    }

    /**
     * Return the new rank number of item based on previous and next item of the list
     * @param parentNodeId the filter value, if null will return the root items
     */
    function getNewRank(parentNodeId: string | null) {
        const filteredList = newItems.filter(
            (item) => item.parentNodeId === parentNodeId
        );
        const activeItemIndex = filteredList.findIndex(
            (item) => item.id === activeId
        );
        const previousItem = filteredList[activeItemIndex - 1];
        const nextItem = filteredList[activeItemIndex + 1];

        if (previousItem && nextItem) {
            return Math.round((previousItem.rank + nextItem.rank) / 2);
        } else if (previousItem) {
            return previousItem.rank + 10000;
        } else if (nextItem) {
            return nextItem.rank - 10000;
        } else {
            return 10000;
        }
    }
}

function getMaxDepth({
    activeItem,
    previousItem,
}: {
    activeItem: FlattenedItem;
    previousItem: FlattenedItem;
}) {
    if (activeItem?.kind === "Project") {
        if (previousItem) {
            if (previousItem.kind === "Folder") {
                return previousItem.depth + 1;
            } else {
                return previousItem.depth;
            }
        }
        return 0;
    }

    return 0;
}

function getMinDepth({
    activeItem,
    previousItem,
    nextItem,
}: {
    activeItem: FlattenedItem;
    previousItem: FlattenedItem;
    nextItem: FlattenedItem;
}) {
    if (activeItem) {
        if (previousItem && previousItem.kind === "Folder") {
            return previousItem.depth + 1;
        }
        if (previousItem && previousItem.parentNodeId) {
            return previousItem.depth;
        }
        if (nextItem && nextItem.parentNodeId) {
            return nextItem.depth + 1;
        }
        return 0;
    }
    return 0;
}

export function removeChildrenOf(items: FlattenedItem[], ids: string[]) {
    const excludeParentIds = [...ids];

    return items.filter((item) => {
        if (item.parentNodeId && excludeParentIds.includes(item.parentNodeId)) {
            if (item.children.length) {
                excludeParentIds.push(item.id);
            }
            return false;
        }
        return true;
    });
}

export function setProperty<T extends keyof NodeType>(
    items: NodeType[],
    id: string,
    property: T,
    setter: (value: NodeType[T]) => NodeType[T]
) {
    for (const item of items) {
        if (item.id === id) {
            item[property] = setter(item[property]);
            continue;
        }

        if (item.children.length) {
            item.children = setProperty(item.children, id, property, setter);
        }
    }

    return [...items];
}
