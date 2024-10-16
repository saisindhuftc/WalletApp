-- Insert users
INSERT INTO users (id, username, password) VALUES (1, 'john_doe', '$2a$10$7B1EBygzzfg/YOVlMzkEtuUtyE5UVHQrWtrR.SqBrJABP.8Qm0EiO'); -- bcrypt encoded password: 'password123'
INSERT INTO users (id, username, password) VALUES (2, 'jane_doe', '$2a$10$zB3O8BytZ5AqUZMEz2UT7uPJeqDDFW9oN7A3ZVO3PUnydvRUnDdkq'); -- bcrypt encoded password: 'mysecretpass'
INSERT INTO users (id, username, password) VALUES (3, 'alice_smith', '$2a$10$E9Bvkrh5lj5K/fiP8Z9YyuNhgogftz1ISshDLuKAwA.XoNjbUtExO'); -- bcrypt encoded password: 'alicepass'
INSERT INTO users (id, username, password) VALUES (4, 'bob_jones', '$2a$10$tSzzZD/bjEOZztYWflAeDuYizEOgQGoLR/D/Mp./TSPyVRTTbyROe'); -- bcrypt encoded password: 'bobpass'
INSERT INTO users (id, username, password) VALUES (5, 'carol_brown', '$2a$10$OPC.bmvZ1IcYhXtKM6BQoefStxHZlsJDLp3wLhBBUdR/YRde1hFZa'); -- bcrypt encoded password: 'carolpass'

-- Insert wallets for the users
INSERT INTO wallet (id, balance, user_id) VALUES (12, 100.0, 1); -- Wallet for john_doe
INSERT INTO wallet (id, balance, user_id) VALUES (13, 250.0, 2); -- Wallet for jane_doe
INSERT INTO wallet (id, balance, user_id) VALUES (14, 150.0, 3); -- Wallet for alice_smith
INSERT INTO wallet (id, balance, user_id) VALUES (15, 300.0, 4); -- Wallet for bob_jones
INSERT INTO wallet (id, balance, user_id) VALUES (16, 500.0, 5); -- Wallet for carol_brown


-- src/test/resources/schema.sql
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    password VARCHAR(255)
);