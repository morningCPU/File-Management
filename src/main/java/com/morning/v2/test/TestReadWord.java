package com.morning.v2.test;

import com.morning.v2.Util.DBUtil;
import com.morning.v2.common.GetCourseList;
import com.morning.v2.common.GetCsvFilesUtil;
import com.morning.v2.dao.CourseDao;
import com.morning.v2.pojo.Course;
import org.junit.Test;

import java.util.ArrayList;

public class TestReadWord {
    @Test
    public void testAll(){
        String docxFolderUrl = "src/main/TestExample/";
        String resultFolderUrl = "src/main/TestExample/result/";

        GetCsvFilesUtil.getCSV(docxFolderUrl,resultFolderUrl);
        ArrayList<Course> courses = GetCourseList.standardizeDirectory(resultFolderUrl, "UTF-8");
        CourseDao courseDao = new CourseDao(DBUtil.getConnection());
        courseDao.insertCoursesBatch(courses);
    }
}
