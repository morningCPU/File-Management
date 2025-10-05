package com.morning.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CleanCsv {

    public static void cleanCsv(String inputPath, String outputPath, String charsetName) {
        try (
                BufferedReader br = new BufferedReader(
                        new InputStreamReader(new FileInputStream(inputPath), charsetName)
                );
                BufferedWriter bw = new BufferedWriter(
                        new OutputStreamWriter(new FileOutputStream(outputPath), charsetName)
                )
        ) {
            String line;
            while ((line = br.readLine()) != null) {
                // 去除空行
                if (line.trim().isEmpty()) continue;

                // 清理行内容
                String processedLine = processLine(line);

                // 分割 CSV 行
                String[] columns = processedLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                if (columns.length >= 3) {
                    String firstColumn = columns[0].trim().replaceAll("\"", "");
                    String thirdColumn = columns[2].trim().replaceAll("\"", "");

                    // 过滤条件：
                    // 1. 第一列不包含“第二课堂”
                    // 2. 第三列为 “必修” 或 “限修”
                    if (!firstColumn.contains("第二课堂") &&
                            (thirdColumn.equals("必修") || thirdColumn.equals("限修"))) {
                        bw.write(processedLine);
                        bw.newLine();
                    }
                }
            }
            System.out.println("3. CSV 文件清洗完成: " + outputPath);
        } catch (IOException e) {
            System.err.println("处理文件时发生 I/O 错误: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("发生未知错误: " + e.getMessage());
        }
    }

    private static String processLine(String line) {
        Pattern pattern = Pattern.compile("(\"([^\"]*(?:\"\"[^\"]*)*)\"|[^,]+)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(line);

        StringBuilder newLine = new StringBuilder();
        boolean firstColumn = true;

        while (matcher.find()) {
            String column = matcher.group(0);
            String contentToClean = column;

            if (column.startsWith("\"") && column.endsWith("\"")) {
                contentToClean = column.substring(1, column.length() - 1).replace("\"\"", "\"");
            }

            String cleanedContent = cleanEnglishPart(contentToClean);

            // 去掉所有不必要的引号
            cleanedContent = cleanedContent.replace("\"", "").trim();

            // 只有内容含逗号才加双引号
            String finalColumn = cleanedContent.contains(",") ? "\"" + cleanedContent + "\"" : cleanedContent;

            if (!firstColumn) newLine.append(",");
            newLine.append(finalColumn);
            firstColumn = false;
        }

        return newLine.toString();
    }

    private static String cleanEnglishPart(String content) {
        String cleaned = content.trim();

        // 去掉换行符后的英文部分
        if (cleaned.contains("\n")) {
            cleaned = cleaned.split("\n")[0].trim();
        }

        // 正则保留中文、数字、罗马数字、斜杠、学分、逗号、空格、括号和破折号
        Pattern p = Pattern.compile("^([\\u4e00-\\u9fa50-9IVX/，,学分\\s\\(\\)（）\\-—]+)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(cleaned);

        String result;
        if (m.find()) {
            result = m.group(1).trim();
        } else {
            result = cleaned;
        }

        // 去掉所有中英文引号
        result = result.replaceAll("[\"“”]", "").trim();

        // 移除末尾多余空格或斜杠
        result = result.replaceAll("[\\s/]+$", "").trim();

        return result;
    }
}
