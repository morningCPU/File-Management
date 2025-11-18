package com.morning.v2.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Teacher {
    private String semester;
    private String selectCode;
    private String courseCode;
    private String courseName;
    private String teachingClass;
    private Byte credits;
    private String nature;
    private String department;
    private String teacher;
    private String title;
    private String timeLocation;
    private String preferred;
    private String status;
    private String campus;
}
