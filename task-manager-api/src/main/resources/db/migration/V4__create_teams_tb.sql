CREATE TABLE teams (
    id TEXT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    organization_id TEXT NOT NULL,

    CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE
);

CREATE TABLE attendees_teams (
    team_id TEXT NOT NULL,
    user_id TEXT NOT NULL,
    PRIMARY KEY (team_id, user_id),

    CONSTRAINT fk_team_id FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

ALTER TABLE task_types ADD team_id TEXT NOT NULL;
ALTER TABLE task_types ADD CONSTRAINT fk_team_id FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE;

ALTER TABLE event_types ADD team_id TEXT NOT NULL;
ALTER TABLE event_types ADD CONSTRAINT fk_team_id FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE;

ALTER TABLE task_statuses ADD team_id TEXT NOT NULL;
ALTER TABLE task_statuses ADD CONSTRAINT fk_team_id FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE CASCADE;