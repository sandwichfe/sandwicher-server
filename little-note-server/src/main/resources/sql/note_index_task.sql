CREATE TABLE `note_index_task` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '任务ID',
  `note_id` bigint NOT NULL COMMENT '笔记ID',
  `action` varchar(20) NOT NULL COMMENT '操作类型：CREATE / UPDATE / DELETE',
  `status` varchar(20) NOT NULL COMMENT '任务状态：WAIT / SUCCESS / FAIL',
  `retry_count` int NOT NULL DEFAULT 0 COMMENT '重试次数',
  `error_msg` varchar(500) DEFAULT NULL COMMENT '最近一次失败原因',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_note_index_task_status_retry` (`status`, `retry_count`, `create_time`),
  KEY `idx_note_index_task_note_id` (`note_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='笔记ES索引同步任务表';
