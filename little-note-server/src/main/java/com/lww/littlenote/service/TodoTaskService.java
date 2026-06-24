package com.lww.littlenote.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.littlenote.entity.TodoTask;
import com.lww.littlenote.req.TodoTaskCompletionRecordUpdateReq;
import com.lww.littlenote.req.TodoTaskQueryReq;
import com.lww.littlenote.req.TodoTaskReq;
import com.lww.littlenote.vo.DayViewVO;
import com.lww.littlenote.vo.MonthViewVO;
import com.lww.littlenote.vo.TaskStatsVO;
import com.lww.littlenote.vo.TodoTaskCompletionRecordVO;
import com.lww.littlenote.vo.TodoTaskCountVO;
import com.lww.littlenote.vo.TodoTaskVo;
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
    IPage<TodoTaskVo> listTasks(TodoTaskQueryReq todoTaskQueryReq);

    /**
     * 查询当前用户的任务详情
     *
     * @param taskId 任务ID
     * @return 任务详情
     */
    TodoTaskVo getTask(Long taskId);

    /**
     * 新增当前用户的任务
     *
     * @param taskReq 任务参数
     * @return 新增后的任务
     */
    TodoTaskVo addTask(TodoTaskReq taskReq);

    /**
     * 编辑当前用户的任务
     *
     * @param taskId  任务ID
     * @param taskReq 任务参数
     * @return 编辑后的任务
     */
    TodoTaskVo editTask(Long taskId, TodoTaskReq taskReq);

    /**
     * 完成任务一次
     *
     * @param taskId 任务ID
     */
    void completeTaskOnce(Long taskId);

    void deleteTask(Long taskId);

    List<TodoTaskCompletionRecordVO> listTaskCompletionRecords(Long taskId);

    void updateTaskCompletionRecord(TodoTaskCompletionRecordUpdateReq req);

    /**
     * 获取用户任务统计
     *
     * @param category 任务类别
     * @return 统计信息
     */
    TaskStatsVO getTaskStats(String category);

    TodoTaskCountVO getTaskCounts();

    /**
     * 日视图
     *
     * @param date 日期，格式 yyyy-MM-dd
     * @return 日视图数据
     */
    DayViewVO getDayView(String date);

    /**
     * 周视图
     *
     * @param year 年份
     * @param week ISO周数
     * @return 周视图数据
     */
    WeekViewVO getWeekView(Integer year, Integer week);

    /**
     * 月视图
     *
     * @param year  年份
     * @param month 月份
     * @return 月视图数据
     */
    MonthViewVO getMonthView(Integer year, Integer month);

    MonthViewVO getMonthViewWithSync(Integer year, Integer month, Long userId);
}
