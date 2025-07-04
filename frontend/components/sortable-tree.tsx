"use client";

import {
    SortableContext,
    verticalListSortingStrategy,
} from "@dnd-kit/sortable";

import { FlattenedItem } from "@/types/node";
import {
    SidebarGroup,
    SidebarGroupAction,
    SidebarGroupContent,
    SidebarGroupLabel,
    SidebarMenu,
} from "@/components/ui/sidebar";
import { Folder, Plus, SquarePlus } from "lucide-react";
import { SortableItem } from "./sortable-item";
import { UniqueIdentifier } from "@dnd-kit/core";
import { group, SidebarDropdown } from "./sidebar-options";
import { Button } from "./ui/button";

interface SortableTreeProps {
    flat: FlattenedItem[];
    items: UniqueIdentifier[];
    onCollapse: (id: string) => void;
}

export const SortableTree: React.FC<SortableTreeProps> = ({
    flat,
    items,
    onCollapse,
}) => {
    const projectMenu: group[] = [
        {
            items: [
                {
                    label: "Create project",
                    icon: SquarePlus,
                    onClick: () => {},
                    kind: "Default",
                },
                {
                    label: "Create folder",
                    icon: Folder,
                    onClick: () => {},
                    kind: "Default",
                },
            ],
        },
    ];

    return (
        <SidebarGroup>
            <SidebarGroupLabel className="text-sm">Projects</SidebarGroupLabel>
            <SidebarDropdown groups={projectMenu}>
                <SidebarGroupAction title="Create Project">
                    <Plus className="size-5 shrink-0" />
                </SidebarGroupAction>
            </SidebarDropdown>
            <SidebarGroupContent>
                <SidebarMenu>
                    <SortableContext
                        items={items}
                        strategy={verticalListSortingStrategy}
                    >
                        {flat.map((item) => (
                            <SortableItem
                                key={item.id}
                                item={item}
                                onCollapse={() => onCollapse(item.id)}
                            />
                        ))}
                    </SortableContext>
                </SidebarMenu>
            </SidebarGroupContent>
        </SidebarGroup>
    );
};
