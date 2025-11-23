package com.morning.v2.controller;

import com.morning.v2.pojo.EvaluationTask;
import com.morning.v2.pojo.TaskTeacher;
import com.morning.v2.service.EvaluationTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class EvaluationTaskController {

    @Autowired
    private EvaluationTaskService evaluationTaskService;

    /**
     * 创建新任务
     */
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createTask(@RequestBody Map<String, Object> requestData) {
        Map<String, Object> response = new HashMap<>();
        try {
            // 提取任务信息
            String taskName = (String) requestData.get("taskName");
            String deadline = (String) requestData.get("deadline");
            String description = (String) requestData.get("description");
            List<Map<String, Object>> teacherDataList = (List<Map<String, Object>>) requestData.get("teachers");

            // 创建任务对象
            EvaluationTask task = new EvaluationTask();
            task.setTaskName(taskName);
            // 设置任务创建时间
            task.setCreateTime(new Date());
            task.setCreator("admin");
            task.setStatus("进行中"); // 默认状态为进行中
            task.setDescription(description);
            // 将字符串转换为Date类型
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date endTime = dateFormat.parse(deadline);
                task.setEndTime(endTime);
            } catch (ParseException e) {
                throw new RuntimeException("日期格式错误，请使用yyyy-MM-dd格式", e);
            }

            // 转换教师数据
            List<TaskTeacher> taskTeachers = teacherDataList.stream().map(teacherData -> {
                // 检查字段是否存在且类型正确
                if (!(teacherData.get("courseCode") instanceof String) ||
                        !(teacherData.get("courseName") instanceof String) ||
                        !(teacherData.get("teacherName") instanceof String)) {
                    throw new IllegalArgumentException("教师数据字段类型不匹配");
                }

                String courseCode = (String) teacherData.get("courseCode");
                String courseName = (String) teacherData.get("courseName");
                String department = (String) teacherData.getOrDefault("department", "");
                String nature = (String) teacherData.getOrDefault("nature", "");
                String teacherName = (String) teacherData.get("teacherName");
                String title = (String) teacherData.getOrDefault("title", "");

                // 检查字段值是否为空
                if (courseCode == null || courseCode.trim().isEmpty() ||
                        courseName == null || courseName.trim().isEmpty() ||
                        teacherName == null || teacherName.trim().isEmpty()) {
                    throw new IllegalArgumentException("教师数据中缺少必要的字段");
                }

                TaskTeacher tt = new TaskTeacher();
                tt.setCourseCode(courseCode);
                tt.setCourseName(courseName);
                tt.setDepartment(department);
                tt.setNature(nature);
                tt.setTeacherName(teacherName);
                tt.setTitle(title);

                return tt;
            }).toList();

            // 保存任务和关联教师
            Long taskId = evaluationTaskService.createTaskWithTeachers(task, taskTeachers);

            response.put("success", true);
            response.put("taskId", taskId);
            response.put("message", "任务创建成功");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "任务创建失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 获取所有任务列表
     */
    @GetMapping("/all")
    public ResponseEntity<List<EvaluationTask>> getAllTasks() {
        List<EvaluationTask> tasks = evaluationTaskService.getAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTaskById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            EvaluationTask task = evaluationTaskService.getTaskById(id);
            if (task != null) {
                response.put("success", true);
                response.put("task", task);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "任务不存在");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 获取任务关联的教师列表
     */
    @GetMapping("/{id}/teachers")
    public ResponseEntity<Map<String, Object>> getTaskTeachers(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<TaskTeacher> teachers = evaluationTaskService.getTaskTeachers(id);
            response.put("success", true);
            response.put("teachers", teachers);
            response.put("totalCount", teachers.size());
            response.put("pendingCount", evaluationTaskService.countTaskTeachersByStatus(id, "未评价"));
            response.put("completedCount", evaluationTaskService.countTaskTeachersByStatus(id, "已评价"));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "获取教师列表失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 更新任务状态
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Map<String, Object>> updateTaskStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        try {
            String status = request.get("status");
            boolean updated = evaluationTaskService.updateTaskStatus(id, status);
            if (updated) {
                response.put("success", true);
                response.put("message", "任务状态更新成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "任务不存在或更新失败");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "更新状态失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTask(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean deleted = evaluationTaskService.deleteTask(id);
            if (deleted) {
                response.put("success", true);
                response.put("message", "任务删除成功");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "任务不存在或删除失败");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "删除任务失败: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}