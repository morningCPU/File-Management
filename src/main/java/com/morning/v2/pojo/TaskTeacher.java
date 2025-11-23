package com.morning.v2.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TaskTeacher {
    private Long id;
    private Long taskId;
    private String courseCode;
    private String courseName;
    private String teacherName;
    private String title;
    private String department;
    private String nature;
    private String evaluationStatus; // pending, completed
    private Date createTime;
}