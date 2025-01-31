-- Таблица отговаряща за мембърите на канал
CREATE TABLE channel_users(
    id INT AUTO_INCREMENT PRIMARY KEY,
    channel_id INT NOT NULL,
    user_id INT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);