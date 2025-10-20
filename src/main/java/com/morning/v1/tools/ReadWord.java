package com.morning.v1.tools;

import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTVMerge;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ReadWord {

    public static void readWord(String filePath, String txtPath, String csvPath) {
        File f = new File(filePath);
        if (!f.exists()){
            System.out.println("文件不存在: " + filePath);
            return ;
        }

        try (FileInputStream fis = new FileInputStream(f)) {
            if (filePath.toLowerCase().endsWith(".docx")) {
                try (XWPFDocument doc = new XWPFDocument(fis)) {
                    List<XWPFTable> tables = doc.getTables();
                    if (tables.isEmpty()){
                        System.out.println("文档中没有表格");
                        return ;
                    }

                    // 筛选表格范围
                    int startIndex = -1;
                    for (int i = 0; i < tables.size(); i++) {
                        XWPFTable t = tables.get(i);
                        if (!t.getRows().isEmpty()) {
                            XWPFTableRow firstRow = t.getRow(0);
                            if (firstRow != null && !firstRow.getTableCells().isEmpty()) {
                                String firstText = firstRow.getCell(0).getText().trim();
                                if (firstText.contains("公共基础课程")) {  // 使用 contains 判断
                                    startIndex = i;
                                    break;
                                }
                            }
                        }
                    }

                    if (startIndex == -1) {
                        System.out.println("未找到以“公共基础课程”开头的表格");
                        return ;
                    }
                    // 从该表开始到最后一个表合并处理
                    List<XWPFTable> targetTables = new ArrayList<>(tables.subList(startIndex, tables.size()));

                    // 将这些表按顺序合并为一个表矩阵
                    List<XWPFTableRow> allRows = new ArrayList<>();
                    for (XWPFTable t : targetTables) {
                        allRows.addAll(t.getRows());
                    }

                    int rows = allRows.size();

                    // 计算最大列数
                    int maxCols = 0;
                    for (XWPFTableRow row : allRows) {
                        int colCount = 0;
                        for (XWPFTableCell cell : row.getTableCells()) {
                            int span = getGridSpan(cell);
                            colCount += span;
                        }
                        if (colCount > maxCols) maxCols = colCount;
                    }

                    String[][] matrix = new String[rows][maxCols];
                    STMerge.Enum[][] vMerge = new STMerge.Enum[rows][maxCols];

                    // 填入矩阵，并记录 vMerge
                    for (int r = 0; r < rows; r++) {
                        XWPFTableRow row = allRows.get(r);
                        List<XWPFTableCell> cells = row.getTableCells();

                        int colIndex = 0;
                        for (XWPFTableCell cell : cells) {
                            while (colIndex < maxCols && matrix[r][colIndex] != null) colIndex++;

                            int span = getGridSpan(cell);
                            String text = getCellText(cell);
                            if (text != null) text = text.trim();
                            if (text != null && text.isEmpty()) text = null;

                            for (int k = 0; k < span && (colIndex + k) < maxCols; k++) {
                                matrix[r][colIndex + k] = text;
                            }

                            STMerge.Enum vm = getVMergeM(cell);
                            for (int k = 0; k < span && (colIndex + k) < maxCols; k++) {
                                vMerge[r][colIndex + k] = vm;
                            }

                            colIndex += span;
                        }
                    }

                    // 按 vMerge 回填
                    for (int c = 0; c < maxCols; c++) {
                        String lastVal = null;
                        for (int r = 0; r < rows; r++) {
                            STMerge.Enum vm = vMerge[r][c];
                            if (vm == STMerge.RESTART) {
                                lastVal = matrix[r][c];
                            } else if (vm == STMerge.CONTINUE) {
                                if (lastVal != null) {
                                    matrix[r][c] = lastVal;
                                }
                            } else {
                                lastVal = matrix[r][c];
                            }
                        }
                    }

                    // 未填的设为 "null"，同时覆盖空字符串
                    for (int r = 0; r < rows; r++) {
                        for (int c = 0; c < maxCols; c++) {
                            if (matrix[r][c] == null || matrix[r][c].trim().isEmpty()) {
                                matrix[r][c] = "null";
                            }
                        }
                    }

                    // 输出 CSV（GBK）
                    try (BufferedWriter csvWriter = new BufferedWriter(
                            new OutputStreamWriter(new FileOutputStream(csvPath), StandardCharsets.UTF_8))) {

                        for (int r = 0; r < rows; r++) {
                            StringBuilder rowBuilder = new StringBuilder();

                            for (int c = 0; c < maxCols; c++) {
                                String cellVal = matrix[r][c];
                                if (cellVal == null) cellVal = ""; // 防止 null 指针

                                // 转义双引号 -> 两个双引号，并去掉换行符
                                String escaped = cellVal
                                        .replace("\"", "\"\"")
                                        .replace("\r", "")
                                        .replace("\n", "");

                                // 每个字段都统一用双引号包起来
                                rowBuilder.append("\"").append(escaped).append("\"");

                                // 列之间加逗号
                                if (c < maxCols - 1) rowBuilder.append(",");
                            }

                            csvWriter.write(rowBuilder.toString());
                            csvWriter.newLine();
                        }
                    }


                    System.out.println("2 输出 CSV 完成 : " + csvPath);
                    return ;
                }
            } else {
                System.out.println("不支持的文件格式");
                return ;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // 读取单元格文字
    private static String getCellText(XWPFTableCell cell) {
        if (cell == null) return null;
        StringBuilder sb = new StringBuilder();
        for (XWPFParagraph p : cell.getParagraphs()) {
            String t = p.getText();
            if (t != null && !t.trim().isEmpty()) {
                if (!sb.isEmpty()) sb.append("\n");
                sb.append(t);
            }
        }
        return sb.isEmpty() ? null : sb.toString();
    }

    // gridSpan 宽度
    private static int getGridSpan(XWPFTableCell cell) {
        if (cell == null) return 1;
        try {
            if (cell.getCTTc() != null && cell.getCTTc().getTcPr() != null && cell.getCTTc().getTcPr().isSetGridSpan()) {
                return cell.getCTTc().getTcPr().getGridSpan().getVal().intValue();
            }
        } catch (Exception ignored) {
        }
        return 1;
    }

    // vMerge 判断，返回 STMerge.Enum
    private static STMerge.Enum getVMergeM(XWPFTableCell cell) {
        if (cell == null || cell.getCTTc() == null || cell.getCTTc().getTcPr() == null) return null;

        CTVMerge vmObj = cell.getCTTc().getTcPr().getVMerge();
        if (vmObj == null) return null;

        if (vmObj.isSetVal()) {
            return vmObj.getVal(); // STMerge.Enum
        }
        return STMerge.CONTINUE;
    }
}
