-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema gachicoding
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `gachicoding`;

-- -----------------------------------------------------
-- Schema gachicoding
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `gachicoding` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `gachicoding`;

-- -----------------------------------------------------
-- Table `gachicoding`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gachicoding`.`user`;

CREATE TABLE IF NOT EXISTS `gachicoding`.`user`
(
    `user_idx`        BIGINT       NOT NULL AUTO_INCREMENT,
    `user_created_at` DATETIME(6)  NOT NULL,
    `user_email`      VARCHAR(255) NOT NULL,
    `user_enabled`    BIT(1)       NOT NULL DEFAULT b'0',
    `user_name`       VARCHAR(255) NOT NULL,
    `user_nick`       VARCHAR(255) NOT NULL,
    `user_password`   VARCHAR(255) NOT NULL,
    `user_role`       VARCHAR(255) NOT NULL DEFAULT 'ROLE_USER',
    PRIMARY KEY (`user_idx`),
    UNIQUE INDEX `UK_j09k2v8lxofv2vecxu2hde9so` (`user_email` ASC) VISIBLE,
    UNIQUE INDEX `UK_23fkpdormb3jwywokgb1gvls5` (`user_nick` ASC) VISIBLE
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `gachicoding`.`board`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gachicoding`.`board`;

CREATE TABLE IF NOT EXISTS `gachicoding`.`board`
(
    `board_idx`      BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `created_at`     DATETIME     NOT NULL,
    `updated_at`     DATETIME     NOT NULL,
    `board_category` VARCHAR(20)  NULL     DEFAULT NULL,
    `board_contents` TEXT         NOT NULL,
    `board_locked`   BIT(1)       NOT NULL DEFAULT b'1',
    `board_title`    VARCHAR(255) NOT NULL,
    `board_views`    BIGINT       NOT NULL DEFAULT '0',
    `user_idx`       BIGINT       NULL     DEFAULT NULL,
    PRIMARY KEY (`board_idx`),
    INDEX `FK99mdf4t9hc7f662omr9279bbk` (`user_idx` ASC) VISIBLE,
    CONSTRAINT `FK99mdf4t9hc7f662omr9279bbk`
        FOREIGN KEY (`user_idx`)
            REFERENCES `gachicoding`.`user` (`user_idx`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `gachicoding`.`comment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gachicoding`.`comment`;

CREATE TABLE IF NOT EXISTS `gachicoding`.`comment`
(
    `comm_idx`         BIGINT       NOT NULL AUTO_INCREMENT,
    `article_category` VARCHAR(255) NULL DEFAULT NULL,
    `article_idx`      BIGINT       NULL DEFAULT NULL,
    `comm_activated`   BIT(1)       NULL DEFAULT NULL,
    `comm_contents`    VARCHAR(255) NULL DEFAULT NULL,
    `comm_regdate`     DATETIME(6)  NULL DEFAULT NULL,
    `user_idx`         BIGINT       NULL DEFAULT NULL,
    PRIMARY KEY (`comm_idx`),
    INDEX `FK2w536tvxybupg9ib08x4twpn5` (`user_idx` ASC) VISIBLE,
    CONSTRAINT `FK2w536tvxybupg9ib08x4twpn5`
        FOREIGN KEY (`user_idx`)
            REFERENCES `gachicoding`.`user` (`user_idx`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `gachicoding`.`email_confirm_token`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gachicoding`.`email_confirm_token`;

CREATE TABLE IF NOT EXISTS `gachicoding`.`email_confirm_token`
(
    `token_id`     BINARY(16)   NOT NULL,
    `confirmed`    BIT(1)       NOT NULL DEFAULT b'0',
    `expired_at`   DATETIME(6)  NOT NULL,
    `target_email` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`token_id`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `gachicoding`.`file`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gachicoding`.`file`;

CREATE TABLE IF NOT EXISTS `gachicoding`.`file`
(
    `file_idx`         BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `created_at`       DATETIME     NOT NULL,
    `updated_at`       DATETIME     NOT NULL,
    `article_category` VARCHAR(255) NOT NULL COMMENT '게시물 카테고리(Board, Notice, Question, Answer)',
    `article_idx`      BIGINT       NOT NULL COMMENT '게시물 번호',
    `file_ext`         VARCHAR(20)  NOT NULL COMMENT '파일 확장자',
    `file_path`        TEXT         NOT NULL COMMENT '저장된 파일 url',
    `origin_filename`  VARCHAR(255) NOT NULL COMMENT '원본 파일 이름',
    `save_filename`    VARCHAR(255) NOT NULL COMMENT '저장 파일 이름',
    PRIMARY KEY (`file_idx`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `gachicoding`.`gachi_q`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gachicoding`.`gachi_q`;

CREATE TABLE IF NOT EXISTS `gachicoding`.`gachi_q`
(
    `qs_idx`       BIGINT       NOT NULL AUTO_INCREMENT,
    `qs_activated` BIT(1)       NULL DEFAULT NULL,
    `qs_category`  VARCHAR(255) NULL DEFAULT NULL,
    `qs_contents`  VARCHAR(255) NULL DEFAULT NULL,
    `qs_error`     VARCHAR(255) NULL DEFAULT NULL,
    `qs_regdate`   DATETIME(6)  NULL DEFAULT NULL,
    `qs_solve`     BIT(1)       NULL DEFAULT NULL,
    `qs_title`     VARCHAR(255) NULL DEFAULT NULL,
    `user_idx`     BIGINT       NULL DEFAULT NULL,
    PRIMARY KEY (`qs_idx`),
    INDEX `FKkvp9oygv68fun9q96371rmkvv` (`user_idx` ASC) VISIBLE,
    CONSTRAINT `FKkvp9oygv68fun9q96371rmkvv`
        FOREIGN KEY (`user_idx`)
            REFERENCES `gachicoding`.`user` (`user_idx`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `gachicoding`.`gachi_a`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gachicoding`.`gachi_a`;

CREATE TABLE IF NOT EXISTS `gachicoding`.`gachi_a`
(
    `as_idx`       BIGINT       NOT NULL AUTO_INCREMENT,
    `as_activated` BIT(1)       NULL DEFAULT NULL,
    `as_contents`  VARCHAR(255) NULL DEFAULT NULL,
    `as_regdate`   DATETIME(6)  NULL DEFAULT NULL,
    `as_select`    BIT(1)       NULL DEFAULT NULL,
    `user_idx`     BIGINT       NULL DEFAULT NULL,
    `qs_idx`       BIGINT       NULL DEFAULT NULL,
    PRIMARY KEY (`as_idx`),
    INDEX `FKhsebvdaa7jwyruqlws9blovij` (`user_idx` ASC) VISIBLE,
    INDEX `FKoh48besc8enyyiyye59o9klsa` (`qs_idx` ASC) VISIBLE,
    CONSTRAINT `FKhsebvdaa7jwyruqlws9blovij`
        FOREIGN KEY (`user_idx`)
            REFERENCES `gachicoding`.`user` (`user_idx`),
    CONSTRAINT `FKoh48besc8enyyiyye59o9klsa`
        FOREIGN KEY (`qs_idx`)
            REFERENCES `gachicoding`.`gachi_q` (`qs_idx`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `gachicoding`.`notice`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gachicoding`.`notice`;

CREATE TABLE IF NOT EXISTS `gachicoding`.`notice`
(
    `not_idx`      BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `created_at`   DATETIME     NOT NULL,
    `updated_at`   DATETIME     NOT NULL,
    `not_contents` TEXT         NOT NULL,
    `not_locked`   BIT(1)       NOT NULL DEFAULT b'1',
    `not_pin`      BIT(1)       NOT NULL DEFAULT b'0',
    `not_title`    VARCHAR(255) NOT NULL,
    `not_views`    BIGINT       NOT NULL DEFAULT '0',
    `user_idx`     BIGINT       NULL     DEFAULT NULL,
    PRIMARY KEY (`not_idx`),
    INDEX `FKglwmb6rx3wn958keo38r4hah4` (`user_idx` ASC) VISIBLE,
    CONSTRAINT `FKglwmb6rx3wn958keo38r4hah4`
        FOREIGN KEY (`user_idx`)
            REFERENCES `gachicoding`.`user` (`user_idx`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `gachicoding`.`tag`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gachicoding`.`tag`;

CREATE TABLE IF NOT EXISTS `gachicoding`.`tag`
(
    `tag_idx`     BIGINT       NOT NULL AUTO_INCREMENT,
    `tag_keyword` VARCHAR(255) NULL DEFAULT NULL,
    PRIMARY KEY (`tag_idx`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `gachicoding`.`tag_rel`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `gachicoding`.`tag_rel`;

CREATE TABLE IF NOT EXISTS `gachicoding`.`tag_rel`
(
    `rel_idx`          BIGINT       NOT NULL AUTO_INCREMENT,
    `article_category` VARCHAR(255) NULL DEFAULT NULL,
    `article_idx`      BIGINT       NULL DEFAULT NULL,
    `tag_idx`          BIGINT       NULL DEFAULT NULL,
    PRIMARY KEY (`rel_idx`),
    INDEX `FKc0oynglomybrghped4hfan931` (`tag_idx` ASC) VISIBLE,
    CONSTRAINT `FKc0oynglomybrghped4hfan931`
        FOREIGN KEY (`tag_idx`)
            REFERENCES `gachicoding`.`tag` (`tag_idx`)
)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;
