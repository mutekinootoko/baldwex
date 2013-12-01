SET NAMES utf8;
SET CHARACTER_SET_CLIENT=utf8;
SET CHARACTER_SET_RESULTS=utf8;

CREATE DATABASE IF NOT EXISTS `lwdba` CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `lwdba`

DROP TABLE IF EXISTS Customer;

CREATE TABLE Customer
( 
    seqNo            BIGINT  AUTO_INCREMENT NOT NULL,
    name             varchar(64)                    ,
    phone            varchar(64)                    ,
    address          mediumtext                     ,
    createTime       DATETIME                       ,
    status           int                    default 1 NULL,
    primary key (seqNo)
) CHARACTER SET utf8 COLLATE utf8_general_ci;

