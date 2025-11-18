package com.morning.v2.commontest;

import com.morning.v2.common.GetTeacherList;
import com.morning.v2.pojo.Teacher;
import org.junit.Test;

import java.util.List;

public class TestGetTeacherList {
    @Test
    public void testGetTeachersByCourseName(){
        String semester = "2025-2026-1";
        String courseName = "人工智能";
        List<Teacher> teachers = GetTeacherList.getTeachersByAndSemesterCourseName(semester,courseName);
        System.out.println(teachers.size());
        teachers.forEach(System.out::println);
    }
}
