create database if not exists course;

use course;

CREATE TABLE IF NOT EXISTS courses
(
    id               INT AUTO_INCREMENT PRIMARY KEY,
    year             VARCHAR(10),
    course_type      TEXT,
    course_name      VARCHAR(255),
    nature           VARCHAR(50),
    credits          DECIMAL(3, 1),
    practice_credits DECIMAL(3, 1),
    semester         VARCHAR(50),
    school           VARCHAR(255),
    indicators       TEXT,
    notes            TEXT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS teachers
(
    semester       VARCHAR(100),
    select_code    VARCHAR(100),
    course_code    VARCHAR(100),
    course_name    VARCHAR(100),
    teaching_class VARCHAR(100),
    credits        TINYINT,
    nature         VARCHAR(100),
    department     VARCHAR(100),
    teacher        VARCHAR(100),
    title          VARCHAR(100),
    time_location  VARCHAR(100),
    preferred      TEXT,
    status         VARCHAR(100),
    campus         VARCHAR(100),
    primary key (semester, select_code)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

