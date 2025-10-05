package com.morning.tools;

import java.io.*;
import java.nio.charset.Charset;
import java.text.Collator;
import java.util.*;

public class SortCsv {

    public static void sortCsv(String inputPath, String outputPath, String charset) {
        List<String[]> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), Charset.forName(charset)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    rows.add(line.split(",", -1)); // 保留空列
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        // 中文拼音比较器
        Collator collator = Collator.getInstance(Locale.CHINA);

        // 定义第三列优先级
        Map<String, Integer> orderMap = new HashMap<>();
        orderMap.put("必修", 1);
        orderMap.put("限修", 2);
        orderMap.put("选修", 3);

        // 排序规则
        rows.sort((a, b) -> {
            // 1. 第三列比较
            int orderA = orderMap.getOrDefault(a[2].trim(), 99);
            int orderB = orderMap.getOrDefault(b[2].trim(), 99);
            if (orderA != orderB) {
                return Integer.compare(orderA, orderB);
            }

            // 2. 第六列第一个数字比较
            int numA = extractFirstNumber(a.length > 5 ? a[5] : "");
            int numB = extractFirstNumber(b.length > 5 ? b[5] : "");
            if (numA != numB) {
                return Integer.compare(numA, numB);
            }

            // 3. 第一列按拼音比较
            return collator.compare(a[0], b[0]);
        });

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), Charset.forName(charset)))) {
            for (String[] row : rows) {
                writer.write(String.join(",", row));
                writer.newLine();
            }
            System.out.println("4. 排序完成: " + outputPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 提取字符串中的第一个数字
    private static int extractFirstNumber(String text) {
        if (text == null) return Integer.MAX_VALUE;
        String num = text.replaceAll("[^0-9]", " ").trim();
        if (num.isEmpty()) return Integer.MAX_VALUE;
        try {
            return Integer.parseInt(num.split(" ")[0]);
        } catch (Exception e) {
            return Integer.MAX_VALUE;
        }
    }
}
