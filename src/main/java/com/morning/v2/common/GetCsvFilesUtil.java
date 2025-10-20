package com.morning.v2.common;
import com.morning.v2.Util.ReadWordToCsvUtil;

import java.io.File;

public class GetCsvFilesUtil {
   public static void getCSV(String docxFolderUrl,String resultFolderUrl){
       File resultFolder = new File(resultFolderUrl);
       if (!resultFolder.exists()) {
           boolean created = resultFolder.mkdirs();
           if (!created) {
               System.err.println("无法创建结果文件夹：" + resultFolder.getAbsolutePath());
               return;
           }
       }

       // 获取所有 Word 文件
       File folder = new File(docxFolderUrl);
       File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".docx"));
       if (files == null || files.length == 0) {
           System.out.println("未找到 word 文件：" + docxFolderUrl);
           return;
       }

       // 遍历文件
       for (File file : files) {
           String name = file.getName().replace(".docx", "");
           System.out.println("\n处理文件: " + name);

           // 构建路径
           String csvPath = new File(resultFolder, name + ".csv").getAbsolutePath();

           try {
               // 读取 Word
               ReadWordToCsvUtil.readWord(file.getAbsolutePath(),csvPath);
               System.out.println("文件 " + name + " 处理完成");
           } catch (Exception e) {
               System.err.println("文件 " + name + " 处理失败: " + e.getMessage());
           }
       }
   }
}
