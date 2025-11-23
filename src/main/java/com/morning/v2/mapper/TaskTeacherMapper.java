package com.morning.v2.mapper;

import com.morning.v2.pojo.TaskTeacher;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TaskTeacherMapper {
    int insertTaskTeacher(@Param("taskTeacher") TaskTeacher taskTeacher);
    int insertTaskTeachersBatch(@Param("taskTeachers") List<TaskTeacher> taskTeachers);
    List<TaskTeacher> selectByTaskId(@Param("taskId") Long taskId);
    List<TaskTeacher> selectByTeacherName(@Param("teacherName") String teacherName);
    int updateEvaluationStatus(@Param("id") Long id, @Param("status") String status);
    int deleteByTaskId(@Param("taskId") Long taskId);
    int countByTaskId(@Param("taskId") Long taskId, @Param("status") String status);
}