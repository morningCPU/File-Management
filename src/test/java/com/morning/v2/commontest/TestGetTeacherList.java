package com.morning.v2.commontest;

import com.morning.v2.Util.SqlSessionUtil;
import com.morning.v2.common.GetTeacherList;
import com.morning.v2.mapper.TeacherMapper;
import com.morning.v2.pojo.Teacher;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.List;

public class TestGetTeacherList {
    @Test
    public void testGetTeachersByCourseName(){
        String courseName = "人工智能";
        List<Teacher> teachers = GetTeacherList.getTeachersByCourseName(courseName);
        teachers.forEach(System.out::println);
    }
}
