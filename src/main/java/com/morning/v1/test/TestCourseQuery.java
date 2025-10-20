package com.morning.v1.test;

/**
 * ClassName: HelloWorld
 * Package: PACKAGE_NAME
 * Description:
 * &#064;Author  morning
 * &#064;Create  2025/6/5 21:11
 * &#064;Version  1.0
 */
import com.morning.v1.getclasstable.CourseQuery;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * 测试 CourseQueryAll 抓取功能
 */
public class TestCourseQuery {

    public static void main(String[] args) {
        // 1️⃣ 测试抓取指定课程
        String courseName = "离散数学";
        String outputCsv = "src/main/TestExample/result/离散数学测试.csv";

        System.out.println("🚀 开始测试抓取课程：" + courseName);
        CourseQuery.queryAllPages(courseName, outputCsv);

        // 2️⃣ 验证文件内容
        System.out.println("📄 检查输出文件内容（前 5 行）：");
        try (BufferedReader br = new BufferedReader(new FileReader(outputCsv))) {
            String line;
            int count = 0;
            while ((line = br.readLine()) != null && count < 5) {
                System.out.println(line);
                count++;
            }
            if (count == 0) {
                System.out.println("⚠️ 文件为空，请检查抓取逻辑或网站可访问性。");
            } else {
                System.out.println("✅ 抓取测试成功，共打印前 " + count + " 行。");
            }
        } catch (IOException e) {
            System.err.println("❌ 无法读取输出文件：" + e.getMessage());
        }
    }
}
