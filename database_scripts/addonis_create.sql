DROP DATABASE IF EXISTS `addonis`;
CREATE DATABASE IF NOT EXISTS `addonis`;

USE `addonis`;

DROP TABLE IF EXISTS `users`;
CREATE TABLE IF NOT EXISTS `users`
(
    `id`       INT PRIMARY KEY AUTO_INCREMENT,
    `username` VARCHAR(20) UNIQUE NOT NULL,
    `password` VARCHAR(20)        NOT NULL,
    `email`    VARCHAR(50) UNIQUE NOT NULL,
    `phone`    VARCHAR(11) UNIQUE NOT NULL,
    `photo`    LONGBLOB               NULL,
    `enabled`  BOOLEAN            NOT NULL DEFAULT FALSE,
    `admin`    BOOLEAN            NOT NULL DEFAULT FALSE

);


DROP TABLE IF EXISTS `confirmation_tokens`;
CREATE TABLE IF NOT EXISTS `confirmation_tokens`
(
    `id`              INT PRIMARY KEY AUTO_INCREMENT,
    `token`           VARCHAR(100) NOT NULL,
    `expiration_date` DATE         NOT NULL,
    `user_id`         INT          NOT NULL,
    CONSTRAINT `confirmation_tokens_users`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
);

DROP TABLE IF EXISTS `ides`;
CREATE TABLE IF NOT EXISTS `ides`
(
    `id`   INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(30) UNIQUE NOT NULL
);

DROP TABLE IF EXISTS `statuses`;
CREATE TABLE IF NOT EXISTS `statuses`
(
    `id`   INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(30) UNIQUE NOT NULL
);

DROP TABLE IF EXISTS `tags`;
CREATE TABLE IF NOT EXISTS `tags`
(
    `id`   INT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(30) UNIQUE NOT NULL
);


DROP TABLE IF EXISTS `github_data`;
CREATE TABLE IF NOT EXISTS `github_data`
(
    id                 INT PRIMARY KEY AUTO_INCREMENT,
    pulls_count        INT          NULL,
    open_issues_count  INT          NULL,
    last_commit_date   DATE         NULL,
    last_commit_tittle VARCHAR(1000) NULL
);

DROP TABLE IF EXISTS `addons`;
CREATE TABLE IF NOT EXISTS `addons`
(
    `id`             INT PRIMARY KEY AUTO_INCREMENT,
    `name`           VARCHAR(50) UNIQUE NOT NULL,
    `ide_id`         INT                NOT NULL,
    `creator_id`     INT                NOT NULL,
    `description`    TEXT               NOT NULL,
    `image`          LONGBLOB,
    `binary_content` LONGBLOB,
    `origin_link`    VARCHAR(500)       NOT NULL,
    `featured`       BOOLEAN            NOT NULL DEFAULT FALSE,
    `github_data_id` INT,
    `status_id`      INT                NOT NULL DEFAULT 1,
    `download_count` INT                DEFAULT 0,
    `total_score`    INT                DEFAULT 0,
    `total_voters`   INT                DEFAULT 0,
    `upload_date`    DATE               NOT NULL,

    CONSTRAINT `addons_ides`
        FOREIGN KEY (`ide_id`) REFERENCES `ides` (`id`),
    CONSTRAINT `addons_users`
        FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`),
    CONSTRAINT `addons_statuses`
        FOREIGN KEY (`status_id`) REFERENCES `statuses` (`id`),
    CONSTRAINT `addons_github_data`
        FOREIGN KEY (`github_data_id`) REFERENCES `github_data` (`id`)
);

DROP TABLE IF EXISTS `addons_tags`;
CREATE TABLE IF NOT EXISTS `addons_tags`
(
    `id`       INT PRIMARY KEY AUTO_INCREMENT,
    `addon_id` INT NOT NULL,
    `tag_id`   INT NOT NULL,
    CONSTRAINT `addons_tags_tags_pk`
        FOREIGN KEY (`tag_id`) REFERENCES `tags` (`id`),
    CONSTRAINT `addons_tags_addons_fk`
        FOREIGN KEY (`addon_id`) REFERENCES `addons` (`id`)
);


