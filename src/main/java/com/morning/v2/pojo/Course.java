package com.morning.v2.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Course {
    private Integer id;
    private String year;
    private String courseType;
    private String courseName;
    private String nature;
    private BigDecimal credits;
    private BigDecimal practiceCredits;
    private String semester;
    private String school;
    private String indicators;
    private String notes;
}
