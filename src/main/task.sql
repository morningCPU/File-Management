-- 创建任务信息表
CREATE TABLE IF NOT EXISTS `evaluation_task` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '任务ID',
  `task_name` VARCHAR(255) NOT NULL COMMENT '任务名称',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `creator` VARCHAR(100) NOT NULL COMMENT '创建者',
  `status` VARCHAR(50) NOT NULL DEFAULT 'pending' COMMENT '任务状态：pending-未开始, in_progress-进行中, completed-已完成',
  `description` TEXT COMMENT '任务描述',
  `end_time` DATETIME COMMENT '任务结束时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价任务表';

-- 创建任务-教师关联表
CREATE TABLE IF NOT EXISTS `task_teacher` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `task_id` BIGINT NOT NULL COMMENT '任务ID',
  `course_code` VARCHAR(100) NOT NULL COMMENT '课程代码',
  `course_name` VARCHAR(255) NOT NULL COMMENT '课程名称',
  `teacher_name` VARCHAR(100) NOT NULL COMMENT '教师姓名',
  `title` VARCHAR(100) COMMENT '教师职称',
  `department` VARCHAR(255) COMMENT '开课部门',
  `nature` VARCHAR(100) COMMENT '课程性质',
  `evaluation_status` VARCHAR(50) NOT NULL DEFAULT 'pending' COMMENT '评价状态：pending-未评价, completed-已完成',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  
  FOREIGN KEY (`task_id`) REFERENCES `evaluation_task`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='任务教师关联表';

-- 创建索引以提高查询性能
CREATE INDEX idx_task_id ON task_teacher(task_id);
CREATE INDEX idx_teacher_name ON task_teacher(teacher_name);
CREATE INDEX idx_course_code ON task_teacher(course_code);
CREATE INDEX idx_evaluation_status ON task_teacher(evaluation_status);