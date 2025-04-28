CREATE TABLE teams (
    id TEXT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE team_members (
    team_id TEXT NOT NULL,
    member_id TEXT NOT NULL,

    CONSTRAINT fk_team_id FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TRIGGER trigger_update_team_updated_at
BEFORE UPDATE ON teams
FOR EACH ROW
EXECUTE PROCEDURE updated_at_function();