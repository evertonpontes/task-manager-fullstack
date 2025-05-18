CREATE TABLE users (
    id UUID DEFAULT gen_random_uuid(),
    role VARCHAR(10) DEFAULT 'USER',
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    picture TEXT,
    email TEXT NOT NULL,
    password TEXT NOT NULL,
    is_email_verified BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX uidx_user_email ON users(email);

CREATE TABLE verification_tokens (
    identifier TEXT NOT NULL,
    code VARCHAR(10) NOT NULL,
    expired_at TIMESTAMP NOT NULL,
    last_sent_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (identifier, code)
);

CREATE TABLE sessions (
    id UUID DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    session_token TEXT NOT NULL,
    expired_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_session_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX uidx_session_session_token ON sessions(session_token);