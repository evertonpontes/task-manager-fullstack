CREATE TABLE scheduled_works (
    id TEXT PRIMARY KEY,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estimated_time TIME,
    completed BOOLEAN DEFAULT false,
    assignee_id TEXT NOT NULL,
    task_id TEXT NOT NULL,
    sort_index DOUBLE PRECISION NOT NULL,

    CONSTRAINT fk_assignee_id FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_id FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

CREATE TABLE logged_times (
    id TEXT PRIMARY KEY,
    date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    spent_time TIME,
    billable BOOLEAN DEFAULT false,
    comment TEXT,
    assignee_id TEXT NOT NULL,
    task_id TEXT NOT NULL,
    sort_index DOUBLE PRECISION NOT NULL,

    CONSTRAINT fk_assignee_id FOREIGN KEY (assignee_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_task_id FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);