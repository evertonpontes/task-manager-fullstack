"use client";

import { NodeType } from "@/types/node";
import { createContext, Dispatch, SetStateAction, useState } from "react";

interface SidebarSortableProviderProps {
    children: React.ReactNode;
}

interface SidebarSortableContextProps {
    isLoaded: boolean;
    setIsLoaded: Dispatch<SetStateAction<boolean>>;
    filter: NodeType[];
    setFilter: Dispatch<SetStateAction<NodeType[]>>;
    activeId: string | null;
    setActiveId: Dispatch<SetStateAction<string | null>>;
    overId: string | null;
    setOverId: Dispatch<SetStateAction<string | null>>;
    offsetLeft: number;
    setOffsetLeft: Dispatch<SetStateAction<number>>;
    isDragging: boolean;
    setIsDragging: Dispatch<SetStateAction<boolean>>;
}

export const SidebarSortableContext =
    createContext<SidebarSortableContextProps>(
        {} as SidebarSortableContextProps
    );

export const SidebarSortableProvider: React.FC<
    SidebarSortableProviderProps
> = ({ children }) => {
    const [isLoaded, setIsLoaded] = useState(false);
    const [filter, setFilter] = useState<NodeType[]>([]);
    const [activeId, setActiveId] = useState<string | null>(null);
    const [overId, setOverId] = useState<string | null>(null);
    const [offsetLeft, setOffsetLeft] = useState(0);
    const [isDragging, setIsDragging] = useState(false);

    return (
        <SidebarSortableContext.Provider
            value={{
                isLoaded,
                setIsLoaded,
                filter,
                setFilter,
                activeId,
                setActiveId,
                overId,
                setOverId,
                offsetLeft,
                setOffsetLeft,
                isDragging,
                setIsDragging,
            }}
        >
            {children}
        </SidebarSortableContext.Provider>
    );
};
