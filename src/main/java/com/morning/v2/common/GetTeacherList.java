package com.morning.v2.common;

import com.morning.v2.pojo.Teacher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

public class GetTeacherList {

    /* ========================= 1. 学期映射 ========================= */
    private static final Map<String, String> TERM_ID_MAP = buildRecentTermMap();

    /** 按时间倒序生成最近的学期映射 */
    private static Map<String, String> buildRecentTermMap() {
        Map<String, String> map = new LinkedHashMap<>();
        // 手动添加已知的学期和对应的 selectTermId
        map.put("2020-2021-1", "96");
        map.put("2020-2021-2", "98");
        map.put("2021-2022-1", "100");
        map.put("2021-2022-2", "105");
        map.put("2022-2023-1", "107");
        map.put("2022-2023-2", "109");
        map.put("2023-2024-1", "112");
        map.put("2023-2024-2", "113");
        map.put("2024-2025-1", "116");
        map.put("2024-2025-2", "118");
        map.put("2025-2026-1", "120");
        map.put("2025-2026-2", "122");
        map.put("2026-2027-1", "124");
        map.put("2026-2027-2", "126");
        return Collections.unmodifiableMap(map);
    }

    /* ========================= 2. 核心抓取 ========================= */
    /**
     * 指定学期 + 课程关键字 抓取开课列表
     * @param semester 学期编码  yyyy-yyyy-[1|2]
     * @param courseName 课程关键字
     * @return 教师列表
     */
    public static List<Teacher> getTeachersByAndSemesterCourseName(String semester, String courseName) {
        List<Teacher> result = new ArrayList<>();
        String termId = TERM_ID_MAP.get(semester);
        if (termId == null) return result;

        String[] parts = semester.split("-");
        String termName = parts[0] + "-" + parts[1] + "第" + parts[2] + "学期";

        String baseUrl = "http://jwc.swjtu.edu.cn/vatuu/CourseAction";
        String btnQuery = URLEncoder.encode("执行查询", StandardCharsets.UTF_8);
        int page = 1;

        try {
            while (true) {
                String query = String.format(
                        "?setAction=queryCourseList&viewType=&jumpPage=%d"
                                + "&selectTableType=History&selectTermId=%s&selectTermName=%s"
                                + "&selectAction=QueryName&key1=%s&courseType=all&key4=&btn_query=%s"
                                + "&orderType=teachId&orderValue=asc",
                        page,
                        URLEncoder.encode(termId, StandardCharsets.UTF_8),
                        URLEncoder.encode(termName, StandardCharsets.UTF_8),
                        URLEncoder.encode(courseName, StandardCharsets.UTF_8),
                        btnQuery
                );

                Document doc = Jsoup.connect(baseUrl + query)
                        .userAgent("Mozilla/5.0")
                        .timeout(10_000)
                        .get();

                System.out.println(baseUrl + query);
                Element table = doc.selectFirst("table");
                if (table == null) break;
                Elements rows = table.select("tr");
                if (rows.size() < 2) break;

                boolean firstRow = true;
                for (Element row : rows) {
                    if (firstRow) { firstRow = false; continue; }
                    Elements cols = row.select("th, td");
                    if (cols.size() < 14) continue;
                    if (cols.get(0).text().contains("没有找到相关记录")) continue;

                    Teacher t = new Teacher();
                    t.setSemester(semester);
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
                if (!doc.text().contains("下一页") && !doc.text().contains("›")) break;
                page++;
                Thread.sleep(1_000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /* ========================= 3. 便捷 API ========================= */
    /** 最近 10 学期全部合并 */
    public static List<Teacher> getTeachersByCourseName(String courseName) {
        List<Teacher> all = new ArrayList<>();
        for (String semester : TERM_ID_MAP.keySet()) {
            all.addAll(getTeachersByAndSemesterCourseName(semester, courseName));
        }
        return all;
    }

    /** 仅当前学期 */
    public static List<Teacher> getTeachersByCourseNameCurrent(String courseName) {
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        int month = today.getMonthValue();
        int flag = (month >= 2 && month <= 7) ? 2 : 1;
        int baseYear = flag == 2 ? year : year - 1;
        String currentSemester = baseYear + "-" + (baseYear + 1) + "-" + flag;
        return getTeachersByAndSemesterCourseName(currentSemester, courseName);
    }
}