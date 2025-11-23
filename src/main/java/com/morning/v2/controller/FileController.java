package com.morning.v2.controller;

import com.morning.v2.service.FileProcessService;
import com.morning.v2.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileProcessService fileProcessService;

    @Autowired
    private CourseService courseService;

    /**
     * 上传多个Word文件并处理
     */
    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        Map<String, Object> response = new HashMap<>();
        try {
            int processedFiles = 0;
            
            // 先保存所有上传的文件
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    // 只保存文件，不进行转换
                    fileProcessService.saveFile(file);
                    processedFiles++;
                }
            }
            
            // 一次性调用GetCsvFilesUtil进行转换
            fileProcessService.convertFiles();
            
            // 处理生成的CSV数据
            Map<String, Integer> processedCounts = fileProcessService.processCSVFiles(courseService);

            System.out.println(1);
            
            // 清理临时文件
            fileProcessService.cleanTempFiles();
            
            response.put("success", true);
            response.put("message", "文件处理成功，共处理" + processedFiles + "个文件");
            response.put("courses", processedCounts.getOrDefault("courses", 0));
            response.put("processedFiles", processedFiles);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "文件处理失败: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
