INSERT INTO chewing.`emoticon_pack` (emoticon_pack_id, emoticon_pack_name, emoticon_pack_url, created_at, modified_at)
VALUES ('emoticon-pack-1', 'Default', 'https://example.com/default.png', NOW(), NOW());
-- Emoticon table mock data
INSERT INTO chewing.`emoticon` (emoticon_id, emoticon_name, emoticon_url, emoticon_pack_id)
VALUES ('emoticon-1', 'smile', 'https://example.com/smile.png', 'emoticon-pack-1'),
       ('emoticon-2', 'sad', 'https://example.com/sad.png', 'emoticon-pack-1'),
       ('emoticon-3', 'angry', 'https://example.com/angry.png', 'emoticon-pack-1');


-- Email table mock data
INSERT INTO chewing.`email` (email_id, email, first_authorized, created_at, modified_at)
VALUES ('email-1', 'user1@example.com', TRUE, NOW(), NOW()),
       ('email-2', 'user2@example.com', FALSE, NOW(), NOW()),
       ('email-3', 'user3@example.com', TRUE, NOW(), NOW());

-- Phone Number table mock data
INSERT INTO chewing.`phone_number` (phone_number_id, phone_number, first_authorized, country_code, created_at,
                                    modified_at)
VALUES ('phone-1', '+1234567890', TRUE, '+1', NOW(), NOW()),
       ('phone-2', '+1987654321', FALSE, '+1', NOW(), NOW()),
       ('phone-3', '+1098765432', TRUE, '+1', NOW(), NOW());

INSERT INTO chewing.`user` (user_id, picture_url, background_picture_url, status_message, user_first_name,
                            user_last_name, birthday, created_at, modified_at, emoticon_id)
VALUES ('user-1', 'https://example.com/user1.png', 'https://example.com/user1.png', 'Hello World!', 'User', 'One',
        '1990-01-01', NOW(), NOW(), 'emoticon-1'),
       ('user-2', 'https://example.com/user2.png', 'https://example.com/user1.png', 'Living the life', 'User', 'Two',
        '1985-05-15', NOW(), NOW(), 'emoticon-1'),
       ('user-3', 'https://example.com/user3.png', 'https://example.com/user1.png', 'Good vibes only', 'User', 'Three',
        '1992-07-20', NOW(), NOW(), 'emoticon-1');

-- Auth table mock data
INSERT INTO chewing.`auth` (auth_id, user_id, email_id, phone_number_id)
VALUES ('auth-1', 'user-1', 'email-1', 'phone-1'),
       ('auth-2', 'user-2', 'email-2', 'phone-2'),
       ('auth-3', 'user-3', 'email-3', 'phone-3');

INSERT INTO chewing.`feed` (feed_id, user_id, feed_topic, created_at, modified_at, likes, version)
VALUES ('feed-1', 'user-1', 'Hello World!', NOW(), NOW(), 0, 0),
       ('feed-2', 'user-2', 'Living the life', NOW(), NOW(), 0, 0),
       ('feed-3', 'user-3', 'Good vibes only', NOW(), NOW(), 0, 0);

INSERT INTO chewing.feed_detail (feed_detail_id, feed_id, feed_detail_type, feed_detail_url, feed_index)
VALUES ('feed-detail-1', 'feed-1', 'IMAGE', 'https://example.com/feed1.png', 0),
       ('feed-detail-2', 'feed-2', 'IMAGE', 'https://example.com/feed2.png', 0),
       ('feed-detail-3', 'feed-3', 'IMAGE', 'https://example.com/feed3.png', 0),
       ('feed-detail-4', 'feed-1', 'VIDEO', 'https://example.com/feed3.png', 1),
       ('feed-detail-5', 'feed-2', 'VIDEO', 'https://example.com/feed3.png', 1),
       ('feed-detail-6', 'feed-3', 'VIDEO', 'https://example.com/feed3.png', 1)


