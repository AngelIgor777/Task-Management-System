CREATE TABLE IF NOT EXISTS tasks
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT         NOT NULL,
    status      VARCHAR(50) DEFAULT 'в ожидании' CHECK (status IN ('в ожидании', 'в процессе', 'завершено')),
    priority    VARCHAR(50) DEFAULT 'средний' CHECK (priority IN ('высокий', 'средний', 'низкий')),
    author_id   INT          NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    assignee_id INT          REFERENCES users (id) ON DELETE SET NULL,
    created_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
);