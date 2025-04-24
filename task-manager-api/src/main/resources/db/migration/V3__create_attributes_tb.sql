CREATE TABLE task_types (
    id TEXT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(255),
    organization_id TEXT,

    CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE
);

CREATE TABLE event_types (
    id TEXT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(255),
    organization_id TEXT,

    CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE
);

CREATE TABLE task_statuses (
    id TEXT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    color VARCHAR(255),
    organization_id TEXT,

    CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE
);