CREATE TABLE accounts (
    id UUID DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    provider VARCHAR(255) NOT NULL,
    provider_account_id TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY(id),
    CONSTRAINT fk_account_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT unique_provider_user UNIQUE (provider, provider_account_id)
);

CREATE TABLE password_reset_tokens (
    id UUID DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    token VARCHAR(10) NOT NULL,
    used BOOLEAN DEFAULT false,
    expired_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_prt_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX uidx_token ON password_reset_tokens(token);