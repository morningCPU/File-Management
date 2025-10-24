package com.morning.v2.common;

import com.morning.v2.pojo.Teacher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class GetTeacherList {
    public static List<Teacher> getTeachersByCourseName(String courseName) {
        List<Teacher> result = new ArrayList<>();
        String baseUrl = "http://jwc.swjtu.edu.cn/vatuu/CourseAction";
        int page = 1;

        try {
            while (true) {
                String query = String.format(
                        "?setAction=queryCourseList&viewType=&jumpPage=%d"
                                + "&selectTableType=ThisTerm&selectAction=QueryName"
                                + "&key1=%s&courseType=all&key4=&btn_query=执行查询"
                                + "&orderType=teachId&orderValue=asc",
                        page, URLEncoder.encode(courseName, "UTF-8")
                );
                String url = baseUrl + query;

                Document doc = Jsoup.connect(url)
                        .userAgent("Mozilla/5.0")
                        .timeout(10_000)
                        .get();

                Element table = doc.selectFirst("table");
                if (table == null) {
                    break; // 无表格，结束
                }

                Elements rows = table.select("tr");
                if (rows.isEmpty()) {
                    break; // 无行，结束
                }

                boolean firstRow = true;
                for (Element row : rows) {
                    // 跳过表头
                    if (firstRow) {
                        firstRow = false;
                        continue;
                    }
                    Elements cols = row.select("th, td");
                    if (cols.size() < 14) {
                        continue; // 列数不对，跳过
                    }

                    // 过滤无用行
                    String firstCol = cols.get(0).text().trim();
                    if (firstCol.contains("没有找到相关记录")) {
                        continue;
                    }

                    //判断学期
                    LocalDate today = LocalDate.now();
                    int year = today.getYear();
                    int month = today.getMonthValue();
                    String year_res = String.valueOf(year);

                    if (month == 1) {
                        year_res += "_1";
                    } else if (month <= 8) {
                        year_res += "_2";
                    } else {
                        year_res += "_3";
                    }

                    // 去掉最后一列“选课名单”
                    Teacher t = new Teacher();
                    t.setYear(year_res);
                    t.setSelectCode(cols.get(1).text().trim());
                    t.setCourseCode(cols.get(2).text().trim());
                    t.setCourseName(cols.get(3).text().trim());
                    t.setTeachingClass(cols.get(4).text().trim());
                    t.setCredits((byte) Float.parseFloat(cols.get(5).text().trim()));
                    t.setNature(cols.get(6).text().trim());
                    t.setDepartment(cols.get(7).text().trim());
                    t.setTeacher(cols.get(8).text().trim());
                    t.setTitle(cols.get(9).text().trim());
                    t.setTimeLocation(cols.get(10).text().trim());
                    t.setPreferred(cols.get(11).text().trim());
                    t.setStatus(cols.get(12).text().trim());
                    t.setCampus(cols.get(13).text().trim());

                    result.add(t);
                }

                // 简单判断下一页：没有“下一页”文字就结束
                if (!doc.text().contains("下一页") && !doc.text().contains("›")) {
                    break;
                }

                page++;
                Thread.sleep(1_000); //  polite delay
            }
        } catch (Exception e) {
            // 捕获任何异常，保证返回空列表而不是崩溃
            e.printStackTrace();
        }

        return result;
    }
}
