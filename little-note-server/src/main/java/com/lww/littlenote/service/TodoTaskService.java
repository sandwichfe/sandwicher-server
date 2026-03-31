package com.lww.littlenote.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.littlenote.entity.TodoTask;
import com.lww.littlenote.req.TodoTaskQueryReq;
import com.lww.littlenote.vo.DayViewVO;
import com.lww.littlenote.vo.MonthViewVO;
import com.lww.littlenote.vo.TaskStatsVO;
import com.lww.littlenote.vo.TodoTaskCompletionRecordVO;
import com.lww.littlenote.vo.TodoTaskCountVO;
import com.lww.littlenote.vo.WeekViewVO;

import java.util.List;

/**
 * <p>
 * 统一任务表 服务类
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
public interface TodoTaskService extends IService<TodoTask> {

    /**
     * 分页查询任务列表
     *
     * @param todoTaskQueryReq 查询条件
     * @return 分页结果
     */
    Page<TodoTask> listTasks(TodoTaskQueryReq todoTaskQueryReq);

    /**
     * 完成任务一次
     *
     * @param taskId 任务ID
     * @param userId 用户ID
     * @return 是否成功
     */
    void completeTaskOnce(Long taskId, Long userId);

    void deleteTask(Long taskId, Long userId);

    List<TodoTaskCompletionRecordVO> listTaskCompletionRecords(Long taskId, Long userId);

    /**
     * 获取用户任务统计
     *
     * @param userId 用户ID
     * @param category 任务类别
     * @return 统计信息
     */
    TaskStatsVO getTaskStats(Long userId, String category);

    TodoTaskCountVO getTaskCounts(Long userId);

    /**
     * 日视图
     *
     * @param date   日期，格式 yyyy-MM-dd
     * @param userId 用户ID
     * @return 日视图数据
     */
    DayViewVO getDayView(String date, Long userId);

    /**
     * 周视图
     *
     * @param year   年份
     * @param week   ISO周数
     * @param userId 用户ID
     * @return 周视图数据
     */
    WeekViewVO getWeekView(Integer year, Integer week, Long userId);

    /**
     * 月视图
     *
     * @param year   年份
     * @param month  月份
     * @param userId 用户ID
     * @return 月视图数据
     */
    MonthViewVO getMonthView(Integer year, Integer month, Long userId);
}
