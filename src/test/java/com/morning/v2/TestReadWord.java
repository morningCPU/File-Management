package com.morning.v2;

import com.morning.v2.Util.SqlSessionUtil;
import com.morning.v2.common.GetCourseList;
import com.morning.v2.common.GetCsvFilesUtil;
import com.morning.v2.mapper.CourseMapper;
import com.morning.v2.pojo.Course;
import org.apache.ibatis.session.SqlSession;
import org.junit.Test;

import java.util.ArrayList;

public class TestReadWord {
    @Test
    public void testAll(){
        String docxFolderUrl = "src/main/TestExample/";
        String resultFolderUrl = "src/main/TestExample/result/";

        GetCsvFilesUtil.getCSV(docxFolderUrl,resultFolderUrl);
        ArrayList<Course> courses = GetCourseList.standardizeDirectory(resultFolderUrl, "UTF-8");
        SqlSession sqlSession = SqlSessionUtil.getSqlSession(true);
        CourseMapper mapper = sqlSession.getMapper(CourseMapper.class);
        int rows = mapper.insertCoursesBatch(courses);
        System.out.println(rows);
    }
}
