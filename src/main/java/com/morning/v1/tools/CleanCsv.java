package com.morning.v1.tools;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.regex.*;

public class CleanCsv {

    public static void cleanCsv(String inputPath, String outputPath, String charsetName) throws IOException {
        Path in  = Paths.get(inputPath);
        Path out = Paths.get(outputPath);

        Pattern natureOk = Pattern.compile("^(必修|选修|限修).*$");
        Pattern dataRow  = Pattern.compile("^\"[^\"]+\",\"[^\"]+\",\"[^\"]+\",\"\\d+(\\.\\d+)?\".*");

        try (BufferedReader br = Files.newBufferedReader(in, Charset.forName(charsetName));
             BufferedWriter bw = Files.newBufferedWriter(out, Charset.forName(charsetName))) {

            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || !dataRow.matcher(line).matches()) continue;

                String[] f = line.split("\",\"", -1);
                if (f.length != 9) continue;
                for (int i = 0; i < f.length; i++) f[i] = f[i].replaceAll("^\"|\"$", "").trim();

                if (!natureOk.matcher(f[2]).find()) continue;

                // 列 6 定位“学期”并删除其后所有内容
                f[5] = extractSemester(f[5]);

                // 其余列通用清洗
                for (int i = 0; i < f.length; i++) {
                    if (i == 5) continue;
                    if ("null".equals(f[i])) continue;
                    f[i] = cleanCell(f[i]);
                }

                // 数值列格式化
                f[3] = formatCredit(f[3]);
                f[4] = formatCredit(f[4]);

                bw.write(String.join(",", f));
                bw.newLine();
            }
        }
    }

    private static String extractSemester(String s) {
        if ("null".equals(s)) return "null";
        return s.replaceAll("学期.*$", "学期");   // 保留“学期”并删除后面所有内容
    }

    private static String cleanCell(String cell) {
        if ("null".equals(cell)) return "null";
        String s = cell.replaceAll("（[^）]*）$", ""); // 去中文括号段
        s = s.replaceAll("(?i)[a-z].*$", "");       // 去英文及之后
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
}