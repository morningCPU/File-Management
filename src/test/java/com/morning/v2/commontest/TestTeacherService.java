package com.morning.v2.commontest;

import com.morning.v2.pojo.Teacher;
import com.morning.v2.service.TeacherService;
import org.junit.Test;

import java.util.List;

public class TestTeacherService {
    @Test
    public void testGetTeacherList(){
        String semester = "2022-2023-1";
        String courseName = "马克思主义基本原理";
        TeacherService teacherService = new TeacherService();
        List<Teacher> teacherList = teacherService.getTeacherList(semester, courseName);
        System.out.println(teacherList.size());
        teacherList.forEach(System.out::println);
    }
}
