package com.morning.v2.test;

import com.morning.v2.Util.DBUtil;
import com.morning.v2.common.GetCourseList;
import com.morning.v2.dao.CourseDao;
import com.morning.v2.pojo.Course;
import org.junit.Test;

import java.util.ArrayList;

public class TestGetCourseList {
    @Test
    public void testStandardizeDirectory(){
        String resultFolderUrl = "src/main/TestExample/result/";
        ArrayList<Course> courses = GetCourseList.standardizeDirectory(resultFolderUrl, "UTF-8");
        CourseDao courseDao = new CourseDao(DBUtil.getConnection());
        courseDao.insertCoursesBatch(courses);
    }
}
