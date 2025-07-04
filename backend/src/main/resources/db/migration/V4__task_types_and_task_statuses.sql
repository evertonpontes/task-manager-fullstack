CREATE TABLE task_types (
    id UUID DEFAULT gen_random_uuid(),
    order_index BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    color VARCHAR(100) NOT NULL,
    node_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_task_types_node_id FOREIGN KEY (node_id) REFERENCES nodes(id) ON DELETE CASCADE
);

CREATE TABLE task_statuses (
    id UUID DEFAULT gen_random_uuid(),
    order_index BIGINT,
    name VARCHAR(100) NOT NULL,
    kind VARCHAR(100) DEFAULT 'Custom',
    color VARCHAR(100) NOT NULL,
    node_id UUID NOT NULL,
    deletable BOOLEAN DEFAULT true,
    draggable BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_task_statuses_node_id FOREIGN KEY (node_id) REFERENCES nodes(id) ON DELETE CASCADE
);