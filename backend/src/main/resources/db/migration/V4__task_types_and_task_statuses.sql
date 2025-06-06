CREATE TABLE task_types (
    id UUID DEFAULT gen_random_uuid(),
    sort_order NUMERIC,
    name VARCHAR(100) NOT NULL,
    color CHAR(7) NOT NULL,
    project_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_task_types_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE task_statuses (
    id UUID DEFAULT gen_random_uuid(),
    sort_order NUMERIC,
    name VARCHAR(100) NOT NULL,
    color CHAR(7) NOT NULL,
    is_task_completed BOOLEAN DEFAULT false,
    project_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_task_statuses_project FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);