"use client";

import React, { useCallback, useMemo } from "react";
import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import { SidebarMenuButton, SidebarMenuItem } from "./ui/sidebar";
import { FlattenedItem, NodeType } from "@/types/node";
import {
    ChevronDown,
    Copy,
    EllipsisVertical,
    Folder,
    PencilLine,
    Settings,
    SquarePlus,
    Trash2,
} from "lucide-react";
import { cn } from "@/lib/utils";
import Link from "next/link";
import { group, SidebarDropdown } from "./sidebar-options";
import { useAuth } from "@/hooks/use-auth";
import { useNodeModal } from "@/hooks/use-node-modal";
import { Button } from "./ui/button";
import { useRouter } from "next/navigation";
import { useNodeStore } from "@/hooks/use-node-store";
import axios from "axios";

interface SortableItemProps {
    item: FlattenedItem;
    onCollapse: (id: string) => void;
    isDragging?: boolean;
}

export const SortableItem: React.FC<SortableItemProps> = ({
    item,
    onCollapse,
    isDragging,
}) => {
    const { attributes, listeners, setNodeRef, transform, transition } =
        useSortable({
            id: item.id,
        });

    const style = {
        transform: CSS.Transform.toString(transform),
        transition,
        marginLeft: `${item.depth * 24}px`,
    };

    return (
        <SidebarMenuItem
            ref={setNodeRef}
            data-dragging={isDragging}
            className="relative z-0 data-[dragging=true]:z-10 data-[dragging=true]:opacity-80 mb-1"
            style={style}
            {...attributes}
            {...listeners}
        >
            <SidebarMenuButton asChild className="h-10 flex items-center">
                <div>
                    <SortableItemDetails
                        item={item}
                        onCollapse={onCollapse}
                        isDragging={isDragging}
                    />
                </div>
            </SidebarMenuButton>
        </SidebarMenuItem>
    );
};

interface SortableItemDetailsProps {
    item: FlattenedItem;
    onCollapse: (id: string) => void;
    isDragging?: boolean;
}

const SortableItemDetailsComponent: React.FC<SortableItemDetailsProps> = ({
    item,
    onCollapse,
    isDragging,
}) => {
    const token = useAuth((state) => state.token);
    const [isHovered, setIsHovered] = React.useState(false);
    const [openDropdown, setOpenDropdown] = React.useState(false);
    const setOpenModal = useNodeModal((state) => state.setOpen);
    const setModalType = useNodeModal((state) => state.setModalType);
    const setKind = useNodeModal((state) => state.setKind);
    const setId = useNodeModal((state) => state.setId);
    const setParentNodeId = useNodeModal((state) => state.setParentNodeId);
    const addNode = useNodeStore((state) => state.addData);

    const handleOpenDropdown = (open: boolean) => setOpenDropdown(open);

    const handleDeleteNode = async (kind: "Folder" | "Project") => {
        setModalType("delete");
        setKind(kind);
        setId(item.id);
        setOpenModal(true);
    };

    const handleUpdateNode = async (kind: "Folder" | "Project") => {
        setModalType("rename");
        setKind(kind);
        setId(item.id);
        setOpenModal(true);
    };

    const handleCreateNode = async (
        kind: "Folder" | "Project",
        parentNodeId: string | null
    ) => {
        setModalType("create");
        setKind(kind);
        setOpenModal(true);
        setParentNodeId(parentNodeId);
    };

    const handleDuplicateProject = async (id: string) => {
        const { data } = await axios.post<NodeType>(`/api/nodes/${id}`, {
            withCredentials: true,
        });
        addNode(data);
    };

    const projectMenu: group[] = [
        {
            items: [
                {
                    label: "Rename project",
                    icon: PencilLine,
                    onClick: () => handleUpdateNode(item.kind),
                    kind: "Default",
                },
                {
                    label: "Duplicate project",
                    icon: Copy,
                    onClick: () => handleDuplicateProject(item.id),
                    kind: "Default",
                },
                {
                    label: "Project Settings",
                    icon: Settings,
                    onClick: () => {},
                    kind: "Default",
                },
            ],
        },
        {
            items: [
                {
                    label: "Delete project",
                    icon: Trash2,
                    onClick: () => handleDeleteNode(item.kind),
                    kind: "Destructive",
                },
            ],
        },
    ];

    const folderMenu: group[] = [
        {
            items: [
                {
                    label: "Create project",
                    icon: SquarePlus,
                    onClick: () => handleCreateNode("Project", item.id),
                    kind: "Default",
                },
                {
                    label: "Rename folder",
                    icon: PencilLine,
                    onClick: () => handleUpdateNode(item.kind),
                    kind: "Default",
                },
            ],
        },
        {
            items: [
                {
                    label: "Delete folder",
                    icon: Trash2,
                    onClick: () => handleDeleteNode(item.kind),
                    kind: "Destructive",
                },
            ],
        },
    ];

    if (!token) return null;

    return item.kind === "Folder" ? (
        <div
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
            onContextMenu={(e) => {
                e.preventDefault();
                handleOpenDropdown(true);
            }}
            className="w-full flex items-center gap-1.5 overflow-hidden select-none"
        >
            <button
                className="cursor-pointer transition-colors text-muted-foreground hover:text-muted"
                onClick={() => onCollapse(item.id)}
            >
                <ChevronDown
                    className={cn(
                        "rotate-0 size-4 shrink-0 transition-transform",
                        item.collapsed && "rotate-180"
                    )}
                />
            </button>
            <Folder className="size-4 shrink-0 fill-muted" />
            <span className="flex-1 truncate text-sm">{item.name}</span>
            <div className="flex items-center">
                <SidebarDropdown
                    groups={folderMenu}
                    disabled={isDragging}
                    open={openDropdown}
                    onOpenChange={handleOpenDropdown}
                >
                    <button
                        className={cn(
                            "opacity-0 transition-opacity cursor-pointer",
                            isHovered && "opacity-100"
                        )}
                    >
                        <EllipsisVertical className="size-4 stroke-muted-foreground hover:stroke-muted transition-colors" />
                    </button>
                </SidebarDropdown>
            </div>
        </div>
    ) : (
        <div
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
            onContextMenu={(e) => {
                e.preventDefault();
                handleOpenDropdown(true);
            }}
            className={cn(
                "w-full flex items-center gap-1.5 overflow-hidden select-none pointer-events-auto",
                isDragging && "pointer-events-none"
            )}
        >
            <Link
                href={`/workspace/${item.id}`}
                className="flex-1 truncate text-sm"
            >
                {item.name}
            </Link>
            <div className="flex items-center">
                <SidebarDropdown
                    groups={projectMenu}
                    disabled={isDragging}
                    open={openDropdown}
                    onOpenChange={handleOpenDropdown}
                >
                    <button
                        className={cn(
                            "opacity-0 transition-opacity cursor-pointer",
                            isHovered && "opacity-100"
                        )}
                    >
                        <EllipsisVertical className="size-4 stroke-muted-foreground hover:stroke-muted transition-colors" />
                    </button>
                </SidebarDropdown>
            </div>
        </div>
    );
};

const SortableItemDetails = React.memo(
    SortableItemDetailsComponent,
    (prev, next) => {
        return (
            prev.item.id === next.item.id &&
            prev.isDragging === next.isDragging &&
            prev.onCollapse === next.onCollapse
        );
    }
);
