package com.morning.v2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot 主应用类
 * 作为应用程序的入口点
 */
@SpringBootApplication
@MapperScan("com.morning.v2.mapper") // 扫描MyBatis的Mapper接口
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}