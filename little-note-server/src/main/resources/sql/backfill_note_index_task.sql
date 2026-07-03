-- 将 note 表中已有笔记补入 ES 索引任务表，供 NoteIndexTaskServiceImpl#processPendingTasks 扫描处理。
-- UPDATE/CREATE 对现有笔记都会执行 saveOrUpdateIndex，这里使用 UPDATE 表示刷新或补建已有笔记索引。
INSERT INTO `note_index_task` (
    `note_id`,
    `action`,
    `status`,
    `retry_count`,
    `error_msg`,
    `create_time`,
    `update_time`
)
SELECT
    n.`id`,
    'UPDATE',
    'WAIT',
    0,
    NULL,
    NOW(),
    NOW()
FROM `note` n
WHERE NOT EXISTS (
    SELECT 1
    FROM `note_index_task` t
    WHERE t.`note_id` = n.`id`
      AND t.`action` IN ('CREATE', 'UPDATE')
      AND t.`status` IN ('WAIT', 'FAIL')
      AND t.`retry_count` < 5
);
