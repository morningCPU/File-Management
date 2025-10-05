import com.morning.database.CsvToMySQL;
import com.morning.database.CreateTable;
import com.morning.tools.CleanCsv;
import com.morning.tools.ConvertEncoding;
import com.morning.tools.ReadWord;
import com.morning.tools.SortCsv;

import java.io.*;
import java.util.Properties;

public class ReadTest {
    public static void main(String[] args){
        String docxFolder = "src/main/TestExample/";
        String resultFolder = "src/main/TestExample/result/";
        String dbPath = "src/main/resources/db.properties";

        // 加载数据库配置
        Properties props = new Properties();
        try (InputStream in = new FileInputStream(dbPath)) {
            props.load(in);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        String charset = props.getProperty("db.charset", "utf8mb4");

        // 获取所有 Word 文件
        File folder = new File(docxFolder);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".docx"));
        if (files == null || files.length == 0) {
            System.out.println("未找到 word 文件：" + docxFolder);
            return;
        }

        // 遍历文件
        for (File file : files) {
            String name = file.getName().replace(".docx", "");
            System.out.println("\n处理文件: " + name);

            // 创建独立的结果文件夹
            File subResultFolder = new File(resultFolder, name);
            if (!subResultFolder.exists()) {
                boolean created = subResultFolder.mkdirs();
                if (!created) {
                    System.err.println("无法创建结果文件夹：" + subResultFolder.getAbsolutePath());
                    continue;
                }
            }

            // 自动建表
            CreateTable.createTable(url, user, password, charset, file.getName());

            // 构建路径
            String txtPath = new File(subResultFolder, name + ".txt").getAbsolutePath();
            String csvPath = new File(subResultFolder, name + ".csv").getAbsolutePath();
            String csvPathClean = new File(subResultFolder, name + "_clean.csv").getAbsolutePath();
            String csvPathSorted = new File(subResultFolder, name + "_sorted.csv").getAbsolutePath();
            String csvPathUtf = new File(subResultFolder, name + "_utf.csv").getAbsolutePath();

            try {
                // 读取 Word
                ReadWord.readWord(file.getAbsolutePath(), txtPath, csvPath);

                // 清洗
                CleanCsv.cleanCsv(csvPath, csvPathClean, "gbk");

                // 排序
                SortCsv.sortCsv(csvPathClean, csvPathSorted, "gbk");

                // 转码
                ConvertEncoding.convert(csvPathSorted, "gbk", csvPathUtf, "utf-8");

                // 导入数据库
                CsvToMySQL.loadCsvToMySQL(csvPathUtf, url, user, password, charset);

                System.out.println("文件 " + name + " 处理完成");
            } catch (Exception e) {
                System.err.println("文件 " + name + " 处理失败: " + e.getMessage());
            }
        }

        System.out.println("\n全部文件处理完成");
    }
}
