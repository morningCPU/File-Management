package com.morning.v2.mapper;

import com.morning.v2.pojo.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeacherMapper {
    //插入一个
    int insertCourse(@Param("teacher") Teacher teacher);
    
    //插入多个
    int insertCoursesBatch(@Param("teachers") List<Teacher> teachers);
    
    //根据学期和课程名称查询教师列表
    List<Teacher> selectTeachersBySemesterAndCourseName(@Param("semester") String semester, @Param("courseName") String courseName);
}
