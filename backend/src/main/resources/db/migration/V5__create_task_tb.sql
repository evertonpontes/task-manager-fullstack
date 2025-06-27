CREATE TABLE tasks (
    id UUID DEFAULT gen_random_uuid(),
    sort_index NUMERIC(10,5),
    title VARCHAR(100) NOT NULL,
    description TEXT,
    due_date TIMESTAMP,
    repeat BOOLEAN DEFAULT false,
    completed BOOLEAN DEFAULT false,
    priority VARCHAR(100) NOT NULL,
    estimated_time TIME,
    user_id UUID NOT NULL,
    task_type_id UUID NOT NULL,
    task_status_id UUID NOT NULL,
    project_id UUID NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_tasks_users FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_tasks_task_types FOREIGN KEY (task_type_id) REFERENCES task_types(id) ON DELETE CASCADE,
    CONSTRAINT fk_tasks_task_statuses FOREIGN KEY (task_status_id) REFERENCES task_statuses(id) ON DELETE CASCADE,
    CONSTRAINT fk_tasks_projects FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE
);

CREATE TABLE sub_tasks (
    id UUID DEFAULT gen_random_uuid(),
    sort_index NUMERIC(10,5),
    title VARCHAR(100) NOT NULL,
    due_date TIMESTAMP,
    completed BOOLEAN DEFAULT false,
    estimated_time TIME,
    parent_task_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_tasks_tasks FOREIGN KEY (parent_task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

CREATE TABLE scheduled_works (
    id UUID DEFAULT gen_random_uuid(),
    sort_index NUMERIC(10,5),
    date TIMESTAMP,
    estimated_time TIME,
    task_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_tasks_tasks FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

CREATE TABLE Logged_times (
    id UUID DEFAULT gen_random_uuid(),
    sort_index NUMERIC(10,5),
    date TIMESTAMP,
    spent_time TIME,
    billable BOOLEAN DEFAULT false,
    comment TEXT,
    task_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_tasks_tasks FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

CREATE TABLE scheduled_repeats (
    id UUID DEFAULT gen_random_uuid(),
    type TEXT NOT NULL,
    action TEXT NOT NULL,
    ends DATE,
    default_status_id UUID,
    due_date TIMESTAMP,
    starting_from DATE,
    skip_weekends BOOLEAN DEFAULT,
    estimated_time TIME,
    task_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_tasks_tasks FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    CONSTRAINT fk_tasks_task_statuses FOREIGN KEY (default_status) REFERENCES task_statuses(id) ON DELETE CASCADE
);

CREATE TABLE attached_files (
    id UUID DEFAULT gen_random_uuid(),
    sort_index NUMERIC(10,5),
    url TEXT NOT NULL,
    task_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_tasks_tasks FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);