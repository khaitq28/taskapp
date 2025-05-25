CREATE TABLE IF NOT EXISTS users (
     id SERIAL PRIMARY KEY,
     name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    role VARCHAR(20)
    );

CREATE TABLE IF NOT EXISTS tasks (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    title VARCHAR(100) NOT NULL,
    des TEXT,
    status VARCHAR(20) DEFAULT 'pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE RESTRICT
    );

-- INSERT INTO users (id, name, email, role) VALUES
-- (1, 'AdminY2k', 'user1@gmail.com', 'admin'),
-- (2, 'UserNormalY2k', 'user2@gmail.com', 'user');
--
--
-- INSERT INTO tasks (id, user_id, title, des) VALUES
-- (1, 1, 'Task 1', 'This is the first task description');