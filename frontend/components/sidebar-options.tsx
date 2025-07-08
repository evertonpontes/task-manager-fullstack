import React from "react";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "./ui/dropdown-menu";
import { DropdownMenuGroup } from "@radix-ui/react-dropdown-menu";
import { LucideIcon } from "lucide-react";
import { cn } from "@/lib/utils";

export type group = {
    items: {
        label: string;
        icon: LucideIcon;
        onClick: () => void;
        kind: "Default" | "Destructive";
    }[];
};

interface SidebarDropdownProps {
    children: React.ReactNode;
    groups: group[];
    disabled?: boolean;
    open?: boolean;
    onOpenChange?: (open: boolean) => void;
}

export const SidebarDropdown: React.FC<SidebarDropdownProps> = ({
    groups,
    disabled,
    children,
    open,
    onOpenChange,
}) => {
    if (disabled) <>{children}</>;

    return (
        <DropdownMenu modal={false} open={open} onOpenChange={onOpenChange}>
            <DropdownMenuTrigger asChild>
                {children ? (
                    children
                ) : (
                    <button className="sr-only">Action</button>
                )}
            </DropdownMenuTrigger>
            <DropdownMenuContent className="w-56 py-1.5 px-2.5" align="start">
                {groups.map((group, index) => (
                    <div key={index}>
                        <DropdownMenuGroup className="gap-0.5">
                            {group.items.map((item, index) => (
                                <DropdownMenuItem
                                    key={index}
                                    onClick={item.onClick}
                                    className="h-10 flex items-center gap-2 py-1.5 px-2.5 cursor-pointer"
                                >
                                    <item.icon
                                        className={cn(
                                            "stroke-1 size-5 shrink-0",
                                            item.kind === "Destructive"
                                                ? "stroke-destructive"
                                                : "stroke-muted-foreground"
                                        )}
                                    />
                                    <span className="ml-2">{item.label}</span>
                                </DropdownMenuItem>
                            ))}
                        </DropdownMenuGroup>
                        {index < groups.length - 1 && <DropdownMenuSeparator />}
                    </div>
                ))}
            </DropdownMenuContent>
        </DropdownMenu>
    );
};
