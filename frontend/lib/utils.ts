import { FlattenedItem, NodeType } from "@/types/node";
import { clsx, type ClassValue } from "clsx";
import { twMerge } from "tailwind-merge";

export function cn(...inputs: ClassValue[]) {
    return twMerge(clsx(inputs));
}

export function decodeJwt(token: string): { exp: number } | null {
    try {
        const base64Url = token.split(".")[1];
        const decoded = atob(base64Url);
        return JSON.parse(decoded);
    } catch (error) {
        if (error instanceof Error) {
            console.log(error.message);
        }

        return null;
    }
}

export function flattenTree(items: NodeType[], depth = 0): FlattenedItem[] {
    if (!items) return [];
    return items.reduce<FlattenedItem[]>((acc, item) => {
        return [
            ...acc,
            { ...item, depth },
            ...flattenTree(item.children, depth + 1),
        ];
    }, []);
}
