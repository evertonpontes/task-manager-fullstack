CREATE TABLE folders (
    id TEXT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    organization_id TEXT NOT NULL,

    CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE
);

CREATE TABLE projects (
    id TEXT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    organization_id TEXT NOT NULL,
    folder_id TEXT NOT NULL,

    CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE,
    CONSTRAINT fk_folder_id FOREIGN KEY (folder_id) REFERENCES folders(id) ON DELETE CASCADE
);

CREATE TABLE attendees_projects (
    project_id TEXT NOT NULL,
    user_id TEXT NOT NULL,
    PRIMARY KEY (project_id, user_id),

    CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

ALTER TABLE task_types ADD project_id TEXT NOT NULL;
ALTER TABLE task_types ADD CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE;

ALTER TABLE event_types ADD project_id TEXT NOT NULL;
ALTER TABLE event_types ADD CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE;

ALTER TABLE task_statuses ADD project_id TEXT NOT NULL;
ALTER TABLE task_statuses ADD CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE;