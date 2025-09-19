CREATE TABLE IF NOT EXISTS users (
        id VARCHAR(50) PRIMARY KEY,
        email VARCHAR(100) NOT NULL UNIQUE,
        password_hash TEXT NULL,
        provider VARCHAR(20) NOT NULL,
        display_name TEXT,
        role VARCHAR(20)
    );

CREATE TABLE IF NOT EXISTS tasks (
    id VARCHAR(50) PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    title VARCHAR(100) NOT NULL,
    des TEXT,
    status VARCHAR(20) DEFAULT 'DOING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
);

CREATE TABLE IF NOT EXISTS refresh_tokens(
     id VARCHAR(50) PRIMARY KEY,
     user_id VARCHAR(50),
     token_hash TEXT NOT NULL,
     expires_at TIMESTAMPTZ NOT NULL,
     created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
     FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
)
