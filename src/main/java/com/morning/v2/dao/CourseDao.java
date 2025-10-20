package com.morning.v2.dao;

import com.morning.v2.pojo.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CourseDao {
    private final Connection conn;

    public CourseDao(Connection conn) {
        this.conn = conn;
    }

    /**
     * 插入单个课程记录
     */
    public boolean insertCourse(Course c) {
        String sql = "INSERT INTO courses " +
                "(year, course_type, course_name, nature, credits, practice_credits, semester, school, indicators, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            setParams(ps, c);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 批量插入课程记录
     */
    public boolean insertCoursesBatch(List<Course> courses) {
        String sql = "INSERT INTO courses " +
                "(year, course_type, course_name, nature, credits, practice_credits, semester, school, indicators, notes) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false); // 开启事务

            for (Course c : courses) {
                setParams(ps, c);
                ps.addBatch();
            }

            ps.executeBatch();
            conn.commit();
            conn.setAutoCommit(true);
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            return false;
        }
    }

    /**
     * 通用参数设置方法
     */
    private void setParams(PreparedStatement ps, Course c){
        try {
            ps.setString(1, c.getYear());
            ps.setString(2, c.getCourseType());
            ps.setString(3, c.getCourseName());
            ps.setString(4, c.getNature());
            ps.setBigDecimal(5, c.getCredits());
            ps.setBigDecimal(6, c.getPracticeCredits());
            ps.setString(7, c.getSemester());
            ps.setString(8, c.getSchool());
            ps.setString(9, c.getIndicators());
            ps.setString(10, c.getNotes());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
