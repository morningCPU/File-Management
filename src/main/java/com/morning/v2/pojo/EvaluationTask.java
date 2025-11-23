package com.morning.v2.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EvaluationTask {
    private Long id;
    private String taskName;
    private Date createTime;
    private String creator;
    private String status; // pending, in_progress, completed
    private String description;
    private Date endTime;
}