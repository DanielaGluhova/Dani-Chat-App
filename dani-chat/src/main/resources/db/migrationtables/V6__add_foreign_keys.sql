-- Външни ключове за таблицата channels
ALTER TABLE channels
    ADD CONSTRAINT FK_created_by
        FOREIGN KEY (created_by) REFERENCES users(id) ON DELETE CASCADE;

-- Външни ключове за таблицата channel_users
ALTER TABLE channel_users
    ADD CONSTRAINT FK_channel
        FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE;

ALTER TABLE channel_users
    ADD CONSTRAINT FK_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

-- Външни ключове за таблицата messages
ALTER TABLE messages
    ADD CONSTRAINT FK_sender
        FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE messages
    ADD CONSTRAINT FK_receiver
        FOREIGN KEY (receiver_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE messages
    ADD CONSTRAINT FK_channel_message
        FOREIGN KEY (channel_id) REFERENCES channels(id) ON DELETE CASCADE;

-- Външни ключове за таблицата friends
ALTER TABLE friends
    ADD CONSTRAINT FK_user_friend
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE;

ALTER TABLE friends
    ADD CONSTRAINT FK_friend_user
        FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE;
