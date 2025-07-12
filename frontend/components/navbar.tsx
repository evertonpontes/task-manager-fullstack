"use client";
import React from "react";
import { SidebarTrigger } from "./ui/sidebar";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Button } from "@/components/ui/button";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { Moon, Power, Settings } from "lucide-react";
import { useAuth } from "@/hooks/use-auth";

interface NavbarProps {
    children?: React.ReactNode;
}

export const Navbar: React.FC<NavbarProps> = ({ children }) => {
    const { user } = useAuth();

    return (
        <header className="shadow-sm px-4 md:px-6 sticky top-0 z-50 bg-background">
            <div className="flex h-16 items-center justify-between gap-4">
                <div className="flex flex-1 items-center gap-2">
                    <SidebarTrigger />
                    {children}
                </div>
                <div className="flex items-center gap-2">
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <Button
                                variant="ghost"
                                className="h-auto p-0 hover:bg-transparent"
                            >
                                <Avatar>
                                    <AvatarImage
                                        src={user?.picture}
                                        alt="Profile image"
                                    />
                                    <AvatarFallback>
                                        {user?.firstName
                                            .charAt(0)
                                            .toUpperCase()}
                                    </AvatarFallback>
                                </Avatar>
                            </Button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent className="max-w-64">
                            <DropdownMenuLabel className="flex min-w-0 items-center gap-2">
                                <Avatar>
                                    <AvatarImage
                                        src={user?.picture}
                                        alt="Profile image"
                                    />
                                    <AvatarFallback>
                                        {user?.firstName
                                            .charAt(0)
                                            .toUpperCase()}
                                    </AvatarFallback>
                                </Avatar>
                                <div className="flex min-w-0 flex-col">
                                    <span className="text-foreground truncate text-sm font-medium">
                                        {user?.firstName} {user?.lastName}
                                    </span>
                                    <span className="text-muted-foreground truncate text-xs font-normal">
                                        {user?.email}
                                    </span>
                                </div>
                            </DropdownMenuLabel>
                            <DropdownMenuSeparator />
                            <DropdownMenuGroup>
                                <DropdownMenuItem>
                                    <Settings
                                        size={16}
                                        className="opacity-60"
                                        aria-hidden="true"
                                    />
                                    <span>Profile settings</span>
                                </DropdownMenuItem>
                                <DropdownMenuItem>
                                    <Moon
                                        size={16}
                                        className="opacity-60"
                                        aria-hidden="true"
                                    />
                                    <span>Theme</span>
                                </DropdownMenuItem>
                            </DropdownMenuGroup>
                            <DropdownMenuSeparator />
                            <DropdownMenuGroup>
                                <DropdownMenuItem>
                                    <Power
                                        size={16}
                                        className="opacity-60"
                                        aria-hidden="true"
                                    />
                                    <span>Log out</span>
                                </DropdownMenuItem>
                            </DropdownMenuGroup>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </div>
            </div>
        </header>
    );
};
