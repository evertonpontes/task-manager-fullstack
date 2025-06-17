CREATE TABLE folders (
    id UUID DEFAULT gen_random_uuid(),
    sort_index NUMERIC(10,5),
    name VARCHAR(100) NOT NULL,
    user_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_folder_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE projects (
    id UUID DEFAULT gen_random_uuid(),
    sort_index NUMERIC(10,5),
    name VARCHAR(100) NOT NULL,
    user_id UUID NOT NULL,
    folder_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_project_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_folder FOREIGN KEY (folder_id) REFERENCES folders(id) ON DELETE CASCADE
);