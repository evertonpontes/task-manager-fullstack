"use client";

import React from "react";
import { useSortable } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import { SidebarMenuButton, SidebarMenuItem } from "./ui/sidebar";
import { FlattenedItem } from "@/types/node";
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
import { group, SidebarContext, SidebarDropdown } from "./sidebar-options";

interface SortableItemProps {
    item: FlattenedItem;
    onCollapse: (id: string) => void;
}

export const SortableItem: React.FC<SortableItemProps> = ({
    item,
    onCollapse,
}) => {
    const {
        attributes,
        listeners,
        setNodeRef,
        transform,
        transition,
        isDragging,
    } = useSortable({
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
        >
            <SidebarMenuButton asChild>
                <div>
                    <SortableItemDetails
                        item={item}
                        onCollapse={onCollapse}
                        isDragging={isDragging}
                        handleProps={{
                            ...attributes,
                            ...listeners,
                        }}
                    />
                </div>
            </SidebarMenuButton>
        </SidebarMenuItem>
    );
};

interface SortableItemDetailsProps {
    item: FlattenedItem;
    onCollapse: (id: string) => void;
    handleProps?: any;
    isDragging?: boolean;
}

const SortableItemDetails: React.FC<SortableItemDetailsProps> = ({
    item,
    onCollapse,
    handleProps,
    isDragging,
}) => {
    const [isHovered, setIsHovered] = React.useState(false);
    const myRef = React.useRef<HTMLDivElement>(null);
    React.useEffect(() => {
        if (myRef.current) {
            myRef.current.addEventListener("mouseenter", () => {
                setIsHovered(true);
            });
            myRef.current.addEventListener("mouseleave", () => {
                setIsHovered(false);
            });
        }
    }, []);

    const projectMenu: group[] = [
        {
            items: [
                {
                    label: "Rename project",
                    icon: PencilLine,
                    onClick: () => {},
                    kind: "Default",
                },
                {
                    label: "Duplicate project",
                    icon: Copy,
                    onClick: () => {},
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
                    onClick: () => {},
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
                    onClick: () => {},
                    kind: "Default",
                },
                {
                    label: "Rename folder",
                    icon: PencilLine,
                    onClick: () => {},
                    kind: "Default",
                },
            ],
        },
        {
            items: [
                {
                    label: "Delete folder",
                    icon: Trash2,
                    onClick: () => {},
                    kind: "Destructive",
                },
            ],
        },
    ];

    if (item.kind === "Folder") {
        return (
            <div
                ref={myRef}
                className="w-full flex items-center gap-1.5 overflow-hidden  select-none"
            >
                <button
                    onClick={() => onCollapse(item.id)}
                    className="cursor-pointer"
                >
                    <ChevronDown
                        className={cn(
                            "rotate-0 size-5 shrink-0 transition-all stroke-muted-foreground hover:stroke-blue-600 select-none",
                            item.collapsed && "rotate-180"
                        )}
                    />
                </button>
                <Folder className="size-5 fill-blue-600 stroke-0 shrink-0" />
                <SidebarContext groups={folderMenu}>
                    <span className={cn("w-full text-left")} {...handleProps}>
                        {item.name}
                    </span>
                </SidebarContext>
                <div className="ml-auto flex items-end justify-end">
                    <SidebarDropdown groups={folderMenu}>
                        <button
                            className={cn(
                                "opacity-0 transition-opacity cursor-pointer",
                                isHovered && "opacity-100"
                            )}
                        >
                            <EllipsisVertical className="size-4 stroke-muted-foreground hover:stroke-blue-600 transition-colors" />
                        </button>
                    </SidebarDropdown>
                </div>
            </div>
        );
    } else {
        return (
            <div
                ref={myRef}
                className="w-full flex items-center overflow-hidden select-none"
            >
                <SidebarContext groups={projectMenu}>
                    <Link
                        className={cn(
                            "w-full text-left",
                            !isDragging && "cursor-pointer"
                        )}
                        {...handleProps}
                        href={`/workspace/${item.id}`}
                    >
                        {item.name}
                    </Link>
                </SidebarContext>
                <div>
                    <SidebarDropdown groups={projectMenu}>
                        <button
                            className={cn(
                                "opacity-0 transition-opacity cursor-pointer",
                                isHovered && "opacity-100"
                            )}
                        >
                            <EllipsisVertical className="size-4 stroke-muted-foreground hover:stroke-blue-600 transition-colors" />
                        </button>
                    </SidebarDropdown>
                </div>
            </div>
        );
    }
};
