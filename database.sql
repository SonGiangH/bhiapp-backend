-- CREATE DATABASE bakerhughesapp;
USE BhiApp;

-- bảng surveys
CREATE TABLE surveys(
    id INT PRIMARY KEY AUTO_INCREMENT,
    dp_length FLOAT DEFAULT 9.2,
    bit_depth FLOAT NOT NULL, 
    survey_depth FLOAT NOT NULL,
    inc FLOAT NOT NULL,
    azi FLOAT NOT NULL,
    totalseen FLOAT NOT NULL,
    burm FLOAT NOT NULL,
    bur30m FLOAT NOT NULL,
    incbit FLOAT NOT NULL,
    toolface Varchar(10) DEFAULT '',
    st FLOAT NOT NULL,
    ed FLOAT NOT NULL,
    totalslid FLOAT NOT NULL,
    slidseen FLOAT NOT NULL,
    slidunseen FLOAT NOT NULL
);

-- bảng tool
CREATE TABLE tool(
    id INT PRIMARY KEY AUTO_INCREMENT,
    sensor_offset FLOAT NOT NULL,
    default_bur FLOAT NOT NULL
);

-- bảng user
CREATE TABLE users(
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(100) NOT NULL,  --pass encrypted
    created_at DATETIME,
    updated_at DATETIME,
    is_active TINYINT(1) DEFAULT 1,
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0,
    goolge_account_id INT DEFAULT 0 
);

-- bảng token
CREATE TABLE tokens(
    id INT PRIMARY KEy AUTO_INCREMENT,
    token VARCHAR(255) UNIQUE NOT NULL,
    token_type VARCHAR(50) NOT NULL,
    expiration_date DATE,
    revoked TINYINT(1) NOT NULL,
    expired TINYINT(1) NOT NULL,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id) 
);

-- Support log in from Goolge and Facebook
CREATE TABLE social_accounts(
    id INT PRIMARY KEY AUTO_INCREMENT,
    `provider` VARCHAR(20) NOT NULL COMMENT "Name of Social network",
    `provider_id` VARCHAR(50) NOT NULL,
    email VARCHAR(150) NOT NULL COMMENT "email of goolge/facebook account",
    `name` VARCHAR(150) NOT NULL COMMENT "user 's name",
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id) 
);

-- Roles
CREATE TABLE roles(
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(20) NOT NULL
);

ALTER TABLE users ADD COLUMN role_id INT;
ALTER TABLE users ADD FOREIGN KEY (role_id) REFERENCES roles(id);