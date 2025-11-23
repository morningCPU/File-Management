package com.morning.v2.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.morning.v2.mapper")
public class MyBatisConfig {
    // MyBatis配置类，主要用于指定Mapper接口的扫描路径
    // 数据源配置已在application.properties中指定
}
