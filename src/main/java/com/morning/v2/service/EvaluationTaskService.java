package com.morning.v2.service;

import com.morning.v2.pojo.EvaluationTask;
import com.morning.v2.pojo.TaskTeacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.morning.v2.mapper.EvaluationTaskMapper;
import com.morning.v2.mapper.TaskTeacherMapper;

import java.util.Date;
import java.util.List;

@Service
public class EvaluationTaskService {

    @Autowired
    private EvaluationTaskMapper evaluationTaskMapper;
    
    @Autowired
    private TaskTeacherMapper taskTeacherMapper;

    /**
     * 创建新任务并关联教师列表
     */
    @Transactional
    public Long createTaskWithTeachers(EvaluationTask task, List<TaskTeacher> taskTeachers) {

        // 插入任务
        evaluationTaskMapper.insertTask(task);
        
        // 为每个关联记录设置任务ID
        Long taskId = task.getId();
        for (TaskTeacher taskTeacher : taskTeachers) {
            taskTeacher.setTaskId(taskId);
            taskTeacher.setEvaluationStatus("未评价"); // 默认状态为未评价
        }
        
        // 批量插入关联记录
        taskTeacherMapper.insertTaskTeachersBatch(taskTeachers);
        
        return taskId;
    }

    /**
     * 获取所有任务列表
     */
    public List<EvaluationTask> getAllTasks() {
        return evaluationTaskMapper.selectAllTasks();
    }

    /**
     * 根据ID获取任务详情
     */
    public EvaluationTask getTaskById(Long id) {
        return evaluationTaskMapper.selectTaskById(id);
    }

    /**
     * 更新任务状态
     */
    public boolean updateTaskStatus(Long id, String status) {
        return evaluationTaskMapper.updateTaskStatus(id, status) > 0;
    }

    /**
     * 更新任务信息
     */
    public boolean updateTask(EvaluationTask task) {
        return evaluationTaskMapper.updateTask(task) > 0;
    }

    /**
     * 删除任务（同时删除关联的教师记录）
     */
    @Transactional
    public boolean deleteTask(Long id) {
        // 先删除关联的教师记录
        taskTeacherMapper.deleteByTaskId(id);
        // 再删除任务
        return evaluationTaskMapper.deleteTask(id) > 0;
    }

    /**
     * 获取任务相关的教师列表
     */
    public List<TaskTeacher> getTaskTeachers(Long taskId) {
        return taskTeacherMapper.selectByTaskId(taskId);
    }

    /**
     * 统计任务下特定状态的教师数量
     */
    public int countTaskTeachersByStatus(Long taskId, String status) {
        return taskTeacherMapper.countByTaskId(taskId, status);
    }
}