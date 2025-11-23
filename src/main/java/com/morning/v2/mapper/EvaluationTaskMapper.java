package com.morning.v2.mapper;

import com.morning.v2.pojo.EvaluationTask;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EvaluationTaskMapper {
    int insertTask(@Param("task") EvaluationTask task);
    EvaluationTask selectTaskById(@Param("id") Long id);
    List<EvaluationTask> selectAllTasks();
    int updateTaskStatus(@Param("id") Long id, @Param("status") String status);
    int updateTask(@Param("task") EvaluationTask task);
    int deleteTask(@Param("id") Long id);
}