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

CREATE TABLE tool(
    id INT PRIMARY KEY AUTO_INCREMENT,
    sensor_offset FLOAT NOT NULL,
    default_bur FLOAT NOT NULL
);