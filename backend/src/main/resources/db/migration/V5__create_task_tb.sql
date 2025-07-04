CREATE TABLE tasks (
    id UUID DEFAULT gen_random_uuid(),
    common_rank NUMERIC(20,0) NOT NULL,
    status_rank INTEGER NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    start_at TIMESTAMP,
    due_date TIMESTAMP,
    spent_time INTEGER DEFAULT 0,
    priority VARCHAR(100) NOT NULL,
    estimated_time INTEGER DEFAULT 0,
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
    CONSTRAINT fk_tasks_nodes FOREIGN KEY (project_id) REFERENCES nodes(id) ON DELETE CASCADE
);

CREATE TABLE sub_tasks (
    id UUID DEFAULT gen_random_uuid(),
    rank NUMERIC(20,0) NOT NULL,
    title VARCHAR(100) NOT NULL,
    due_date TIMESTAMP,
    status VARCHAR(100) DEFAULT 'Active',
    estimated_time INTEGER DEFAULT 0,
    parent_task_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_sub_tasks_tasks FOREIGN KEY (parent_task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

CREATE TABLE time_blocks (
    id UUID DEFAULT gen_random_uuid(),
    rank NUMERIC(20,0) NOT NULL,
    start_at TIMESTAMP,
    status VARCHAR(100) DEFAULT 'Active',
    duration INTEGER DEFAULT 0,
    spent_time INTEGER DEFAULT 0,
    manual_spent_time INTEGER DEFAULT 0,
    billable BOOLEAN DEFAULT false,
    comment TEXT,
    task_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_time_blocks_tasks FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);

CREATE TABLE recurrence_rules (
      id UUID DEFAULT gen_random_uuid(),
      rule TEXT NOT NULL,
      rule_text TEXT NOT NULL,
      frequency VARCHAR(100) NOT NULL,
      initial_task_status_id UUID NOT NULL,
      due_date_shift INTEGER DEFAULT 0,
      estimated_time INTEGER DEFAULT 0,
      task_id UUID,
      created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
      PRIMARY KEY (id),
      CONSTRAINT fk_recurrence_rules_tasks FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
      CONSTRAINT fk_recurrence_rules_task_statuses FOREIGN KEY (initial_task_status_id) REFERENCES task_statuses(id) ON DELETE CASCADE
);

CREATE TABLE repeatable_rules (
    id UUID DEFAULT gen_random_uuid(),
    initial_task_status_id UUID NOT NULL,
    until TIMESTAMP,
    due_date_shift INTEGER DEFAULT 0,
    initial_time_block_duration INTEGER DEFAULT 0,
    start_at_date_shift INTEGER DEFAULT 0,
    task_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_recurrence_rules_tasks FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE,
    CONSTRAINT fk_recurrence_rules_task_statuses FOREIGN KEY (initial_task_status_id) REFERENCES task_statuses(id) ON DELETE CASCADE
);

CREATE TABLE attached_files (
    id UUID DEFAULT gen_random_uuid(),
    sort_index NUMERIC(10,5),
    url TEXT NOT NULL,
    task_id UUID,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_attached_files_tasks FOREIGN KEY (task_id) REFERENCES tasks(id) ON DELETE CASCADE
);