package com.morning.v2.service;

import com.morning.v2.common.GetTeacherList;
import com.morning.v2.mapper.TeacherMapper;
import com.morning.v2.pojo.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    // 使用Spring自动注入TeacherMapper
    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 获取教师列表，优先从数据库查询，没有则从教务处网站获取并保存
     */
    public List<Teacher> getTeacherList(String semester, String courseName) {
        // 先从数据库查询
        List<Teacher> dbTeachers = teacherMapper.selectTeachersBySemesterAndCourseName(semester, courseName);
        if (dbTeachers != null && !dbTeachers.isEmpty()) {
            return dbTeachers;
        }
        
        // 如果数据库中没有数据，则从教务处网站获取
        List<Teacher> teachers = GetTeacherList.getTeachersByAndSemesterCourseName(semester, courseName);
        
        // 获取到数据后保存到数据库
        if (teachers != null && !teachers.isEmpty()) {
            teacherMapper.insertCoursesBatch(teachers);
        }
        
        return teachers;
    }

    /**
     * 保存教师数据到数据库，使用Spring管理的TeacherMapper
     */
    public int saveTeachers(List<Teacher> teachers) {
        int rows = 0;
        if (teachers != null && !teachers.isEmpty()) {
            rows = teacherMapper.insertCoursesBatch(teachers);
        }
        return rows;
    }

    /**
     * 保存单个教师数据
     */
    public int saveTeacher(Teacher teacher) {
        int rows = 0;
        if (teacher != null) {
            rows = teacherMapper.insertCourse(teacher);
        }
        return rows;
    }
}
