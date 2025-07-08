"use client";

import {
    DndContext,
    closestCenter,
    KeyboardSensor,
    PointerSensor,
    useSensor,
    useSensors,
    DragEndEvent,
    UniqueIdentifier,
    DragStartEvent,
    DragOverEvent,
    DragMoveEvent,
} from "@dnd-kit/core";
import { restrictToVerticalAxis } from "@dnd-kit/modifiers";
import { sortableKeyboardCoordinates } from "@dnd-kit/sortable";

import {
    Sidebar,
    SidebarContent,
    SidebarHeader,
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem,
    SidebarSeparator,
} from "@/components/ui/sidebar";
import { FlattenedItem, NodeType } from "@/types/node";
import { GalleryVerticalEnd } from "lucide-react";
import { SortableTree } from "./sortable-tree";
import { useEffect, useMemo, useState } from "react";
import { flattenTree } from "@/lib/utils";
import {
    getProjection,
    getSubtree,
    removeChildrenOf,
    setProperty,
} from "@/lib/utilities";
import axios from "axios";
import { useAuth } from "@/hooks/use-auth";
import { useNodeStore } from "@/hooks/use-node-store";
import { toast } from "react-toastify";

export const AppSidebar = () => {
    const [isLoaded, setIsLoaded] = useState(false);
    const [activeId, setActiveId] = useState<string | null>(null);
    const [overId, setOverId] = useState<string | null>(null);
    const [offsetLeft, setOffsetLeft] = useState(0);
    const [isDragging, setIsDragging] = useState(false);
    const nodeStored = useNodeStore((state) => state.data);
    const setNodeData = useNodeStore((state) => state.setData);
    const token = useAuth((state) => state.token);

    useEffect(() => {
        async function fetchNodes() {
            try {
                const response = await axios.get<NodeType[]>("/api/nodes", {
                    withCredentials: true,
                });
                const data = response.data;
                const nodes = data.map((node) => ({
                    ...node,
                    collapsed: false,
                }));
                setNodeData(orderByRank(nodes));
            } catch (error) {
                console.error("Error fetching nodes:", error);
            } finally {
                setIsLoaded(true);
            }
        }

        if (!token) return;
        fetchNodes();
    }, [setNodeData, token]);

    const flattenedItems = useMemo(() => {
        const flattenedTree: FlattenedItem[] = flattenTree(nodeStored);
        const collapsedItems = flattenedTree.reduce<string[]>(
            (acc, { children, collapsed, id }) =>
                collapsed && children?.length ? [...acc, id] : acc,
            []
        );

        return removeChildrenOf(
            flattenedTree,
            activeId && isDragging
                ? [activeId, ...collapsedItems]
                : collapsedItems
        );
    }, [activeId, isDragging, nodeStored]);

    const projected =
        activeId && overId
            ? getProjection(flattenedItems, activeId, overId, offsetLeft, 20)
            : null;

    const sensors = useSensors(
        useSensor(PointerSensor, {
            activationConstraint: {
                distance: 5,
            },
        }),
        useSensor(KeyboardSensor, {
            coordinateGetter: sortableKeyboardCoordinates,
        })
    );

    const dataIds = useMemo<UniqueIdentifier[]>(
        () => flattenedItems.map((item) => item.id) || [],
        [flattenedItems]
    );

    const handleDragStart = ({ active: { id: activeId } }: DragStartEvent) => {
        setIsDragging(true);
        setActiveId(activeId.toString());
        setOverId(activeId.toString());
    };

    const handleDragMove = ({ delta }: DragMoveEvent) => {
        setOffsetLeft(delta.x);
    };

    const handleDragOver = ({ over }: DragOverEvent) => {
        setOverId(over?.id.toString() ?? null);
    };

    function handleDragEnd(event: DragEndEvent) {
        resetState();
        const { active, over } = event;

        if (over && projected) {
            const { depth, parentId, rank } = projected;
            const clonedItems: FlattenedItem[] = JSON.parse(
                JSON.stringify(flattenTree(nodeStored))
            );
            const overIndex = clonedItems.findIndex(
                (item) => item.id === over.id
            );

            const subtree = getSubtree(clonedItems, active.id.toString());
            const subtreeIds = subtree.map((item) => item.id);

            const itemsWithoutSubtree = clonedItems.filter(
                (item) => !subtreeIds.includes(item.id)
            );

            const newItems = [
                ...itemsWithoutSubtree.slice(0, overIndex),
                ...subtree.map((item) => {
                    const newDepth =
                        item.id === active.id
                            ? depth
                            : item.depth + (depth - item.depth);
                    const newParent =
                        item.id === active.id ? parentId : item.parentNodeId;
                    const newRank = item.id === active.id ? rank : item.rank;
                    return {
                        ...item,
                        depth: newDepth,
                        parentNodeId: newParent,
                        rank: newRank,
                    };
                }),
                ...itemsWithoutSubtree.slice(overIndex),
            ];

            const updatedItem = newItems[overIndex];

            onSubmitSortable(updatedItem);

            const rebuiltTree = buildTree(newItems);
            setNodeData(rebuiltTree);
        }
    }

    function resetState() {
        setActiveId(null);
        setOverId(null);
        setOffsetLeft(0);
        setIsDragging(false);
    }

    function handleCollapse(id: string) {
        const cloned: NodeType[] = nodeStored;
        const updatedNodes = setProperty(cloned, id, "collapsed", (value) => {
            return !value;
        });

        setNodeData(updatedNodes);
    }

    async function onSubmitSortable(item: FlattenedItem) {
        const data = {
            name: item.name,
            rank: item.rank,
            parentNodeId: item.parentNodeId,
        };

        try {
            await axios.put<NodeType>(`/api/nodes/${item.id}`, data, {
                withCredentials: true,
            });
        } catch (error) {
            console.error("Error fetching nodes:", error);
            toast.error(`${item.kind} update failed.`, {
                position: "bottom-left",
            });
        }
    }

    if (!isLoaded) return null;

    return (
        <Sidebar collapsible="offcanvas">
            <SidebarHeader>
                <SidebarMenu>
                    <SidebarMenuItem className="h-12 flex items-center justify-center">
                        <SidebarMenuButton asChild>
                            <a
                                href="/workspace"
                                className="text-base font-semibold"
                            >
                                <GalleryVerticalEnd className="size-6 shrink-0" />
                                <span>My Workspace</span>
                            </a>
                        </SidebarMenuButton>
                    </SidebarMenuItem>
                </SidebarMenu>
            </SidebarHeader>
            <SidebarSeparator className="mx-0" />
            <SidebarContent>
                <DndContext
                    sensors={sensors}
                    collisionDetection={closestCenter}
                    modifiers={[restrictToVerticalAxis]}
                    onDragStart={handleDragStart}
                    onDragMove={handleDragMove}
                    onDragOver={handleDragOver}
                    onDragEnd={handleDragEnd}
                >
                    <SortableTree
                        items={dataIds}
                        flat={flattenedItems}
                        onCollapse={handleCollapse}
                        isDragging={isDragging}
                    />
                </DndContext>
            </SidebarContent>
        </Sidebar>
    );
};

function buildTree(flat: FlattenedItem[]): NodeType[] {
    const map = new Map<string, NodeType>();
    const root: NodeType[] = [];

    flat.forEach((item) => {
        map.set(item.id, { ...item, children: [] });
    });

    flat.forEach((item) => {
        const node = map.get(item.id);

        if (!node) return;
        if (item.parentNodeId) {
            const parent = map.get(item.parentNodeId);
            parent?.children.push(node);
        } else {
            root.push(node);
        }
    });

    return root;
}

function orderByRank(nodes: NodeType[]) {
    return nodes.sort((a, b) => {
        if (a.kind === "Folder") a.children = orderByRank(a.children);
        if (b.kind === "Folder") b.children = orderByRank(b.children);
        return a.rank - b.rank;
    });
}
