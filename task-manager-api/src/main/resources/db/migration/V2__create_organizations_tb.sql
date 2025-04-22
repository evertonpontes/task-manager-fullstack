CREATE TABLE organizations (
    id TEXT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    user_id TEXT NOT NULL,

    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE attendees_organizations (
    organization_id TEXT NOT NULL,
    user_id TEXT NOT NULL,
    PRIMARY KEY (organization_id, user_id),

    CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);