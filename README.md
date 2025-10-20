# 写了com.morning.v2

# 使用
1. 用main文件夹下面的course.sql建库
2. resources/db.properties改动
```text
driver=com.mysql.cj.jdbc.Driver
url=jdbc:mysql://localhost:3306/course?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai
user=root
password=
```
3. main/java/com/morning/v2/test/TestReadWord是整体测试

# 相关文件介绍
```text
common 项目工具
    |- GetCourseList 从csv文件获取对象List
    |- GetCsvFileUtil 获取csv文件
dao 数据库操作
pojo 实体
test 测试
Util 通用工具
    |- DBUtil 数据库连接
    |- ReadWordToCsv 读取word
```