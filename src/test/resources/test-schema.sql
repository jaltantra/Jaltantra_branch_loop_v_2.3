CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    country VARCHAR(255) NOT NULL,
    state VARCHAR(255),
    password VARCHAR(255) NOT NULL,
    organization VARCHAR(255) NOT NULL,
    designation VARCHAR(255) NOT NULL,
    enabled BOOLEAN NOT NULL,
    verification_code VARCHAR(255)
    );
