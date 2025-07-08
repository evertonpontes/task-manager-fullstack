import { NodeType } from "@/types/node";
import { create } from "zustand";

type NodeStoreProps = {
    data: NodeType[];
    setData: (data: NodeType[]) => void;
    addData: (data: NodeType) => void;
    updateData: (id: string, data: NodeType) => void;
    deleteData: (id: string) => void;
};

export const useNodeStore = create<NodeStoreProps>((set) => ({
    data: [],
    setData: (data) => set({ data }),
    addData: (data) => set((state) => ({ data: [...state.data, data] })),
    updateData: (id, data) =>
        set((state) => ({
            data: state.data.map((item) => (item.id === id ? data : item)),
        })),
    deleteData: (id) =>
        set((state) => ({ data: state.data.filter((item) => item.id !== id) })),
}));
