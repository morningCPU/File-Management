package com.morning.v1.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CreateTable {

    public static void createTable(String url, String user, String password, String charset, String fileName) {
        try {
            // 提取年份
            String year = extractYear(fileName);
            if (year == null) {
                System.err.println("未从文件名中找到年份：" + fileName);
                return;
            }

            String tableName = "course_" + year;

            // 连接数据库
            try (Connection conn = DriverManager.getConnection(url, user, password);
                 Statement stmt = conn.createStatement()) {

                // 建表语句
                String sql = "CREATE TABLE IF NOT EXISTS " + tableName + " (" +
                        "id INT AUTO_INCREMENT PRIMARY KEY," +
                        "course_type TEXT," +
                        "course_name VARCHAR(255)," +
                        "nature VARCHAR(50)," +
                        "credits DECIMAL(3,1)," +
                        "practice_credits DECIMAL(3,1)," +
                        "semester VARCHAR(50)," +
                        "school VARCHAR(255)," +
                        "indicators TEXT," +
                        "notes TEXT" +
                        ") ENGINE=InnoDB DEFAULT CHARSET=" + charset + ";";

                stmt.executeUpdate(sql);
                System.out.println("1. 数据表已创建：" + tableName);
            }

        } catch (Exception e) {
            System.err.println("创建表失败：" + e.getMessage());
        }
    }

    // 提取文件名中的年份（连续的4位数字）
    private static String extractYear(String fileName) {
        Pattern pattern = Pattern.compile("(\\d{4})");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
