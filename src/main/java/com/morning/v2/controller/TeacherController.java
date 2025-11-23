package com.morning.v2.controller;

import com.morning.v2.pojo.Teacher;
import com.morning.v2.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    /**
     * 根据课程信息获取并保存教师数据
     * @param requestData 包含年份、课程名称和学期的请求数据
     * @return 处理结果
     */
    @PostMapping("/fetchAndSave")
    public Map<String, Object> fetchAndSave(@RequestBody Map<String, String> requestData) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 从请求数据中获取信息
            String semester = requestData.get("semester");
            String courseName = requestData.get("courseName");
            
            if (semester == null || courseName == null) {
                result.put("success", false);
                result.put("message", "学期和课程名称不能为空");
                return result;
            }
            
            // 使用TeacherService获取教师列表
            List<Teacher> teachers = teacherService.getTeacherList(semester, courseName);
            
            if (teachers.isEmpty()) {
                result.put("success", true);
                result.put("message", "未找到相关教师数据");
                result.put("count", 0);
                return result;
            }
            
            // 保存教师数据到数据库
            int savedRows = teacherService.saveTeachers(teachers);
            
            result.put("success", true);
            result.put("message", "教师数据获取并保存成功");
            result.put("count", savedRows);
            
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "处理失败: " + e.getMessage());
            e.printStackTrace();
        }
        
        return result;
    }
    
    /**
     * 获取教师列表
     * @param semester 学期
     * @param courseName 课程名称
     * @return 教师列表（确保返回所有教师数据）
     */
    @GetMapping("/list")
     public List<?> getTeachersList(@RequestParam String semester, @RequestParam String courseName) {
         // 直接调用服务层获取教师列表并返回所有数据
         return teacherService.getTeacherList(semester, courseName);
     }
}

