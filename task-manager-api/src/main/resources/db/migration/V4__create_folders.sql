CREATE TABLE folders (
    id TEXT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TRIGGER trigger_update_folder_updated_at
BEFORE UPDATE ON folders
FOR EACH ROW
EXECUTE PROCEDURE updated_at_function();