"use client";

import { Search } from "lucide-react";

import { Label } from "@/components/ui/label";
import {
    SidebarGroup,
    SidebarGroupContent,
    SidebarInput,
    SidebarMenu,
    SidebarMenuButton,
    SidebarMenuItem,
} from "@/components/ui/sidebar";
import { useContext } from "react";
import { SidebarSortableContext } from "@/providers/sidebar-sortable-provider";
import { flattenTree } from "@/lib/utils";
import { useNodeStore } from "@/hooks/use-node-store";
import { FlattenedItem, NodeType } from "@/types/node";

export function SearchForm({ ...props }: React.ComponentProps<"form">) {
    const nodeStored = useNodeStore((state) => state.data);
    const { setFilter } = useContext(SidebarSortableContext);

    const handleFilter = (data: NodeType[], query: string): NodeType[] => {
        return data.filter((node) => {
            if (node.children && node.children.length > 0) {
                return handleFilter(node.children, query);
            }
            return node.name.toLowerCase().includes(query);
        });
    };

    const handleSearch = (e: React.ChangeEvent<HTMLInputElement>) => {
        e.preventDefault();
        const query = e.target.value.toLowerCase();
        const filteredNodes = handleFilter(nodeStored, query);
        console.log(filteredNodes);
        setFilter(filteredNodes);
    };

    return (
        <form {...props}>
            <SidebarGroup className="py-0">
                <SidebarGroupContent className="relative">
                    <SidebarMenu>
                        <SidebarMenuItem>
                            <Label htmlFor="search" className="sr-only">
                                Search
                            </Label>
                            <SidebarInput
                                id="search"
                                placeholder="Search the docs..."
                                className="pl-8 bg-transparent border-0 focus-visible:ring-0 focus-visible:ring-offset-0 h-10 hover:bg-sidebar-accent rounded-md"
                                onChange={handleSearch}
                            />
                            <Search className="pointer-events-none absolute top-1/2 left-2 size-4 -translate-y-1/2 opacity-50 select-none" />
                        </SidebarMenuItem>
                        <SidebarMenuItem>
                            <SidebarMenuButton asChild className="h-10">
                                <a href="#">
                                    <span>All my activities</span>
                                </a>
                            </SidebarMenuButton>
                        </SidebarMenuItem>
                    </SidebarMenu>
                </SidebarGroupContent>
            </SidebarGroup>
        </form>
    );
}
