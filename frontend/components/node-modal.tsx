"use client";
import React, { useEffect, useMemo } from "react";
import { Modal } from "./ui/modal";
import { Input } from "./ui/input";
import { useNodeModal } from "@/hooks/use-node-modal";
import { NodeType } from "@/types/node";
import axios from "axios";
import { useNodeStore } from "@/hooks/use-node-store";
import { toast } from "react-toastify";
import { flattenTree } from "@/lib/utils";

export const NodeModal = () => {
    const [activeNode, setActiveNode] = React.useState<NodeType | null>(null);

    const nodes = useNodeStore((state) => state.data);
    const modalType = useNodeModal((state) => state.modalType);
    const open = useNodeModal((state) => state.open);
    const setOpen = useNodeModal((state) => state.setOpen);
    const kind = useNodeModal((state) => state.kind);
    const id = useNodeModal((state) => state.id);
    const setId = useNodeModal((state) => state.setId);
    const setKind = useNodeModal((state) => state.setKind);
    const setModalType = useNodeModal((state) => state.setModalType);
    const parentNodeId = useNodeModal((state) => state.parentNodeId);
    const setParentNodeId = useNodeModal((state) => state.setParentNodeId);

    useEffect(() => {
        if (id) {
            const node = nodes.find((node) => node.id === id);
            if (node) {
                setActiveNode(node);
            }
        }
    }, [id, nodes]);

    const handleOpenChange = (open: boolean) => {
        setOpen(!open);
        if (!open) {
            setModalType("none");
            setKind("Folder");
            setId("");
            setParentNodeId(null);
        }
    };

    if (modalType === "create") {
        return (
            <CreateNodeModal
                open={open}
                onOpenChange={handleOpenChange}
                kind={kind}
                parentNodeId={parentNodeId}
            />
        );
    } else if (modalType === "rename") {
        return (
            <UpdateNodeModal
                open={open}
                onOpenChange={handleOpenChange}
                kind={kind}
                id={id}
            />
        );
    } else if (modalType === "delete") {
        return (
            <DeleteNodeModal
                open={open}
                onOpenChange={handleOpenChange}
                kind={kind}
                id={id}
            />
        );
    }
};

interface CreateNodeModalProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    kind: "Folder" | "Project";
    parentNodeId?: string | null;
}

const CreateNodeModal: React.FC<CreateNodeModalProps> = ({
    open,
    onOpenChange,
    kind,
    parentNodeId,
}) => {
    const [name, setName] = React.useState("");
    const nodes = useNodeStore((state) => state.data);
    const addNode = useNodeStore((state) => state.addData);
    const updateNode = useNodeStore((state) => state.updateData);

    const api = parentNodeId
        ? `/api/nodes/${parentNodeId}/projects`
        : `/api/nodes?kind=${kind}`;

    const handleChangeName = (e: React.ChangeEvent<HTMLInputElement>) =>
        setName(e.target.value);

    const createNode = async () => {
        try {
            const response = await axios.post<NodeType>(
                api,
                {
                    name,
                },
                {
                    withCredentials: true,
                }
            );

            if (parentNodeId) {
                const parent = nodes.find((node) => node.id === parentNodeId);
                if (parent) {
                    parent.children = parent.children || [];
                    parent.children.push(response.data);
                    updateNode(parentNodeId, parent);
                }
            } else {
                addNode(response.data);
            }

            toast.success(`${kind} created successfully.`, {
                position: "bottom-left",
            });
            onOpenChange(false);
        } catch (error) {
            if (error instanceof Error) {
                console.log(error.message);
                toast.error(`${kind} creation failed.`, {
                    position: "bottom-left",
                });
            }
        }
    };

    return (
        <Modal
            open={open}
            onOpenChange={onOpenChange}
            title={"Create " + kind}
            actionText={"Create " + kind}
            action={() => createNode()}
        >
            <Input
                type="text"
                placeholder={kind + " name"}
                value={name}
                onChange={handleChangeName}
                aria-describedby="name"
            />
        </Modal>
    );
};

interface DeleteNodeModalProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    kind: "Folder" | "Project";
    id: string;
}

const DeleteNodeModal: React.FC<DeleteNodeModalProps> = ({
    open,
    onOpenChange,
    kind,
    id,
}) => {
    const removeNode = useNodeStore((state) => state.deleteData);

    const description =
        kind === "Folder"
            ? "Are you sure you want to delete the whole folder?"
            : "Are you sure you want to permanently delete this project?";

    const deleteNode = async () => {
        try {
            await axios.delete(`/api/nodes/${id}`, {
                withCredentials: true,
            });
            removeNode(id);
            toast.success(`${kind} deleted successfully.`, {
                position: "bottom-left",
            });
            onOpenChange(false);
        } catch (error) {
            if (error instanceof Error) {
                console.log(error.message);
                toast.error(`${kind} deletion failed.`, {
                    position: "bottom-left",
                });
            }
        }
    };

    return (
        <Modal
            open={open}
            onOpenChange={onOpenChange}
            title={"Delete " + kind}
            actionText={"Delete " + kind}
            action={() => deleteNode()}
            actionType="destructive"
        >
            <span className="text-muted-foreground text-sm">{description}</span>
        </Modal>
    );
};

interface UpdateNodeModalProps {
    open: boolean;
    onOpenChange: (open: boolean) => void;
    kind: "Folder" | "Project";
    id: string;
}

const UpdateNodeModal: React.FC<UpdateNodeModalProps> = ({
    open,
    onOpenChange,
    kind,
    id,
}) => {
    const node: NodeType = useMemo(() => {
        const nodes = useNodeStore.getState().data;
        const flattenedItems = flattenTree(nodes);
        return flattenedItems.find((node) => node.id === id) as NodeType;
    }, [id]);

    const [name, setName] = React.useState(node?.name);
    const updateNode = useNodeStore((state) => state.updateData);

    const handleChangeName = (e: React.ChangeEvent<HTMLInputElement>) =>
        setName(e.target.value);

    const renameNode = async () => {
        try {
            const response = await axios.put<NodeType>(
                `/api/nodes/${id}`,
                {
                    name,
                    rank: node?.rank,
                    parentNodeId: node?.parentNodeId,
                },
                {
                    withCredentials: true,
                }
            );

            updateNode(id, response.data);
            toast.success(`${kind} renamed successfully.`, {
                position: "bottom-left",
            });
            onOpenChange(false);
        } catch (error) {
            if (error instanceof Error) {
                console.log(error.message);
                toast.error(`${kind} update failed.`, {
                    position: "bottom-left",
                });
            }
        }
    };

    return (
        <Modal
            open={open}
            onOpenChange={onOpenChange}
            title={"Rename " + kind}
            actionText={"Rename " + kind}
            action={() => renameNode()}
        >
            <Input
                type="text"
                placeholder={kind + " name"}
                value={name}
                onChange={handleChangeName}
                aria-describedby="name"
            />
        </Modal>
    );
};
