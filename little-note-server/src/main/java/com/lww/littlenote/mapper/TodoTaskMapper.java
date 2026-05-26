package com.lww.littlenote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lww.littlenote.entity.TodoTask;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 统一任务表 Mapper 接口
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Mapper
public interface TodoTaskMapper extends BaseMapper<TodoTask> {

    @Update("""
            UPDATE todo_task t
            LEFT JOIN (
                SELECT
                    task_id,
                    COUNT(*) AS completed_count,
                    MAX(completed_at) AS last_complete_time
                FROM todo_task_completion_record
                WHERE user_id = #{userId}
                GROUP BY task_id
            ) r ON t.id = r.task_id
            SET
                t.completed_count = IFNULL(r.completed_count, 0),
                t.last_complete_time = r.last_complete_time,
                t.status = CASE
                    WHEN IFNULL(r.completed_count, 0) >= IFNULL(t.target_count, 1) THEN 1
                    ELSE 0
                END,
                t.update_time = NOW(),
                t.update_by = #{userId}
            WHERE t.user_id = #{userId}
              AND DATE(t.deadline) BETWEEN #{startDate} AND #{endDate}
            """)
    int syncTaskCompletionStats(@Param("userId") Long userId,
                                @Param("startDate") String startDate,
                                @Param("endDate") String endDate);

}
