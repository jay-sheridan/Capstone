INSERT INTO users (full_name, email, password_hash, role, phone, status, created_at, updated_at)
VALUES
('Jay Patel', 'jay@example.com', '$2a$10$U7PhLuaE.tu3ZmIhpjTkyenFzHli2XAE41jKUe2hD0E7ZFJ7fBhPy', 'BUYER', '1234567890', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Test Dealer', 'dealer@example.com', '$2a$10$hashedpasswordhere', 'DEALER', '9876543210', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);