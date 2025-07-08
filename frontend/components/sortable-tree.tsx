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
import { useNodeModal } from "@/hooks/use-node-modal";
import { useState } from "react";

interface SortableTreeProps {
    flat: FlattenedItem[];
    items: UniqueIdentifier[];
    onCollapse: (id: string) => void;
    isDragging?: boolean;
}

export const SortableTree: React.FC<SortableTreeProps> = ({
    flat,
    items,
    onCollapse,
    isDragging,
}) => {
    const projectMenu: group[] = [
        {
            items: [
                {
                    label: "Create project",
                    icon: SquarePlus,
                    onClick: () => handleCreateProject(),
                    kind: "Default",
                },
                {
                    label: "Create folder",
                    icon: Folder,
                    onClick: () => handleCreateFolder(),
                    kind: "Default",
                },
            ],
        },
    ];

    const [openDropdown, setOpenDropdown] = useState(false);
    const setOpenModal = useNodeModal((state) => state.setOpen);
    const setModalType = useNodeModal((state) => state.setModalType);
    const setKind = useNodeModal((state) => state.setKind);

    function handleCreateProject() {
        setKind("Project");
        setModalType("create");
        setOpenModal(true);
    }

    function handleCreateFolder() {
        setKind("Folder");
        setModalType("create");
        setOpenModal(true);
    }

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
                                isDragging={isDragging}
                            />
                        ))}
                    </SortableContext>
                </SidebarMenu>
            </SidebarGroupContent>
        </SidebarGroup>
    );
};
