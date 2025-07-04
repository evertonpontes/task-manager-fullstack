CREATE TABLE nodes (
    id UUID DEFAULT gen_random_uuid(),
    rank BIGINT NOT NULL,
    kind VARCHAR(100) DEFAULT 'Project',
    name VARCHAR(100) NOT NULL,
    parent_node_id UUID,
    user_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_folder_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_folder_parent_node FOREIGN KEY (parent_node_id) REFERENCES nodes(id) ON DELETE CASCADE
);