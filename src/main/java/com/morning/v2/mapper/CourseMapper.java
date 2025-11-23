package com.morning.v2.mapper;

import com.morning.v2.pojo.Course;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseMapper {
    int insertCourse(@Param("course") Course course);
    int insertCoursesBatch(@Param("courses") List<Course> courses);
    List<Course> selectAllCourses();
    List<String> selectAllYears();
}
