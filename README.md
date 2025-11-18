# v2

## 1. 技术栈
1. 持久层          
MyBatis 3.5.15 + MySQL 8.3.0
2. Office 处理     
Apache POI 5.2.5
3. HTML 解析	
Jsoup 1.17.2
4. 日志 
log4j2 2.23.1
5. 工具	
Lombok 1.18.34
6. 测试	
JUnit 4.13.1

***

## 2. 项目结构
```text
|- readword
    |- src 源码
        |- main
            |- java
                |- com.morning.v2
                    |- common 项目工具
                        |- GetCourseList 获取课程列表
                        |- GetCscFilesUtil 从word读取表格到csv(项目个性化处理)
                        |- GetTeacherList 获取教师列表
                    |- Util 通用工具
                        |- ReadWordToCsvUtil 从word读取表格到csv
                    |- mapper 映射
                    |- pojo   实体
            |- resource
                |- com.morning,v2.mapper 数据库操作
                |- jdbc.properties jdbc配置
                |- log4j.xml log4j日志配置
                |- mybatis-config.xml mybatis配置
            |- TestExample 测试样本文件
            |- course.sql  创建数据库sql
    |- test 测试
        |- java.com.morning.v2
            |- commontest
            |- utiltest
            |- TestReadTeacher 测试获取教师列表
            |- TestReadWord    测试读取word
```

***

## 3. 使用
1. 创建数据库
使用 course.sql 创建数据库
创建的数据库名为 course ，注意不要与现有数据库重复
2. 修改配置文件
```text
jdbc.driver=com.mysql.cj.jdbc.Driver
jdbc.url=jdbc:mysql://localhost:3306/course
jdbc.username=root
jdbc.password=password
```
将数据库密码改为自己的密码