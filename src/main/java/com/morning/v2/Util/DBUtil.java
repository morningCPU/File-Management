package com.morning.v2.Util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

    private static final Properties PROP = new Properties();

    static {
        // 从 classpath 读取 db.properties
        try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException("db.properties 不在 classpath 中");
            }
            PROP.load(in);
            Class.forName(PROP.getProperty("driver"));
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("初始化数据库配置失败", e);
        }
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    PROP.getProperty("url"),
                    PROP.getProperty("user"),
                    PROP.getProperty("password"));
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}