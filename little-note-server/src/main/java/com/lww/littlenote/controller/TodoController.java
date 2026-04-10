package com.lww.littlenote.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.resources_server.utils.SecurityUserUtils;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.littlenote.config.api.ApiLittleNoteRestController;
import com.lww.littlenote.entity.TodoTask;
import com.lww.littlenote.req.DayViewReq;
import com.lww.littlenote.req.MonthViewReq;
import com.lww.littlenote.req.TodoTaskCompletionRecordUpdateReq;
import com.lww.littlenote.req.TodoTaskQueryReq;
import com.lww.littlenote.req.TodoTaskReq;
import com.lww.littlenote.req.WeekViewReq;
import com.lww.littlenote.service.TodoTaskService;
import com.lww.littlenote.vo.DayViewVO;
import com.lww.littlenote.vo.MonthViewVO;
import com.lww.littlenote.vo.TaskStatsVO;
import com.lww.littlenote.vo.TodoTaskCompletionRecordVO;
import com.lww.littlenote.vo.TodoTaskCountVO;
import com.lww.littlenote.vo.TodoTaskVo;
import com.lww.littlenote.vo.WeekViewVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@ApiLittleNoteRestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoTaskService todoTaskService;

    @PostMapping("/tasks/list")
    public ResponseResult<IPage<TodoTaskVo>> listTasks(@RequestBody TodoTaskQueryReq todoTaskQueryReq) {
        Long userId = SecurityUserUtils.getUserId();
        todoTaskQueryReq.setUserId(userId);
        Page<TodoTask> page = todoTaskService.listTasks(todoTaskQueryReq);
        IPage<TodoTaskVo> pageVo = page.convert(item -> CustomBeanUtils.copyProperties(item, TodoTaskVo.class));
        return ResultUtil.success(pageVo);
    }

    @GetMapping("/tasks/{id}")
    public ResponseResult<TodoTaskVo> getTask(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        TodoTask task = todoTaskService.getById(id);
        if (task == null || !task.getUserId().equals(userId)) {
            return ResultUtil.error("任务不存在");
        }
        return ResultUtil.success(CustomBeanUtils.copyProperties(task, TodoTaskVo.class));
    }

    @PostMapping("/tasks")
    public ResponseResult<TodoTaskVo> addTask(@RequestBody TodoTaskReq taskReq) {
        Long userId = SecurityUserUtils.getUserId();
        TodoTask task = new TodoTask();
        BeanUtils.copyProperties(taskReq, task);
        task.setUserId(userId);
        task.setCompletedCount(0);
        task.setStatus(0);
        task.setCreateTime(LocalDateTime.now());
        task.setCreateBy(userId);

        if (task.getTargetCount() == null || task.getTargetCount() <= 0) {
            task.setTargetCount(1);
        }

        boolean saved = todoTaskService.save(task);
        if (saved) {
            return ResultUtil.success(CustomBeanUtils.copyProperties(task, TodoTaskVo.class));
        }
        return ResultUtil.error("添加失败");
    }

    @PutMapping("/tasks/{id}")
    public ResponseResult<TodoTaskVo> editTask(@PathVariable Long id, @RequestBody TodoTaskReq taskReq) {
        Long userId = SecurityUserUtils.getUserId();
        TodoTask existingTask = todoTaskService.getById(id);
        if (existingTask == null || !existingTask.getUserId().equals(userId)) {
            return ResultUtil.error("任务不存在");
        }

        TodoTask task = new TodoTask();
        BeanUtils.copyProperties(taskReq, task);
        task.setId(id);
        task.setUserId(userId);
        task.setUpdateTime(LocalDateTime.now());
        task.setUpdateBy(userId);

        if (task.getTargetCount() != null && task.getTargetCount() <= 0) {
            task.setTargetCount(1);
        }

        boolean updated = todoTaskService.updateById(task);
        if (updated) {
            return ResultUtil.success(CustomBeanUtils.copyProperties(task, TodoTaskVo.class));
        }
        return ResultUtil.error("更新失败");
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseResult<Void> deleteTask(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        todoTaskService.deleteTask(id, userId);
        return ResultUtil.success();
    }

    @PostMapping("/tasks/{id}/complete")
    public ResponseResult<Void> completeTask(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        todoTaskService.completeTaskOnce(id, userId);
        return ResultUtil.success();
    }

    @GetMapping("/tasks/{id}/completion-records")
    public ResponseResult<List<TodoTaskCompletionRecordVO>> listTaskCompletionRecords(@PathVariable Long id) {
        Long userId = SecurityUserUtils.getUserId();
        return ResultUtil.success(todoTaskService.listTaskCompletionRecords(id, userId));
    }
    
    @PostMapping("/tasks/completion-record/update")
    public ResponseResult<Void> updateTaskCompletionRecord(@RequestBody TodoTaskCompletionRecordUpdateReq req) {
        Long userId = SecurityUserUtils.getUserId();
        todoTaskService.updateTaskCompletionRecord(userId, req);
        return ResultUtil.success();
    }

    @GetMapping("/tasks/stats")
    public ResponseResult<TaskStatsVO> getTaskStats(@RequestParam(required = false) String category) {
        Long userId = SecurityUserUtils.getUserId();
        return ResultUtil.success(todoTaskService.getTaskStats(userId, category));
    }

    @GetMapping("/tasks/counts")
    public ResponseResult<TodoTaskCountVO> getTaskCounts() {
        Long userId = SecurityUserUtils.getUserId();
        return ResultUtil.success(todoTaskService.getTaskCounts(userId));
    }

    @PostMapping("/tasks/day-view")
    public ResponseResult<DayViewVO> getDayView(@RequestBody DayViewReq req) {
        Long userId = SecurityUserUtils.getUserId();
        return ResultUtil.success(todoTaskService.getDayView(req.getDate(), userId));
    }

    @PostMapping("/tasks/week-view")
    public ResponseResult<WeekViewVO> getWeekView(@RequestBody WeekViewReq req) {
        Long userId = SecurityUserUtils.getUserId();
        return ResultUtil.success(todoTaskService.getWeekView(req.getYear(), req.getWeek(), userId));
    }

    @PostMapping("/tasks/month-view")
    public ResponseResult<MonthViewVO> getMonthView(@RequestBody MonthViewReq req) {
        Long userId = SecurityUserUtils.getUserId();
        return ResultUtil.success(todoTaskService.getMonthView(req.getYear(), req.getMonth(), userId));
    }
}
