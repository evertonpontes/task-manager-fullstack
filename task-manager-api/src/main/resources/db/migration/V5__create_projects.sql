CREATE TABLE projects (
    id TEXT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id TEXT NOT NULL,
    folder_id TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_folder_id FOREIGN KEY (folder_id) REFERENCES folders(id) ON DELETE CASCADE
);

CREATE TABLE project_members (
    project_id TEXT NOT NULL,
    member_id TEXT NOT NULL,

    CONSTRAINT fk_project_id FOREIGN KEY (project_id) REFERENCES projects(id) ON DELETE CASCADE,
    CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TRIGGER trigger_update_project_updated_at
BEFORE UPDATE ON projects
FOR EACH ROW
EXECUTE PROCEDURE updated_at_function();