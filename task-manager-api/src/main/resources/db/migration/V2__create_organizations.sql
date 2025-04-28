CREATE TABLE organizations (
    id TEXT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    owner_id TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_owner_id FOREIGN KEY (owner_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE organization_members (
    organization_id TEXT NOT NULL,
    member_id TEXT NOT NULL,

    CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE,
    CONSTRAINT fk_member_id FOREIGN KEY (member_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TRIGGER trigger_update_organization_updated_at
BEFORE UPDATE ON organizations
FOR EACH ROW
EXECUTE PROCEDURE updated_at_function();