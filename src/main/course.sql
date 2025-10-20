create database if not exists course;

use course;

CREATE TABLE IF NOT EXISTS courses (
                                       id INT AUTO_INCREMENT PRIMARY KEY,
                                       year VARCHAR(10),
                                       course_type TEXT,
                                       course_name VARCHAR(255),
                                       nature VARCHAR(50),
                                       credits DECIMAL(3,1),
                                       practice_credits DECIMAL(3,1),
                                       semester VARCHAR(50),
                                       school VARCHAR(255),
                                       indicators TEXT,
                                       notes TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

