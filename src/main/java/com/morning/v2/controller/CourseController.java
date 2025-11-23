package com.morning.v2.controller;

import com.morning.v2.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.morning.v2.pojo.Course;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    
    /**
     * 获取所有不同的年份
     */
    @GetMapping("/years")
    public ResponseEntity<List<String>> getAllYears() {
        List<String> years = courseService.getAllYears();
        return ResponseEntity.ok(years);
    }
    
    /**
     * 保存单个课程数据（手动输入）
     */
    @PostMapping("/save")
    public ResponseEntity<Map<String, Object>> saveCourse(@RequestBody Course course) {
        Map<String, Object> response = new HashMap<>();
        try {
            courseService.saveCourse(course);
            response.put("success", true); // 添加success字段，表示成功
            response.put("message", "课程保存成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false); // 添加success字段，表示失败
            response.put("error", "课程保存失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}

