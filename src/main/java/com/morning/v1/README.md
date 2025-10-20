# 提取课程

## 1. 功能
根据.docx文档提取培养计划课程

***

## 2. 思路
找到表格中第一行包含"公共基础课程"的表格，从这张表格到最后就是需要的表格数据
将word表格内容读取转换为csv文件，对数据进行处理后放入mysql数据库中

***

## 3. 技术依赖
1. java
2. maven
3. apache.poi
4. mysql

## 4. 项目结构
```txt
|- src
    |- main
        |- test 测试部分
            |- com.morning.test.ReadTest.java 读word
        |- java 代码部分
            |- com.morning
                |- database 数据库操作
                    |- CreateTable 创建数据库表
                    |- CsvToMySQL 从csv文件中存入mysql
                |- tools 文件操作工具
                    |- ReadWord 读取word
                    |- CleanCsv 数据清洗
                    |- SortCsv 数据初步排序
                    |- ConverEncoding 编码转换
        |- resources 
            |- db.properties 数据库配置文件
        |- TestExample 测试文件
            |- result 结果
                |- .csv 读取word结果
                |- .txt 相应的txt
                |- _clean.csv 清洗结果
                |- _sorted.csv 排序结果
                |- _utf.csv 编码结果
            |- 测试文件
|- .gitignore git文件管理
|- pom.xml maven配置
|- README.md 文档
```

***

## 5. 使用步骤
1. 将word放入TestExample文件夹下
2. 在mysql中创建数据库
3. 配置 db.properties
```properties
db.url=jdbc:mysql://localhost:3306/创建的数据库名?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8
db.user=root
db.password=密码
db.charset=utf8mb4
```
4. 运行ReadTest.java

***

## 6. 详细解释
### 6.1 数据清洗的操作
过滤条件：
1. 第一列不包含“第二课堂” (不知道什么好乱，我就去掉了)
2. 第三列为 “必修” 或 “限修” 
3. 除去了英文(加上英文太长了,清洗效果有些内容不好)
### 6.2 编码转换
win里面excel打开csv默认是gbk编码,不好改,为了方便调试输出的文件会是gbk编码的
但是mysql最好用utf编码存储,所以转换一下
### 6.3 简单排序
1. 第三列排序(必修>限修)
2. 第六列第一个数字比较(学期)
3. 第一列按拼音比较(课程)
+ 只是简单排序为了有一个基本的样子,传入数据库在数据库中就可以查询排序

***

## 7. 改进
1. 清洗有去除不彻底和清除不需要清除的内容的情况
2. 数据库简单