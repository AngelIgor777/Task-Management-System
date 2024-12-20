CREATE TABLE IF NOT EXISTS comments
(
    id         SERIAL PRIMARY KEY,
    task_id    INT  NOT NULL REFERENCES tasks (id) ON DELETE CASCADE,
    user_id    INT  NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    content    TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);