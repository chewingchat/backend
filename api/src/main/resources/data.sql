-- User table mock data
INSERT INTO chewing.`user` (user_id, picture_url, status_message, user_name, birthday, created_at, modified_at)
VALUES ('user-1', 'https://example.com/user1.png', 'Hello World!', 'User One', '1990-01-01', NOW(), NOW()),
       ('user-2', 'https://example.com/user2.png', 'Living the life', 'User Two', '1985-05-15', NOW(), NOW()),
       ('user-3', 'https://example.com/user3.png', 'Good vibes only', 'User Three', '1992-07-20', NOW(), NOW());

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

-- Auth table mock data
INSERT INTO chewing.`auth` (auth_id, user_id, email_id, phone_number_id)
VALUES ('auth-1', 'user-1', 'email-1', 'phone-1'),
       ('auth-2', 'user-2', 'email-2', 'phone-2'),
       ('auth-3', 'user-3', 'email-3', 'phone-3');

INSERT INTO chewing.`sort` (sort_id, chat_room_sort, friends_sort, user_id)
VALUES
    ('sort-1', 'NOT_READ_DESC', 'NAME_ASC', 'user-1'),
    ('sort-2', 'NOT_READ_DESC', 'NAME_ASC', 'user-2'),
    ('sort-3', 'NOT_READ_DESC', 'NAME_ASC', 'user-3');