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

const sampleNodes: NodeType[] = [
    {
        id: "1",
        rank: 10000,
        name: "E-commerce App",
        kind: "Project",
        parentNodeId: null,
        userId: "1",
        children: [],
        taskTypes: [],
        taskStatuses: [],
        createdAt: new Date(),
        updatedAt: new Date(),
    },
    {
        id: "2",
        rank: 20000,
        name: "Portfolio",
        kind: "Project",
        parentNodeId: null,
        userId: "1",
        children: [],
        taskTypes: [],
        taskStatuses: [],
        createdAt: new Date(),
        updatedAt: new Date(),
    },
    {
        id: "3",
        rank: 30000,
        name: "Technical Documentation",
        kind: "Project",
        parentNodeId: null,
        userId: "1",
        children: [],
        taskTypes: [],
        taskStatuses: [],
        createdAt: new Date(),
        updatedAt: new Date(),
    },
    {
        id: "4",
        rank: 40000,
        name: "Design System",
        kind: "Folder",
        parentNodeId: null,
        userId: "1",
        children: [
            {
                id: "5",
                rank: 10000,
                name: "Typography",
                kind: "Project",
                parentNodeId: "4",
                userId: "1",
                children: [],
                taskTypes: [],
                taskStatuses: [],
                createdAt: new Date(),
                updatedAt: new Date(),
            },
            {
                id: "6",
                rank: 20000,
                name: "Colors",
                kind: "Project",
                parentNodeId: "4",
                userId: "1",
                children: [],
                taskTypes: [],
                taskStatuses: [],
                createdAt: new Date(),
                updatedAt: new Date(),
            },
            {
                id: "7",
                rank: 30000,
                name: "Buttons",
                kind: "Project",
                parentNodeId: "4",
                userId: "1",
                children: [],
                taskTypes: [],
                taskStatuses: [],
                createdAt: new Date(),
                updatedAt: new Date(),
            },
        ],
        taskTypes: [],
        taskStatuses: [],
        createdAt: new Date(),
        updatedAt: new Date(),
    },
    {
        id: "8",
        rank: 50000,
        name: "New Folder",
        kind: "Folder",
        parentNodeId: null,
        userId: "1",
        children: [],
        taskTypes: [],
        taskStatuses: [],
        createdAt: new Date(),
        updatedAt: new Date(),
    },
];
export const AppSidebar = () => {
    const [isLoaded, setIsLoaded] = useState(false);
    const [nodes, setNodes] = useState(orderByRank(sampleNodes));
    const [activeId, setActiveId] = useState<string | null>(null);
    const [overId, setOverId] = useState<string | null>(null);
    const [offsetLeft, setOffsetLeft] = useState(0);
    const [isDragging, setIsDragging] = useState(false);

    useEffect(() => {
        if (typeof window !== "undefined") {
            setIsLoaded(true);
        }
    }, []);

    const flattenedItems = useMemo(() => {
        const flattenedTree = flattenTree(nodes);
        const collapsedItems = flattenedTree.reduce<string[]>(
            (acc, { children, collapsed, id }) =>
                collapsed && children.length ? [...acc, id] : acc,
            []
        );

        return removeChildrenOf(
            flattenedTree,
            activeId && isDragging
                ? [activeId, ...collapsedItems]
                : collapsedItems
        );
    }, [activeId, isDragging, nodes]);

    const projected =
        activeId && overId
            ? getProjection(flattenedItems, activeId, overId, offsetLeft, 20)
            : null;

    const sensors = useSensors(
        useSensor(PointerSensor, {
            activationConstraint: {
                delay: 100,
                tolerance: 10,
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
        setActiveId(activeId.toString());
        setOverId(activeId.toString());
        document.body.style.setProperty("cursor", "grabbing");
    };

    const handleDragMove = ({ delta }: DragMoveEvent) => {
        setIsDragging(true);
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
                JSON.stringify(flattenTree(nodes))
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

            const rebuiltTree = buildTree(newItems);
            setNodes(rebuiltTree);
        }
    }

    function resetState() {
        setActiveId(null);
        setOverId(null);
        setOffsetLeft(0);
        setIsDragging(false);

        document.body.style.setProperty("cursor", "");
    }

    function handleCollapse(id: string) {
        setNodes((items) => {
            const cloned: NodeType[] = JSON.parse(JSON.stringify(items));
            return setProperty(cloned, id, "collapsed", (value) => {
                return !value;
            });
        });
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
