ALTER TABLE teams ADD organization_id TEXT NOT NULL;
ALTER TABLE teams ADD CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE;

ALTER TABLE folders ADD organization_id TEXT NOT NULL;
ALTER TABLE folders ADD CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE;

ALTER TABLE projects ADD organization_id TEXT NOT NULL;
ALTER TABLE projects ADD CONSTRAINT fk_organization_id FOREIGN KEY (organization_id) REFERENCES organizations(id) ON DELETE CASCADE;