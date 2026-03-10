package com.lww.littlenote.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.exception.AppException;
import com.lww.littlenote.entity.TodoTask;
import com.lww.littlenote.mapper.TodoTaskMapper;
import com.lww.littlenote.req.TodoTaskQueryReq;
import com.lww.littlenote.service.TodoTaskService;
import com.lww.littlenote.vo.DayViewVO;
import com.lww.littlenote.vo.MonthViewVO;
import com.lww.littlenote.vo.TaskStatsVO;
import com.lww.littlenote.vo.TodoTaskVo;
import com.lww.littlenote.vo.WeekViewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 统一任务表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Service
@RequiredArgsConstructor
public class TodoTaskServiceImpl extends ServiceImpl<TodoTaskMapper, TodoTask> implements TodoTaskService {

    @Override
    public Page<TodoTask> listTasks(TodoTaskQueryReq todoTaskQueryReq) {
        Page<TodoTask> page = new Page<>(todoTaskQueryReq.getPageNum(), todoTaskQueryReq.getPageSize());
        LambdaQueryWrapper<TodoTask> queryWrapper = new LambdaQueryWrapper<>();
        
        queryWrapper.eq(TodoTask::getUserId, todoTaskQueryReq.getUserId())
                .eq(StringUtils.hasText(todoTaskQueryReq.getTaskType()), TodoTask::getTaskType, todoTaskQueryReq.getTaskType())
                .eq(TodoTask::getStatus, todoTaskQueryReq.getStatus())
                .orderByDesc(TodoTask::getCreateTime);
        
        return this.page(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void completeTaskOnce(Long taskId, Long userId) {
        TodoTask task = this.getById(taskId);
        if (task == null || !task.getUserId().equals(userId)) {
            throw new AppException("任务不存在或不属于该用户");
        }
        
        // 检查是否已完成
        if (Integer.valueOf(1).equals(task.getStatus())) {
            throw new AppException("任务已完成，不能重复完成");
        }

        // 检查每日限制
        if ( Integer.valueOf(1).equals(task.getIsDailyLimit())) {
            if (task.getLastCompleteTime() != null && 
                task.getLastCompleteTime().toLocalDate().isEqual(LocalDate.now())) {
                throw new AppException("今日已完成，不能重复完成");
            }
        }
        
        // 增加完成次数
        task.setCompletedCount(task.getCompletedCount() + 1);
        task.setLastCompleteTime(LocalDateTime.now());
        task.setUpdateTime(LocalDateTime.now());
        task.setUpdateBy(userId);

        // 判断是否全部完成，更新status
        if (task.getCompletedCount() >= task.getTargetCount()) {
            task.setStatus(1);
        }

        this.updateById(task);
    }


    @Override
    public TaskStatsVO getTaskStats(Long userId, String category) {
        LambdaQueryWrapper<TodoTask> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TodoTask::getUserId, userId);
        
        long totalTasks = this.count(queryWrapper);

        // 统计已完成的任务（status = 1）
        queryWrapper.eq(TodoTask::getStatus, 1);
        long completedTasks = this.count(queryWrapper);


        TaskStatsVO taskStatsVO = new TaskStatsVO(totalTasks, completedTasks);
        taskStatsVO.setTotalTasks(totalTasks);
        taskStatsVO.setCompletedTasks(completedTasks);
        taskStatsVO.setPendingTasks(totalTasks - completedTasks);
        taskStatsVO.setCompletionRate(totalTasks > 0 ? (double) completedTasks / totalTasks : 0.0);

        return taskStatsVO;
    }

    @Override
    public DayViewVO getDayView(String dateStr, Long userId) {
        LocalDate date = LocalDate.parse(dateStr);

        // 查询任务（按deadline日期匹配）
        LambdaQueryWrapper<TodoTask> globalWrapper = new LambdaQueryWrapper<>();
        globalWrapper.eq(TodoTask::getUserId, userId)
                .apply("DATE(deadline) = {0}", dateStr);
        List<TodoTask> globalTasks = this.list(globalWrapper);

        // 所有任务
        List<TodoTask> allTasks = new ArrayList<>(globalTasks);

        // 按deadline小时分组
        Map<String, List<TodoTaskVo>> timeMap = new LinkedHashMap<>();
        List<TodoTaskVo> unscheduled = new ArrayList<>();

        for (TodoTask task : allTasks) {
            TodoTaskVo vo = CustomBeanUtils.copyProperties(task, TodoTaskVo.class);
            if (task.getDeadline() != null) {
                String hour = String.format("%02d:00", task.getDeadline().getHour());
                timeMap.computeIfAbsent(hour, k -> new ArrayList<>()).add(vo);
            } else {
                unscheduled.add(vo);
            }
        }

        // 构建schedule（按时间排序）
        List<DayViewVO.TimeSlot> schedule = new ArrayList<>();
        timeMap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    DayViewVO.TimeSlot slot = new DayViewVO.TimeSlot();
                    slot.setTime(entry.getKey());
                    slot.setTasks(entry.getValue());
                    schedule.add(slot);
                });

        // 未排期任务放在末尾
        if (!unscheduled.isEmpty()) {
            DayViewVO.TimeSlot slot = new DayViewVO.TimeSlot();
            slot.setTime("未排期");
            slot.setTasks(unscheduled);
            schedule.add(slot);
        }

        // 构建响应
        DayViewVO result = new DayViewVO();
        result.setDate(dateStr);
        result.setWeekDay(getChineseWeekDay(date));
        result.setTaskTotal(allTasks.size());
        result.setCompletedTotal((int) allTasks.stream().filter(this::isTaskCompleted).count());
        result.setSchedule(schedule);

        return result;
    }

    @Override
    public WeekViewVO getWeekView(Integer year, Integer week, Long userId) {
        // 计算ISO周的起止日期
        String weekDateStr = String.format("%04d-W%02d-1", year, week);
        LocalDate startOfWeek = LocalDate.parse(weekDateStr, DateTimeFormatter.ISO_WEEK_DATE);
        LocalDate endOfWeek = startOfWeek.plusDays(6);

        // 查询该周所有任务
        List<TodoTask> allTasks = queryTasksInDateRange(userId, startOfWeek, endOfWeek);

        // 按日期分组
        Map<LocalDate, List<TodoTask>> tasksByDate = groupTasksByDate(allTasks);

        // 构建每天数据
        String[] weekLabels = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        List<WeekViewVO.DayItem> days = new ArrayList<>();
        int totalTasks = 0;
        int completedTotal = 0;

        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = startOfWeek.plusDays(i);
            List<TodoTask> tasksForDay = tasksByDate.getOrDefault(currentDate, Collections.emptyList());
            int completedCount = (int) tasksForDay.stream().filter(this::isTaskCompleted).count();

            WeekViewVO.DayItem dayItem = new WeekViewVO.DayItem();
            dayItem.setDate(currentDate.toString());
            dayItem.setLabel(weekLabels[i]);
            dayItem.setTaskCount(tasksForDay.size());
            dayItem.setCompletedCount(completedCount);
            dayItem.setTasks(tasksForDay.stream()
                    .map(t -> CustomBeanUtils.copyProperties(t, TodoTaskVo.class))
                    .collect(Collectors.toList()));
            days.add(dayItem);

            totalTasks += tasksForDay.size();
            completedTotal += completedCount;
        }

        // 构建响应
        WeekViewVO result = new WeekViewVO();
        result.setYear(year);
        result.setWeek(week);
        result.setWeekLabel(year + "年第" + week + "周");
        result.setDateRange(startOfWeek.format(DateTimeFormatter.ofPattern("MM-dd"))
                + " ~ " + endOfWeek.format(DateTimeFormatter.ofPattern("MM-dd")));
        result.setTaskTotal(totalTasks);
        result.setCompletedTotal(completedTotal);
        result.setDays(days);

        return result;
    }

    @Override
    public MonthViewVO getMonthView(Integer year, Integer month, Long userId) {
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        // 查询该月所有任务
        List<TodoTask> allTasks = queryTasksInDateRange(userId, startOfMonth, endOfMonth);

        // 按日期分组
        Map<LocalDate, List<TodoTask>> tasksByDate = groupTasksByDate(allTasks);

        // 构建每天数据
        List<MonthViewVO.DateItem> dates = new ArrayList<>();
        int totalTasks = 0;
        int completedTotal = 0;

        for (int day = 1; day <= startOfMonth.lengthOfMonth(); day++) {
            LocalDate currentDate = LocalDate.of(year, month, day);
            List<TodoTask> tasksForDay = tasksByDate.getOrDefault(currentDate, Collections.emptyList());
            int completedCount = (int) tasksForDay.stream().filter(this::isTaskCompleted).count();

            MonthViewVO.DateItem dateItem = new MonthViewVO.DateItem();
            dateItem.setDate(currentDate.toString());
            dateItem.setDay(day);
            dateItem.setWeekDay(getChineseShortWeekDay(currentDate));
            dateItem.setTaskCount(tasksForDay.size());
            dateItem.setCompletedCount(completedCount);
            dates.add(dateItem);

            totalTasks += tasksForDay.size();
            completedTotal += completedCount;
        }

        // 构建响应
        MonthViewVO result = new MonthViewVO();
        result.setYear(year);
        result.setMonth(month);
        result.setMonthLabel(year + "年" + month + "月");
        result.setTaskTotal(totalTasks);
        result.setCompletedTotal(completedTotal);
        result.setDates(dates);

        return result;
    }

    // ===== 视图辅助方法 =====

    private List<TodoTask> queryTasksInDateRange(Long userId, LocalDate startDate, LocalDate endDate) {

        LambdaQueryWrapper<TodoTask> globalWrapper = new LambdaQueryWrapper<>();
        globalWrapper.eq(TodoTask::getUserId, userId)
                .apply("DATE(deadline) BETWEEN {0} AND {1}", startDate.toString(), endDate.toString());
        return this.list(globalWrapper);
    }

    private Map<LocalDate, List<TodoTask>> groupTasksByDate(List<TodoTask> tasks) {
        Map<LocalDate, List<TodoTask>> result = new HashMap<>();
        for (TodoTask task : tasks) {
            LocalDate date = getTaskDate(task);
            if (date != null) {
                result.computeIfAbsent(date, k -> new ArrayList<>()).add(task);
            }
        }
        return result;
    }

    private LocalDate getTaskDate(TodoTask task) {
        return task.getDeadline() != null ? task.getDeadline().toLocalDate() : null;
    }

    private boolean isTaskCompleted(TodoTask task) {
        return Integer.valueOf(1).equals(task.getStatus());
    }

    private String getChineseWeekDay(LocalDate date) {
        String[] weekDays = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期日"};
        return weekDays[date.getDayOfWeek().getValue() - 1];
    }

    private String getChineseShortWeekDay(LocalDate date) {
        String[] weekDays = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
        return weekDays[date.getDayOfWeek().getValue() - 1];
    }
}
