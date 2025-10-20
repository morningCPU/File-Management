package com.morning.v1.getclasstable;

/**
 * ClassName: HelloWorld
 * Package: PACKAGE_NAME
 * Description:
 * &#064;Author  morning
 * &#064;Create  2025/6/5 21:11
 * &#064;Version  1.0
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URLEncoder;

public class CourseQuery {

    public static void main(String[] args) {
        // 示例：查询“离散数学”
        queryAllPages("离散数学", "离散数学课程.csv");
    }

    /**
     * 查询指定课程名的所有分页内容，并导出为 CSV
     */
    public static void queryAllPages(String courseName, String outputCsvPath) {
        String baseUrl = "http://jwc.swjtu.edu.cn/vatuu/CourseAction";
        int page = 1;
        boolean hasData = false;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputCsvPath))) {

            boolean firstPage = true;
            while (true) {
                String query = String.format(
                        "?setAction=queryCourseList&viewType=&jumpPage=%d"
                                + "&selectTableType=ThisTerm&selectAction=QueryName"
                                + "&key1=%s&courseType=all&key4=&btn_query=执行查询"
                                + "&orderType=teachId&orderValue=asc",
                        page, URLEncoder.encode(courseName, "UTF-8")
                );
                String url = baseUrl + query;
                System.out.println("🔍 正在抓取第 " + page + " 页：" + url);

                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .timeout(10000)
                        .get();

                // 查找课程表
                Element table = doc.selectFirst("table");
                if (table == null) {
                    System.out.println("⚠️ 第 " + page + " 页未找到表格（可能是最后一页）");
                    break;
                }

                Elements rows = table.select("tr");
                if (rows.isEmpty()) {
                    System.out.println("⚠️ 第 " + page + " 页无数据，结束抓取。");
                    break;
                }

                // 第一页写表头
                for (int i = 0; i < rows.size(); i++) {
                    if (!firstPage && i == 0) continue; // 跳过重复表头
                    Elements cols = rows.get(i).select("th, td");
                    StringBuilder sb = new StringBuilder();
                    for (int c = 0; c < cols.size(); c++) {
                        String text = cols.get(c).text().replaceAll("[\\r\\n]+", " ");
                        sb.append("\"").append(text).append("\"");
                        if (c < cols.size() - 1) sb.append(",");
                    }
                    writer.write(sb.toString());
                    writer.newLine();
                }

                hasData = true;
                firstPage = false;
                page++;

                // 检查下一页是否存在（可根据 HTML 是否包含“下一页”按钮判断）
                if (!doc.text().contains("下一页") && !doc.text().contains("›")) {
                    break;
                }

                Thread.sleep(1000); // 避免请求过快
            }

        } catch (Exception e) {
            System.err.println("❌ 抓取出错：" + e.getMessage());
        }

        if (hasData) {
            System.out.println("✅ 全部页面抓取完成，已保存到：" + outputCsvPath);
        } else {
            System.out.println("⚠️ 未抓取到任何数据。");
        }
    }
}
