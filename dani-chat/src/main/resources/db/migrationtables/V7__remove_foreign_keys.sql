ALTER TABLE channels DROP CONSTRAINT FK_created_by;
ALTER TABLE channel_users DROP CONSTRAINT FK_channel;
ALTER TABLE channel_users DROP CONSTRAINT FK_user;
ALTER TABLE messages DROP CONSTRAINT FK_sender;
ALTER TABLE messages DROP CONSTRAINT FK_receiver;
ALTER TABLE messages DROP CONSTRAINT FK_channel_message;
ALTER TABLE friends DROP CONSTRAINT FK_user_friend;
ALTER TABLE friends DROP CONSTRAINT FK_friend_user;
