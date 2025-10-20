package com.morning.v1.database;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CsvToMySQL {
    public static void loadCsvToMySQL(String inputPath, String url, String user, String password, String charset) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            // 文件编码和数据库字符集分开
            String fileCharset = charset.equalsIgnoreCase("utf8mb4") ? "UTF-8" : charset;

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(inputPath), Charset.forName(fileCharset)))
            ) {
                // 提取表名
                String tableName = extractTableName(inputPath);
                if (tableName == null) {
                    System.err.println("无法从文件名中提取年份：" + inputPath);
                    return;
                }

                // 连接数据库
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(url, user, password);
                conn.setAutoCommit(false);

                // SQL
                String sql = "INSERT INTO " + tableName + " " +
                        "(course_type, course_name, nature, credits, practice_credits, semester, school, indicators, notes) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                pstmt = conn.prepareStatement(sql);

                // 导入
                String line;
                int count = 0;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] cols = line.split(",", -1);
                    if (cols.length < 9) continue;

                    pstmt.setString(1, cols[0]);
                    pstmt.setString(2, cols[1]);
                    pstmt.setString(3, cols[2]);
                    pstmt.setBigDecimal(4, parseDecimal(cols[3]));
                    pstmt.setBigDecimal(5, parseDecimal(cols[4]));
                    pstmt.setString(6, cols[5]);
                    pstmt.setString(7, cols[6]);

                    // 倒数第二列（合并第8列到倒数第2列）
                    StringBuilder sb = new StringBuilder();
                    for (int i = 7; i < cols.length - 1; i++) {
                        if (!sb.isEmpty()) sb.append(","); // 保留逗号
                        sb.append(cols[i]);
                    }
                    cols[7] = sb.toString();
                    pstmt.setString(8, cols[7]);

                    cols[8] = cols[cols.length - 1];
                    pstmt.setString(9, cols[8]);
                    pstmt.addBatch();
                    count++;
                }

                pstmt.executeBatch();
                conn.commit();
                System.out.println("6. CSV 数据成功导入表：" + tableName + "，共导入 " + count + " 条记录。");
            }

        } catch (Exception e) {
            System.err.println("导入失败：" + e.getMessage());
            try { if (conn != null) conn.rollback(); } catch (SQLException ignored) {}
        } finally {
            try { if (pstmt != null) pstmt.close(); if (conn != null) conn.close(); } catch (SQLException ignored) {}
        }
    }

    private static String extractTableName(String inputPath) {
        String fileName = inputPath.substring(inputPath.lastIndexOf('/') + 1)
                .replaceAll("\\\\", "/");
        Pattern pattern = Pattern.compile("(\\d{4})");
        Matcher matcher = pattern.matcher(fileName);
        if (matcher.find()) {
            return "course_" + matcher.group(1);
        }
        return null;
    }

    private static java.math.BigDecimal parseDecimal(String val) {
        try { return new java.math.BigDecimal(val.trim()); }
        catch (Exception e) { return java.math.BigDecimal.ZERO; }
    }
}
