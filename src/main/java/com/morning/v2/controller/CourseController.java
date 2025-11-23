package com.morning.v2.controller;

import com.morning.v2.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.morning.v2.pojo.Course;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 获取所有课程数据
     */
    @GetMapping("/all")
    public ResponseEntity<List<Course>> getAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }
}

