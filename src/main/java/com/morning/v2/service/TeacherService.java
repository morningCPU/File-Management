package com.morning.v2.service;

import com.morning.v2.Util.SqlSessionUtil;
import com.morning.v2.common.GetTeacherList;
import com.morning.v2.mapper.TeacherMapper;
import com.morning.v2.pojo.Teacher;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TeacherService {

    /**
     * 获取教师列表，优先从数据库查询，没有则从教务处网站获取并保存
     */
    public List<Teacher> getTeacherList(String semester, String className) {
        // 优先从数据库查询
        SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        
        try {
            // 先从数据库查询
            List<Teacher> dbTeachers = mapper.selectTeachersBySemesterAndCourseName(semester, className);
            if (dbTeachers != null && !dbTeachers.isEmpty()) {
                return dbTeachers;
            }
            
            // 如果数据库中没有数据，则从教务处网站获取
            List<Teacher> teachers = GetTeacherList.getTeachersByAndSemesterCourseName(semester, className);
            
            // 获取到数据后保存到数据库
            if (teachers != null && !teachers.isEmpty()) {
                mapper.insertCoursesBatch(teachers);
            }
            
            return teachers;
        } finally {
            // 确保关闭SqlSession
            sqlSession.close();
        }
    }

    /**
     * 保存教师数据到数据库，使用SqlSession和TeacherMapper
     */
    public int saveTeachers(List<Teacher> teachers) {
        // 使用SqlSessionUtil获取SqlSession，设置自动提交为true
        SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        
        int rows = 0;
        if (teachers != null && !teachers.isEmpty()) {
            rows = mapper.insertCoursesBatch(teachers);
        }
        
        // 关闭SqlSession
        sqlSession.close();
        
        return rows;
    }

    /**
     * 保存单个教师数据
     */
    public int saveTeacher(Teacher teacher) {
        SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        
        int rows = 0;
        if (teacher != null) {
            rows = mapper.insertCourse(teacher);
        }
        
        sqlSession.close();
        return rows;
    }
}
