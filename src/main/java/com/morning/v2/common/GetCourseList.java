package com.morning.v2.common;

import com.morning.v2.pojo.Course;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetCourseList {

    public static ArrayList<Course> standardizeDirectory(String dirPath, String charsetName) {
        ArrayList<Course> allCourses = new ArrayList<>();

        Path dir = Paths.get(dirPath);
        if (!Files.isDirectory(dir)) {
            throw new IllegalArgumentException("输入路径不是目录: " + dirPath);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, "*.csv")) {
            for (Path csv : stream) {
                ArrayList<Course> list = standardizeFile(csv, charsetName);
                allCourses.addAll(list);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("目录处理完成，共计课程数：" + allCourses.size());
        return allCourses;
    }

    private static ArrayList<Course> standardizeFile(Path filePath, String charsetName) throws IOException {
        ArrayList<Course> courses = new ArrayList<>();

        String fileName = filePath.getFileName().toString();
        String year = extractYear(fileName);

        Pattern natureOk = Pattern.compile("^(必修|选修|限修).*$");
        Pattern dataRow = Pattern.compile("^\"[^\"]+\",\"[^\"]+\",\"[^\"]+\",\"\\d+(\\.\\d+)?\".*");

        try (BufferedReader br = Files.newBufferedReader(filePath, Charset.forName(charsetName))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || !dataRow.matcher(line).matches()) continue;

                String[] f = line.split("\",\"", -1);
                if (f.length != 9) continue;
                for (int i = 0; i < f.length; i++) {
                    f[i] = f[i].replaceAll("^\"|\"$", "").trim();
                }

                if (!natureOk.matcher(f[2]).find()) continue;

                // ====== 各列单独处理 ======
                // 第1列：去掉英文
                f[0] = removeEnglish(f[0]);

                // 第2列：不动

                // 第3列：去掉英文
                f[2] = removeEnglish(f[2]);

                // 第4列：学分格式化
                f[3] = formatCredit(f[3]);

                // 第5列：实践学分格式化
                f[4] = formatCredit(f[4]);

                // 第6列：提取学期
                f[5] = extractSemester(f[5]);

                // 第7列：去掉英文
                f[6] = removeEnglish(f[6]);

                // 第8列、第9列：保持原有清洗逻辑
                f[7] = cleanCell(f[7]);
                f[8] = cleanCell(f[8]);
                // ====== 处理结束 ======

                Course c = new Course();
                c.setYear(year);
                c.setCourseType(f[0]);
                c.setCourseName(f[1]);
                c.setNature(f[2]);
                c.setCredits(parseDecimal(f[3]));
                c.setPracticeCredits(parseDecimal(f[4]));
                c.setSemester(f[5]);
                c.setSchool(f[6]);
                c.setIndicators(f[7]);
                c.setNotes(f[8]);

                courses.add(c);
            }
        }

        System.out.println("已处理文件：" + filePath.getFileName() + "（课程数：" + courses.size() + "）");
        return courses;
    }

    // 去掉英文部分，仅保留中文
    private static String removeEnglish(String text) {
        if ("null".equals(text) || text == null) return "null";
        // 删除所有英文字母、空格及常见标点
        String result = text.replaceAll("[A-Za-z（）()\\[\\]{}.,:;\"'\\-_/]+", "").trim();
        return result.isEmpty() ? text.trim() : result;
    }

    private static String extractYear(String fileName) {
        Matcher m = Pattern.compile("(\\d{4})").matcher(fileName);
        return m.find() ? m.group(1) : "未知";
    }

    private static String extractSemester(String s) {
        if ("null".equals(s)) return "null";
        return s.replaceAll("学期.*$", "学期");
    }

    private static String cleanCell(String cell) {
        if ("null".equals(cell)) return "null";
        String s = cell.replaceAll("（[^）]*）$", "");
        s = s.replaceAll("(?i)[a-z].*$", "");
        return s.trim();
    }

    private static String formatCredit(String s) {
        if ("null".equals(s)) return "null";
        try {
            return String.format("%.1f", Double.parseDouble(s));
        } catch (NumberFormatException e) {
            return "null";
        }
    }

    private static BigDecimal parseDecimal(String s) {
        if (s == null || "null".equals(s)) return null;
        try {
            return new BigDecimal(s);
        } catch (Exception e) {
            return null;
        }
    }
}
