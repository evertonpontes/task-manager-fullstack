import { create } from "zustand";

type NodeModalProps = {
    open: boolean;
    setOpen: (open: boolean) => void;
    modalType: "create" | "rename" | "delete" | "none";
    setModalType: (type: "create" | "rename" | "delete" | "none") => void;
    kind: "Folder" | "Project";
    setKind: (kind: "Folder" | "Project") => void;
    id: string;
    setId: (id: string) => void;
    parentNodeId: string | null;
    setParentNodeId: (id: string | null) => void;
};

export const useNodeModal = create<NodeModalProps>((set) => ({
    open: false,
    setOpen: (open: boolean) => set({ open }),
    modalType: "none",
    setModalType: (type: "create" | "rename" | "delete" | "none") =>
        set({ modalType: type }),
    kind: "Folder",
    setKind: (kind: "Folder" | "Project") => set({ kind }),
    id: "",
    setId: (id: string) => set({ id }),
    parentNodeId: null,
    setParentNodeId: (id: string | null) => set({ parentNodeId: id }),
}));
