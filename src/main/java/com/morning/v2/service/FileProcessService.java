package com.morning.v2.service;

import com.morning.v2.common.GetCsvFilesUtil;
import com.morning.v2.common.GetCourseList;
import com.morning.v2.pojo.Course;
import com.morning.v2.service.CourseService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileProcessService {

    private final String WORD_DIR;
    private final String CSV_DIR;

    public FileProcessService() {
        String resourcesPath;
        try {
            // 获取resources目录的绝对路径
            resourcesPath = new ClassPathResource("").getFile().getAbsolutePath();
        } catch (IOException e) {
            // 如果获取ClassPathResource失败，使用当前工作目录
            resourcesPath = System.getProperty("user.dir") + "/src/main/resources";
            System.err.println("警告: 无法获取ClassPathResource，使用备用路径: " + resourcesPath);
        }
        
        WORD_DIR = resourcesPath + "/word";
        CSV_DIR = resourcesPath + "/csv";

        // 确保目录存在
        new File(WORD_DIR).mkdirs();
        new File(CSV_DIR).mkdirs();
    }

    /**
     * 保存上传的文件到指定目录
     */
    public void saveFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("上传的文件不能为空");
        }

        String originalFileName = file.getOriginalFilename();
        Path wordFilePath = Paths.get(WORD_DIR, originalFileName);
        Files.write(wordFilePath, file.getBytes());
    }

    /**
     * 调用GetCsvFilesUtil进行文件转换
     */
    public void convertFiles() {
        GetCsvFilesUtil.getCSV(WORD_DIR, CSV_DIR);
    }

    /**
     * 处理生成的CSV文件，仅保存课程数据
     */
    public Map<String, Integer> processCSVFiles(CourseService courseService) throws IOException {
        Map<String, Integer> result = new HashMap<>();
        int totalCourses = 0;

        // 直接调用GetCourseList.standardizeDirectory方法处理CSV目录中的所有文件
        List<Course> courses = GetCourseList.standardizeDirectory(CSV_DIR, "UTF-8");
        
        // 保存课程数据
        courseService.saveCourses(courses);
        totalCourses = courses.size();

        result.put("courses", totalCourses);
        // 不需要处理教师数据
        return result;
    }

    /**
     * 处理上传的Word文件，转换为CSV（保留用于向后兼容）
     */
    public String processWordFile(MultipartFile file) throws IOException {
        saveFile(file);
        convertFiles();
        
        String originalFileName = file.getOriginalFilename();
        String csvFileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + ".csv";
        return CSV_DIR + "/" + csvFileName;
    }

    /**
     * 清理临时文件
     */
    public void cleanTempFiles() {
        // 由于文件已经保存到resources目录，不需要清理原始文件
        // 这里可以根据需要添加其他清理逻辑
    }
}
