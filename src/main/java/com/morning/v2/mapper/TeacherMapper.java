package com.morning.v2.mapper;

import com.morning.v2.pojo.Teacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TeacherMapper {
    int insertCourse(@Param("teacher") Teacher teacher);
    int insertCoursesBatch(@Param("teachers") List<Teacher> teachers);
}
