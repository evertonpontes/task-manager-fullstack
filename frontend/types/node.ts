export type TaskType = {
    id: string;
    orderIndex: number;
    name: string;
    color: string;
    nodeId: string;
    createdAt: Date;
    updatedAt: Date;
};

export type TaskStatus = {
    id: string;
    orderIndex: number | null;
    name: string;
    kind: "Custom" | "Completed";
    color: string;
    nodeId: string;
    deletable: boolean;
    draggable: boolean;
    createdAt: Date;
    updatedAt: Date;
};

export type NodeType = {
    id: string;
    rank: number;
    name: string;
    kind: "Folder" | "Project";
    parentNodeId: string | null;
    userId: string;
    children: NodeType[];
    taskTypes: TaskType[];
    taskStatuses: TaskStatus[];
    createdAt: Date;
    updatedAt: Date;
    collapsed?: boolean;
};

export type FlattenedItem = NodeType & {
    depth: number;
};
