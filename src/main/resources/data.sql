--all passwords: 12345

INSERT INTO users (id, username, email, enabled, password_hash)
VALUES (1, 'user', 'user@example.com', true, '$2a$08$Y5jCqL1DYeCedaKYdQGn7OEWVpvc8u1a3ThzQKa2Ai.wD5PvlmNlS');

INSERT INTO users (id, username, email, enabled, password_hash)
VALUES (2, 'user2', 'user2@example.com', true, '$2a$08$Y5jCqL1DYeCedaKYdQGn7OEWVpvc8u1a3ThzQKa2Ai.wD5PvlmNlS');

INSERT INTO users (id, username, email, enabled, password_hash)
VALUES (3, 'user3', 'user3@example.com', true, '$2a$08$Y5jCqL1DYeCedaKYdQGn7OEWVpvc8u1a3ThzQKa2Ai.wD5PvlmNlS');