package com.morning.v2.commontest;

import com.morning.v2.common.GetTeacherList;
import com.morning.v2.pojo.Teacher;
import org.junit.Test;

import java.util.List;

public class TestGetTeacherList {
    @Test
    public void testGetTeachersByCourseName(){
        String semester = "2022-2023-1";
        String courseName = "马克思主义基本原理";
        List<Teacher> teachers = GetTeacherList.getTeachersByAndSemesterCourseName(semester,courseName);
        System.out.println(teachers.size());
        teachers.forEach(System.out::println);
    }
}
