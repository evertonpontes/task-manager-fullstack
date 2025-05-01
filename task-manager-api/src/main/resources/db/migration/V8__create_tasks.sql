CREATE TABLE tasks (
    id TEXT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    due_date TIMESTAMP,
    estimated_time TIME,
    description TEXT,
    status VARCHAR(255),
    type VARCHAR(255),
    priority VARCHAR(255),
    repeat_task BOOLEAN DEFAULT false,
    sort_index DOUBLE PRECISION NOT NULL,
    assignee_id TEXT NOT NULL,
    organization_id TEXT NOT NULL,
    project_id TEXT,
    team_id TEXT,
    folder_id TEXT,

    CONSTRAINT fk_assignee_id FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE,
    CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_team_id FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    CONSTRAINT fk_folder_id FOREIGN KEY (folder_id) REFERENCES folders(id)
);

CREATE TABLE sub_tasks (
    id TEXT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    due_date TIMESTAMP,
    estimated_time TIME,
    completed BOOLEAN DEFAULT false,
    assignee_id TEXT NOT NULL,
    sort_index DOUBLE PRECISION NOT NULL,
    task_id TEXT NOT NULL,

    CONSTRAINT fk_assignee_id FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_id FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

CREATE TABLE task_attached_files (
    task_id TEXT NOT NULL,
    attached_file TEXT,

    PRIMARY KEY (task_id, attached_file),
    CONSTRAINT fk_task_id FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);