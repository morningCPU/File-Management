package com.morning.v2;

import com.morning.v2.Util.SqlSessionUtil;
import com.morning.v2.common.GetTeacherList;
import com.morning.v2.mapper.TeacherMapper;
import com.morning.v2.pojo.Teacher;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class TestReadTeacher {
    @Test
    public void testReadTeacher(){
        SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
        TeacherMapper mapper = sqlSession.getMapper(TeacherMapper.class);
        String courseName = "高等数学";
        List<Teacher> teachers = GetTeacherList.getTeachersByCourseName(courseName);
        int rows = mapper.insertCoursesBatch(teachers);
        System.out.println(rows);
    }
}
