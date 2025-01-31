-- Приятели
CREATE TABLE friends(
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    friend_id INT NOT NULL,
    is_deleted BOOLEAN DEFAULT FALSE
);