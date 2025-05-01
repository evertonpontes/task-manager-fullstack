CREATE TABLE organizations (
    id TEXT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id TEXT NOT NULL,
    sort_index DOUBLE PRECISION NOT NULL,


    CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE organization_members (
    group_id TEXT NOT NULL,
    member_id TEXT NOT NULL,
    sort_index DOUBLE PRECISION NOT NULL,

    CONSTRAINT fk_group_id FOREIGN KEY (group_id) REFERENCES organizations(id) ON DELETE CASCADE,
    CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES users(id) ON DELETE CASCADE
);