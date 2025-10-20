package com.morning.v1.tools;

import java.io.*;
import java.nio.charset.Charset;

public class ConvertEncoding {

     // 将 CSV 从 GBK 转为 UTF-8
    public static void convert(String inputPath, String org,String outputPath,String obj) {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputPath), Charset.forName(org)));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), Charset.forName(obj)))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
            }
            System.out.println("5. 转换完成: " + outputPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
