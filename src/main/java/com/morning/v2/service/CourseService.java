package com.morning.v2.service;

import com.morning.v2.common.GetCourseList;
import com.morning.v2.pojo.Course;
import com.morning.v2.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseMapper courseMapper;

    /**
     * 从CSV文件读取并处理课程数据
     */
    public List<Course> processCourseFromCsv(String csvFilePath) {
        File csvFile = new File(csvFilePath);
        if (!csvFile.exists() || !csvFile.isFile()) {
            throw new IllegalArgumentException("CSV文件不存在或路径错误: " + csvFilePath);
        }

        // 使用现有的GetCourseList工具类处理数据
        return GetCourseList.standardizeDirectory(csvFile.getParent(),"UTF-8");
    }

    /**
     * 保存课程数据到数据库
     */
    public void saveCourses(List<Course> courses) {
        // 使用批量插入方法
        if (courses != null && !courses.isEmpty()) {
            courseMapper.insertCoursesBatch(courses);
        }
    }

    /**
     * 保存单个课程数据
     */
    public void saveCourse(Course course) {
        if (course != null) {
            courseMapper.insertCourse(course);
        }
    }
    
    /**
     * 获取所有课程数据
     */
    public List<Course> getAllCourses() {
        return courseMapper.selectAllCourses();
    }
}
