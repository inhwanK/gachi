-- gachicoding
DROP SCHEMA IF EXISTS `gachicoding`;

-- gachicoding
CREATE SCHEMA `gachicoding`;

-- 유저
DROP TABLE IF EXISTS `gachicoding`.`user` RESTRICT;

-- 인증
DROP TABLE IF EXISTS `gachicoding`.`auth` RESTRICT;

-- 소셜
DROP TABLE IF EXISTS `gachicoding`.`social` RESTRICT;

-- 공지사항
DROP TABLE IF EXISTS `gachicoding`.`notice` RESTRICT;

-- 태그
DROP TABLE IF EXISTS `gachicoding`.`tag` RESTRICT;

-- 공지태그
DROP TABLE IF EXISTS `gachicoding`.`not_tag` RESTRICT;

-- 댓글
DROP TABLE IF EXISTS `gachicoding`.`comment` RESTRICT;

-- 가치고민(질문)
DROP TABLE IF EXISTS `gachicoding`.`gachiQ` RESTRICT;

-- 가치해결(답변)
DROP TABLE IF EXISTS `gachicoding`.`gachiA` RESTRICT;

-- 고민태그
DROP TABLE IF EXISTS `gachicoding`.`q_tag` RESTRICT;

-- 가치트렌드
DROP TABLE IF EXISTS `gachicoding`.`gachi_trend` RESTRICT;

-- 아고라
DROP TABLE IF EXISTS `gachicoding`.`gachi_agora` RESTRICT;

-- 투표현황
DROP TABLE IF EXISTS `gachicoding`.`agora_vote` RESTRICT;

-- 프로필
DROP TABLE IF EXISTS `gachicoding`.`profile` RESTRICT;

-- 수상내역
DROP TABLE IF EXISTS `gachicoding`.`award` RESTRICT;

-- 자격내역
DROP TABLE IF EXISTS `gachicoding`.`certificate` RESTRICT;

-- 학력
DROP TABLE IF EXISTS `gachicoding`.`degree` RESTRICT;

-- 포트폴리오
DROP TABLE IF EXISTS `gachicoding`.`portfolio` RESTRICT;

-- 기술
DROP TABLE IF EXISTS `gachicoding`.`skill` RESTRICT;

-- 프로필기술
DROP TABLE IF EXISTS `gachicoding`.`profile_skills` RESTRICT;

-- 파일
DROP TABLE IF EXISTS `gachicoding`.`file` RESTRICT;

-- 자유게시판
DROP TABLE IF EXISTS `gachicoding`.`board` RESTRICT;

-- 유저
CREATE TABLE `gachicoding`.`user`
(
    `user_idx`       BIGINT(20) UNSIGNED NOT NULL COMMENT '유저번호',               -- 유저번호
    `user_name`      VARCHAR(255)        NOT NULL COMMENT '유저이름',               -- 유저이름
    `user_nick`      VARCHAR(255)        NOT NULL COMMENT '유저별명',               -- 유저별명
    `user_email`     VARCHAR(255)        NOT NULL COMMENT '이메일',                -- 이메일
    `user_password`  VARCHAR(255)        NOT NULL COMMENT '비밀번호',               -- 비밀번호
    `user_regdate`   DATETIME            NOT NULL DEFAULT now() COMMENT '생성일자', -- 생성일자
    `user_activated` BOOLEAN             NOT NULL DEFAULT true COMMENT '활성상태',  -- 활성상태
    `user_role`      VARCHAR(15)         NOT NULL DEFAULT 'GUEST' COMMENT '권한', -- 권한
    `user_auth`      BOOLEAN             NOT NULL DEFAULT false COMMENT '인증여부', -- 인증여부
    `user_picture`   TEXT                NOT NULL COMMENT '사진'                  -- 사진
)
    COMMENT '유저';

-- 유저
ALTER TABLE `gachicoding`.`user`
    ADD CONSTRAINT `PK_user` -- 유저 기본키
        PRIMARY KEY (
                     `user_idx` -- 유저번호
            );

-- 유저 유니크 인덱스
CREATE UNIQUE INDEX `UIX_user`
    ON `gachicoding`.`user` ( -- 유저
                             `user_nick` ASC, -- 유저별명
                             `user_email` ASC -- 이메일
        );

ALTER TABLE `gachicoding`.`user`
    MODIFY COLUMN `user_idx` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '유저번호';

-- 인증
CREATE TABLE `gachicoding`.`auth`
(
    `auth_token`   VARCHAR(37)  NOT NULL COMMENT '토큰',                 -- 토큰
    `auth_email`   VARCHAR(255) NOT NULL COMMENT '이메일',                -- 이메일
    `auth_regdate` DATETIME     NOT NULL DEFAULT now() COMMENT '생성일시', -- 생성일시
    `auth_expdate` DATETIME     NOT NULL COMMENT '만료일시'                -- 만료일시
)
    COMMENT '인증';

-- 인증
ALTER TABLE `gachicoding`.`auth`
    ADD CONSTRAINT `PK_auth` -- 인증 기본키
        PRIMARY KEY (
                     `auth_token` -- 토큰
            );

-- 소셜
CREATE TABLE `gachicoding`.`social`
(
    `social_idx`  BIGINT(21) UNSIGNED NOT NULL COMMENT '소셜번호',  -- 소셜번호
    `user_idx`    BIGINT(20) UNSIGNED NOT NULL COMMENT '유저번호',  -- 유저번호
    `social_type` VARCHAR(20)         NOT NULL COMMENT '소셜유형',  -- 소셜유형
    `social_id`   VARCHAR(255)        NOT NULL COMMENT '소셜아이디', -- 소셜아이디
    `social_date` DATETIME            NOT NULL COMMENT '인증일시'   -- 인증일시
)
    COMMENT '소셜';

-- 소셜
ALTER TABLE `gachicoding`.`social`
    ADD CONSTRAINT `PK_social` -- 소셜 기본키
        PRIMARY KEY (
                     `social_idx` -- 소셜번호
            );

ALTER TABLE `gachicoding`.`social`
    MODIFY COLUMN `social_idx` BIGINT(21) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '소셜번호';

-- 공지사항
CREATE TABLE `gachicoding`.`notice`
(
    `not_idx`       BIGINT(21) UNSIGNED NOT NULL COMMENT '공지사항번호',            -- 공지사항번호
    `user_idx`      BIGINT(20) UNSIGNED NOT NULL COMMENT '작성자번호',             -- 작성자번호
    `not_title`     VARCHAR(255)        NOT NULL COMMENT '제목',                -- 제목
    `not_content`   TEXT                NOT NULL COMMENT '본문',                -- 본문
    `not_views`     INT(20) UNSIGNED    NOT NULL COMMENT '조회수',               -- 조회수
    `not_pin`       BOOLEAN             NOT NULL COMMENT '고정',                -- 고정
    `not_regdate`   DATETIME            NOT NULL DEFAULT now() COMMENT '작성일', -- 작성일
    `not_activated` BOOLEAN             NOT NULL DEFAULT true COMMENT '활성상태'  -- 활성상태
)
    COMMENT '공지사항';

-- 공지사항
ALTER TABLE `gachicoding`.`notice`
    ADD CONSTRAINT `PK_notice` -- 공지사항 기본키
        PRIMARY KEY (
                     `not_idx` -- 공지사항번호
            );

ALTER TABLE `gachicoding`.`notice`
    MODIFY COLUMN `not_idx` BIGINT(21) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '공지사항번호';

-- 태그
CREATE TABLE `gachicoding`.`tag`
(
    `tag_idx`     BIGINT(21) UNSIGNED NOT NULL COMMENT '태그번호', -- 태그번호
    `tag_keyword` VARCHAR(255)        NOT NULL COMMENT '태그키워드' -- 태그키워드
)
    COMMENT '태그';

-- 태그
ALTER TABLE `gachicoding`.`tag`
    ADD CONSTRAINT `PK_tag` -- 태그 기본키
        PRIMARY KEY (
                     `tag_idx` -- 태그번호
            );

-- 공지태그
CREATE TABLE `gachicoding`.`not_tag`
(
    `not_idx` BIGINT(21) UNSIGNED NOT NULL COMMENT '공지사항번호', -- 공지사항번호
    `tag_idx` BIGINT(21) UNSIGNED NOT NULL COMMENT '태그번호'    -- 태그번호
)
    COMMENT '공지태그';

-- 공지태그
ALTER TABLE `gachicoding`.`not_tag`
    ADD CONSTRAINT `PK_not_tag` -- 공지태그 기본키
        PRIMARY KEY (
                     `not_idx`, -- 공지사항번호
                     `tag_idx` -- 태그번호
            );

-- 댓글
CREATE TABLE `gachicoding`.`comment`
(
    `comm_idx`         BIGINT(22) UNSIGNED NOT NULL COMMENT '댓글번호',   -- 댓글번호
    `user_idx`         BIGINT(20) UNSIGNED NOT NULL COMMENT '작성자번호',  -- 작성자번호
    `parents_idx`      BIGINT(22) UNSIGNED NOT NULL COMMENT '상위댓글번호', -- 상위댓글번호
    `comm_content`     VARCHAR(9999)       NOT NULL COMMENT '댓글내용',   -- 댓글내용
    `comm_regdate`     DATETIME            NOT NULL COMMENT '작성일시',   -- 작성일시
    `comm_activate`    BOOLEAN             NOT NULL COMMENT '활성여부',   -- 활성여부
    `article_category` VARCHAR(255)        NOT NULL COMMENT '게시글분류',  -- 게시글분류
    `article_idx`      BIGINT(23) UNSIGNED NOT NULL COMMENT '글번호'     -- 글번호
)
    COMMENT '댓글';

-- 댓글
ALTER TABLE `gachicoding`.`comment`
    ADD CONSTRAINT `PK_comment` -- 댓글 기본키
        PRIMARY KEY (
                     `comm_idx` -- 댓글번호
            );

-- 가치고민(질문)
CREATE TABLE `gachicoding`.`gachiQ`
(
    `q_idx`      BIGINT(22) UNSIGNED NOT NULL COMMENT '가치고민번호', -- 가치고민번호
    `user_idx`   BIGINT(20) UNSIGNED NOT NULL COMMENT '작성자번호',  -- 작성자번호
    `q_title`    VARCHAR(255)        NOT NULL COMMENT '가치고민제목', -- 가치고민제목
    `q_content`  TEXT                NOT NULL COMMENT '가치고민내용', -- 가치고민내용
    `q_error`    TEXT                NULL COMMENT '가치고민에러',     -- 가치고민에러
    `q_category` VARCHAR(30)         NOT NULL COMMENT '카테고리',   -- 카테고리
    `q_solve`    BOOLEAN             NOT NULL COMMENT '해결여부',   -- 해결여부
    `q_regdate`  DATETIME            NOT NULL COMMENT '작성일',    -- 작성일
    `q_activate` BOOLEAN             NOT NULL COMMENT '활성여부'    -- 활성여부
)
    COMMENT '가치고민(질문)';

-- 가치고민(질문)
ALTER TABLE `gachicoding`.`gachiQ`
    ADD CONSTRAINT `PK_gachiQ` -- 가치고민(질문) 기본키
        PRIMARY KEY (
                     `q_idx` -- 가치고민번호
            );

-- 가치해결(답변)
CREATE TABLE `gachicoding`.`gachiA`
(
    `a_idx`      BIGINT(23) UNSIGNED NOT NULL COMMENT '가치해결번호', -- 가치해결번호
    `user_idx`   BIGINT(20) UNSIGNED NOT NULL COMMENT '작성자번호',  -- 작성자번호
    `q_idx`      BIGINT(22) UNSIGNED NOT NULL COMMENT '가치고민번호', -- 가치고민번호
    `a_content`  TEXT                NOT NULL COMMENT '가치해결내용', -- 가치해결내용
    `a_select`   BOOLEAN             NOT NULL COMMENT '채택여부',   -- 채택여부
    `a_regdate`  DATETIME            NOT NULL COMMENT '작성일',    -- 작성일
    `a_activate` BOOLEAN             NOT NULL COMMENT '활성여부'    -- 활성여부
)
    COMMENT '가치해결(답변)';

-- 가치해결(답변)
ALTER TABLE `gachicoding`.`gachiA`
    ADD CONSTRAINT `PK_gachiA` -- 가치해결(답변) 기본키
        PRIMARY KEY (
                     `a_idx` -- 가치해결번호
            );

-- 고민태그
CREATE TABLE `gachicoding`.`q_tag`
(
    `q_idx`   BIGINT(22) UNSIGNED NOT NULL COMMENT '가치고민번호', -- 가치고민번호
    `tag_idx` BIGINT(21) UNSIGNED NOT NULL COMMENT '태그번호'    -- 태그번호
)
    COMMENT '고민태그';

-- 고민태그
ALTER TABLE `gachicoding`.`q_tag`
    ADD CONSTRAINT `PK_q_tag` -- 고민태그 기본키
        PRIMARY KEY (
                     `q_idx`, -- 가치고민번호
                     `tag_idx` -- 태그번호
            );

-- 가치트렌드
CREATE TABLE `gachicoding`.`gachi_trend`
(
    `news_idx`       BIGINT(20) UNSIGNED NOT NULL COMMENT '뉴스번호',  -- 뉴스번호
    `user_idx`       BIGINT(20) UNSIGNED NOT NULL COMMENT '작성자번호', -- 작성자번호
    `news_title`     VARCHAR(255)        NOT NULL COMMENT '뉴스제목',  -- 뉴스제목
    `news_content`   TEXT                NOT NULL COMMENT '뉴스내용',  -- 뉴스내용
    `news_ref`       TEXT                NULL COMMENT '출처',        -- 출처
    `news_regdate`   DATETIME            NOT NULL COMMENT '작성일',   -- 작성일
    `news_url`       VARCHAR(255)        NULL COMMENT '링크',        -- 링크
    `news_pin`       BOOLEAN             NOT NULL COMMENT '고정',    -- 고정
    `news_thumbnail` VARCHAR(255)        NOT NULL COMMENT '썸네일'    -- 썸네일
)
    COMMENT '가치트렌드';

-- 가치트렌드
ALTER TABLE `gachicoding`.`gachi_trend`
    ADD CONSTRAINT `PK_gachi_trend` -- 가치트렌드 기본키
        PRIMARY KEY (
                     `news_idx` -- 뉴스번호
            );

-- 아고라
CREATE TABLE `gachicoding`.`gachi_agora`
(
    `agora_idx`       BIGINT(23) UNSIGNED NOT NULL COMMENT '아고라번호', -- 아고라번호
    `user_idx`        BIGINT(20) UNSIGNED NULL COMMENT '작성자번호',     -- 작성자번호
    `agora_title`     VARCHAR(255)        NOT NULL COMMENT '제목',    -- 제목
    `agora_content`   TEXT                NOT NULL COMMENT '본문',    -- 본문
    `agora_thumbnail` VARCHAR(255)        NOT NULL COMMENT '썸네일',   -- 썸네일
    `agora_startdate` DATETIME            NOT NULL COMMENT '시작일',   -- 시작일
    `agora_enddate`   DATETIME            NOT NULL COMMENT '종료일',   -- 종료일
    `agora_regdate`   DATETIME            NOT NULL COMMENT '작성일',   -- 작성일
    `agora_views`     INT(20) UNSIGNED    NOT NULL COMMENT '조회수'    -- 조회수
)
    COMMENT '아고라';

-- 아고라
ALTER TABLE `gachicoding`.`gachi_agora`
    ADD CONSTRAINT `PK_gachi_agora` -- 아고라 기본키
        PRIMARY KEY (
                     `agora_idx` -- 아고라번호
            );

-- 투표현황
CREATE TABLE `gachicoding`.`agora_vote`
(
    `vote_idx`   BIGINT(23) UNSIGNED NOT NULL COMMENT '투표번호',  -- 투표번호
    `agora_idx`  BIGINT(23) UNSIGNED NOT NULL COMMENT '아고라번호', -- 아고라번호
    `user_idx`   BIGINT(20) UNSIGNED NOT NULL COMMENT '투표자번호', -- 투표자번호
    `vote_agree` BOOLEAN             NOT NULL COMMENT '찬반'     -- 찬반
)
    COMMENT '투표현황';

-- 투표현황
ALTER TABLE `gachicoding`.`agora_vote`
    ADD CONSTRAINT `PK_agora_vote` -- 투표현황 기본키
        PRIMARY KEY (
                     `vote_idx` -- 투표번호
            );

-- 프로필
CREATE TABLE `gachicoding`.`profile`
(
    `user_idx`          BIGINT(20) UNSIGNED NOT NULL COMMENT '유저번호',   -- 유저번호
    `profile_name`      VARCHAR(255)        NOT NULL COMMENT '이름',     -- 이름
    `profile_picture`   TEXT                NOT NULL COMMENT '사진',     -- 사진
    `profile_position`  VARCHAR(255)        NOT NULL COMMENT '간단직무',   -- 간단직무
    `profile_career`    VARCHAR(255)        NOT NULL COMMENT '간단경력',   -- 간단경력
    `profile_introduce` TEXT                NOT NULL COMMENT '한줄소개',   -- 한줄소개
    `profile_contime`   VARCHAR(255)        NOT NULL COMMENT '연락가능시간', -- 연락가능시간
    `profile_region`    VARCHAR(255)        NOT NULL COMMENT '지역',     -- 지역
    `profile_award`     VARCHAR(255)        NOT NULL COMMENT '수상내역'    -- 수상내역
)
    COMMENT '프로필';

-- 프로필
ALTER TABLE `gachicoding`.`profile`
    ADD CONSTRAINT `PK_profile` -- 프로필 기본키
        PRIMARY KEY (
                     `user_idx` -- 유저번호
            );

-- 수상내역
CREATE TABLE `gachicoding`.`award`
(
    `award_idx`   BIGINT(23) UNSIGNED NOT NULL COMMENT '수상번호', -- 수상번호
    `user_idx`    BIGINT(20) UNSIGNED NOT NULL COMMENT '유저번호', -- 유저번호
    `award_title` VARCHAR(255)        NOT NULL COMMENT '대회명',  -- 대회명
    `award_rank`  VARCHAR(255)        NOT NULL COMMENT '입상내역', -- 입상내역
    `award_date`  DATE                NOT NULL COMMENT '수상날짜', -- 수상날짜
    `award_place` VARCHAR(255)        NOT NULL COMMENT '주최기관'  -- 주최기관
)
    COMMENT '수상내역';

-- 수상내역
ALTER TABLE `gachicoding`.`award`
    ADD CONSTRAINT `PK_award` -- 수상내역 기본키
        PRIMARY KEY (
                     `award_idx` -- 수상번호
            );

-- 자격내역
CREATE TABLE `gachicoding`.`certificate`
(
    `certificate_idx`   BIGINT(23) UNSIGNED NOT NULL COMMENT '자격번호',  -- 자격번호
    `user_idx`          BIGINT(20) UNSIGNED NOT NULL COMMENT '유저번호',  -- 유저번호
    `certificate_title` VARCHAR(255)        NOT NULL COMMENT '자격증명',  -- 자격증명
    `certificate_rank`  VARCHAR(255)        NOT NULL COMMENT '자격증등급', -- 자격증등급
    `certificate_date`  DATE                NOT NULL COMMENT '발급날짜',  -- 발급날짜
    `certificate_place` VARCHAR(255)        NOT NULL COMMENT '발급기관'   -- 발급기관
)
    COMMENT '자격내역';

-- 자격내역
ALTER TABLE `gachicoding`.`certificate`
    ADD CONSTRAINT `PK_certificate` -- 자격내역 기본키
        PRIMARY KEY (
                     `certificate_idx` -- 자격번호
            );

-- 학력
CREATE TABLE `gachicoding`.`degree`
(
    `degree_idx`          BIGINT(23) UNSIGNED NOT NULL COMMENT '학력번호', -- 학력번호
    `user_idx`            BIGINT(20) UNSIGNED NOT NULL COMMENT '유저번호', -- 유저번호
    `degree_state`        VARCHAR(255)        NOT NULL COMMENT '학력구분', -- 학력구분
    `degree_title`        VARCHAR(225)        NOT NULL COMMENT '학교명',  -- 학교명
    `degree_major`        VARCHAR(255)        NOT NULL COMMENT '전공계열', -- 전공계열
    `degree_major_detail` VARCHAR(255)        NOT NULL COMMENT '전공',   -- 전공
    `degree_enter_date`   DATE                NOT NULL COMMENT '입학년',  -- 입학년
    `degree_gradu_date`   DATE                NOT NULL COMMENT '졸업년',  -- 졸업년
    `degree_gradu_state`  VARCHAR(255)        NOT NULL COMMENT '졸업구분'  -- 졸업구분
)
    COMMENT '학력';

-- 학력
ALTER TABLE `gachicoding`.`degree`
    ADD CONSTRAINT `PK_degree` -- 학력 기본키
        PRIMARY KEY (
                     `degree_idx` -- 학력번호
            );

-- 포트폴리오
CREATE TABLE `gachicoding`.`portfolio`
(
    `port_idx`      BIGINT(23) UNSIGNED NOT NULL COMMENT '포트폴리오번호', -- 포트폴리오번호
    `user_idx`      BIGINT(20) UNSIGNED NOT NULL COMMENT '유저번호',    -- 유저번호
    `port_link`     VARCHAR(255)        NULL COMMENT '포트폴리오링크',     -- 포트폴리오링크
    `port_filepath` VARCHAR(255)        NULL COMMENT '파일경로',        -- 파일경로
    `port_comment`  TEXT                NULL COMMENT '포트폴리오설명'      -- 포트폴리오설명
)
    COMMENT '포트폴리오';

-- 포트폴리오
ALTER TABLE `gachicoding`.`portfolio`
    ADD CONSTRAINT `PK_portfolio` -- 포트폴리오 기본키
        PRIMARY KEY (
                     `port_idx` -- 포트폴리오번호
            );

-- 기술
CREATE TABLE `gachicoding`.`skill`
(
    `skill_idx`  BIGINT(23) UNSIGNED NOT NULL COMMENT '기술번호', -- 기술번호
    `skill_name` VARCHAR(255)        NOT NULL COMMENT '기술명'   -- 기술명
)
    COMMENT '기술';

-- 기술
ALTER TABLE `gachicoding`.`skill`
    ADD CONSTRAINT `PK_skill` -- 기술 기본키
        PRIMARY KEY (
                     `skill_idx` -- 기술번호
            );

-- 프로필기술
CREATE TABLE `gachicoding`.`profile_skills`
(
    `user_idx`  BIGINT(20) UNSIGNED NOT NULL COMMENT '유저번호', -- 유저번호
    `skill_idx` BIGINT(23) UNSIGNED NOT NULL COMMENT '기술번호'  -- 기술번호
)
    COMMENT '프로필기술';

-- 프로필기술
ALTER TABLE `gachicoding`.`profile_skills`
    ADD CONSTRAINT `PK_profile_skills` -- 프로필기술 기본키
        PRIMARY KEY (
                     `user_idx`, -- 유저번호
                     `skill_idx` -- 기술번호
            );

-- 파일
CREATE TABLE `gachicoding`.`file`
(
    `file_idx`       BIGINT(21)   NOT NULL COMMENT '파일번호',              -- 파일번호
    `board_idx`      BIGINT(21)   NOT NULL COMMENT '게시글번호',             -- 게시글번호
    `board_category` VARCHAR(20)  NOT NULL COMMENT '게시판카테고리',           -- 게시판카테고리
    `orig_filename`  VARCHAR(255) NOT NULL COMMENT '원본파일이름',            -- 원본파일이름
    `save_filename`  VARCHAR(255) NOT NULL COMMENT '저장파일이름',            -- 저장파일이름
    `file_size`      INT          NOT NULL COMMENT '파일크기',              -- 파일크기
    `file_ext`       VARCHAR(20)  NOT NULL COMMENT '파일확장자',             -- 파일확장자
    `file_path`      VARCHAR(255) NOT NULL COMMENT '파일경로',              -- 파일경로
    `file_activated` BOOLEAN      NOT NULL DEFAULT TRUE COMMENT '활성상태', -- 활성상태
    `file_regdate`   DATETIME     NOT NULL DEFAULT NOW() COMMENT '생성일자' -- 생성일자
)
    COMMENT '파일';

-- 파일
ALTER TABLE `gachicoding`.`file`
    ADD CONSTRAINT `PK_file` -- 파일 기본키
        PRIMARY KEY (
                     `file_idx` -- 파일번호
            );

-- 자유게시판
CREATE TABLE `gachicoding`.`board`
(
    `board_idx`       BIGINT(21) UNSIGNED NOT NULL COMMENT '자유게시판번호',           -- 자유게시판번호
    `user_idx`        BIGINT(20) UNSIGNED NULL COMMENT '작성자번호',                 -- 작성자번호
    `board_title`     VARCHAR(255)        NOT NULL COMMENT '제목',                -- 제목
    `board_content`   TEXT                NOT NULL COMMENT '본문',                -- 본문
    `board_views`     INT(20) UNSIGNED    NOT NULL DEFAULT 0 COMMENT '조회수',     -- 조회수
    `board_pin`       BOOLEAN             NOT NULL DEFAULT false COMMENT '고정',  -- 고정
    `board_regdate`   DATETIME            NOT NULL DEFAULT now() COMMENT '작성일', -- 작성일
    `board_activated` BOOLEAN             NOT NULL DEFAULT true COMMENT '활성상태'  -- 활성상태
)
    COMMENT '자유게시판';

-- 자유게시판
ALTER TABLE `gachicoding`.`board`
    ADD CONSTRAINT `PK_board` -- 자유게시판 기본키
        PRIMARY KEY (
                     `board_idx` -- 자유게시판번호
            );

ALTER TABLE `gachicoding`.`board`
    MODIFY COLUMN `board_idx` BIGINT(21) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '자유게시판번호';

-- 소셜
ALTER TABLE `gachicoding`.`social`
    ADD CONSTRAINT `FK_user_TO_social` -- 유저 -> 소셜
        FOREIGN KEY (
                     `user_idx` -- 유저번호
            )
            REFERENCES `gachicoding`.`user` ( -- 유저
                                             `user_idx` -- 유저번호
                );

-- 공지사항
ALTER TABLE `gachicoding`.`notice`
    ADD CONSTRAINT `FK_user_TO_notice` -- 유저 -> 공지사항
        FOREIGN KEY (
                     `user_idx` -- 작성자번호
            )
            REFERENCES `gachicoding`.`user` ( -- 유저
                                             `user_idx` -- 유저번호
                );

-- 공지태그
ALTER TABLE `gachicoding`.`not_tag`
    ADD CONSTRAINT `FK_notice_TO_not_tag` -- 공지사항 -> 공지태그
        FOREIGN KEY (
                     `not_idx` -- 공지사항번호
            )
            REFERENCES `gachicoding`.`notice` ( -- 공지사항
                                               `not_idx` -- 공지사항번호
                );

-- 공지태그
ALTER TABLE `gachicoding`.`not_tag`
    ADD CONSTRAINT `FK_tag_TO_not_tag` -- 태그 -> 공지태그
        FOREIGN KEY (
                     `tag_idx` -- 태그번호
            )
            REFERENCES `gachicoding`.`tag` ( -- 태그
                                            `tag_idx` -- 태그번호
                );

-- 댓글
ALTER TABLE `gachicoding`.`comment`
    ADD CONSTRAINT `FK_comment_TO_comment` -- 댓글 -> 댓글
        FOREIGN KEY (
                     `parents_idx` -- 상위댓글번호
            )
            REFERENCES `gachicoding`.`comment` ( -- 댓글
                                                `comm_idx` -- 댓글번호
                );

-- 댓글
ALTER TABLE `gachicoding`.`comment`
    ADD CONSTRAINT `FK_user_TO_comment` -- 유저 -> 댓글
        FOREIGN KEY (
                     `user_idx` -- 작성자번호
            )
            REFERENCES `gachicoding`.`user` ( -- 유저
                                             `user_idx` -- 유저번호
                );

-- 가치고민(질문)
ALTER TABLE `gachicoding`.`gachiQ`
    ADD CONSTRAINT `FK_user_TO_gachiQ` -- 유저 -> 가치고민(질문)
        FOREIGN KEY (
                     `user_idx` -- 작성자번호
            )
            REFERENCES `gachicoding`.`user` ( -- 유저
                                             `user_idx` -- 유저번호
                );

-- 가치해결(답변)
ALTER TABLE `gachicoding`.`gachiA`
    ADD CONSTRAINT `FK_user_TO_gachiA` -- 유저 -> 가치해결(답변)
        FOREIGN KEY (
                     `user_idx` -- 작성자번호
            )
            REFERENCES `gachicoding`.`user` ( -- 유저
                                             `user_idx` -- 유저번호
                );

-- 가치해결(답변)
ALTER TABLE `gachicoding`.`gachiA`
    ADD CONSTRAINT `FK_gachiQ_TO_gachiA` -- 가치고민(질문) -> 가치해결(답변)
        FOREIGN KEY (
                     `q_idx` -- 가치고민번호
            )
            REFERENCES `gachicoding`.`gachiQ` ( -- 가치고민(질문)
                                               `q_idx` -- 가치고민번호
                );

-- 고민태그
ALTER TABLE `gachicoding`.`q_tag`
    ADD CONSTRAINT `FK_gachiQ_TO_q_tag` -- 가치고민(질문) -> 고민태그
        FOREIGN KEY (
                     `q_idx` -- 가치고민번호
            )
            REFERENCES `gachicoding`.`gachiQ` ( -- 가치고민(질문)
                                               `q_idx` -- 가치고민번호
                );

-- 고민태그
ALTER TABLE `gachicoding`.`q_tag`
    ADD CONSTRAINT `FK_tag_TO_q_tag` -- 태그 -> 고민태그
        FOREIGN KEY (
                     `tag_idx` -- 태그번호
            )
            REFERENCES `gachicoding`.`tag` ( -- 태그
                                            `tag_idx` -- 태그번호
                );

-- 가치트렌드
ALTER TABLE `gachicoding`.`gachi_trend`
    ADD CONSTRAINT `FK_user_TO_gachi_trend` -- 유저 -> 가치트렌드
        FOREIGN KEY (
                     `user_idx` -- 작성자번호
            )
            REFERENCES `gachicoding`.`user` ( -- 유저
                                             `user_idx` -- 유저번호
                );

-- 아고라
ALTER TABLE `gachicoding`.`gachi_agora`
    ADD CONSTRAINT `FK_user_TO_gachi_agora` -- 유저 -> 아고라
        FOREIGN KEY (
                     `user_idx` -- 작성자번호
            )
            REFERENCES `gachicoding`.`user` ( -- 유저
                                             `user_idx` -- 유저번호
                );

-- 투표현황
ALTER TABLE `gachicoding`.`agora_vote`
    ADD CONSTRAINT `FK_gachi_agora_TO_agora_vote` -- 아고라 -> 투표현황
        FOREIGN KEY (
                     `agora_idx` -- 아고라번호
            )
            REFERENCES `gachicoding`.`gachi_agora` ( -- 아고라
                                                    `agora_idx` -- 아고라번호
                );

-- 투표현황
ALTER TABLE `gachicoding`.`agora_vote`
    ADD CONSTRAINT `FK_user_TO_agora_vote` -- 유저 -> 투표현황
        FOREIGN KEY (
                     `user_idx` -- 투표자번호
            )
            REFERENCES `gachicoding`.`user` ( -- 유저
                                             `user_idx` -- 유저번호
                );

-- 프로필
ALTER TABLE `gachicoding`.`profile`
    ADD CONSTRAINT `FK_user_TO_profile` -- 유저 -> 프로필
        FOREIGN KEY (
                     `user_idx` -- 유저번호
            )
            REFERENCES `gachicoding`.`user` ( -- 유저
                                             `user_idx` -- 유저번호
                );

-- 수상내역
ALTER TABLE `gachicoding`.`award`
    ADD CONSTRAINT `FK_profile_TO_award` -- 프로필 -> 수상내역
        FOREIGN KEY (
                     `user_idx` -- 유저번호
            )
            REFERENCES `gachicoding`.`profile` ( -- 프로필
                                                `user_idx` -- 유저번호
                );

-- 자격내역
ALTER TABLE `gachicoding`.`certificate`
    ADD CONSTRAINT `FK_profile_TO_certificate` -- 프로필 -> 자격내역
        FOREIGN KEY (
                     `user_idx` -- 유저번호
            )
            REFERENCES `gachicoding`.`profile` ( -- 프로필
                                                `user_idx` -- 유저번호
                );

-- 학력
ALTER TABLE `gachicoding`.`degree`
    ADD CONSTRAINT `FK_profile_TO_degree` -- 프로필 -> 학력
        FOREIGN KEY (
                     `user_idx` -- 유저번호
            )
            REFERENCES `gachicoding`.`profile` ( -- 프로필
                                                `user_idx` -- 유저번호
                );

-- 포트폴리오
ALTER TABLE `gachicoding`.`portfolio`
    ADD CONSTRAINT `FK_profile_TO_portfolio` -- 프로필 -> 포트폴리오
        FOREIGN KEY (
                     `user_idx` -- 유저번호
            )
            REFERENCES `gachicoding`.`profile` ( -- 프로필
                                                `user_idx` -- 유저번호
                );

-- 프로필기술
ALTER TABLE `gachicoding`.`profile_skills`
    ADD CONSTRAINT `FK_skill_TO_profile_skills` -- 기술 -> 프로필기술
        FOREIGN KEY (
                     `skill_idx` -- 기술번호
            )
            REFERENCES `gachicoding`.`skill` ( -- 기술
                                              `skill_idx` -- 기술번호
                );

-- 프로필기술
ALTER TABLE `gachicoding`.`profile_skills`
    ADD CONSTRAINT `FK_profile_TO_profile_skills` -- 프로필 -> 프로필기술
        FOREIGN KEY (
                     `user_idx` -- 유저번호
            )
            REFERENCES `gachicoding`.`profile` ( -- 프로필
                                                `user_idx` -- 유저번호
                );

-- 자유게시판
ALTER TABLE `gachicoding`.`board`
    ADD CONSTRAINT `FK_user_TO_board` -- 유저 -> 자유게시판
        FOREIGN KEY (
                     `user_idx` -- 작성자번호
            )
            REFERENCES `gachicoding`.`user` ( -- 유저
                                             `user_idx` -- 유저번호
                );