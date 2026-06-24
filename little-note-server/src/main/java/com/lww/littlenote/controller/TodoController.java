package com.lww.littlenote.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.littlenote.config.api.ApiLittleNoteRestController;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@ApiLittleNoteRestController
@RequestMapping("/todo")
@RequiredArgsConstructor
public class TodoController {

    private final TodoTaskService todoTaskService;

    @PostMapping("/tasks/list")
    public ResponseResult<IPage<TodoTaskVo>> listTasks(@RequestBody TodoTaskQueryReq todoTaskQueryReq) {
        return ResultUtil.success(todoTaskService.listTasks(todoTaskQueryReq));
    }

    @GetMapping("/tasks/{id}")
    public ResponseResult<TodoTaskVo> getTask(@PathVariable Long id) {
        return ResultUtil.success(todoTaskService.getTask(id));
    }

    @PostMapping("/tasks")
    public ResponseResult<TodoTaskVo> addTask(@RequestBody TodoTaskReq taskReq) {
        return ResultUtil.success(todoTaskService.addTask(taskReq));
    }

    @PutMapping("/tasks/{id}")
    public ResponseResult<TodoTaskVo> editTask(@PathVariable Long id, @RequestBody TodoTaskReq taskReq) {
        return ResultUtil.success(todoTaskService.editTask(id, taskReq));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseResult<Void> deleteTask(@PathVariable Long id) {
        todoTaskService.deleteTask(id);
        return ResultUtil.success();
    }

    @PostMapping("/tasks/{id}/complete")
    public ResponseResult<Void> completeTask(@PathVariable Long id) {
        todoTaskService.completeTaskOnce(id);
        return ResultUtil.success();
    }

    @GetMapping("/tasks/{id}/completion-records")
    public ResponseResult<List<TodoTaskCompletionRecordVO>> listTaskCompletionRecords(@PathVariable Long id) {
        return ResultUtil.success(todoTaskService.listTaskCompletionRecords(id));
    }
    
    @PostMapping("/tasks/completion-record/update")
    public ResponseResult<Void> updateTaskCompletionRecord(@RequestBody TodoTaskCompletionRecordUpdateReq req) {
        todoTaskService.updateTaskCompletionRecord(req);
        return ResultUtil.success();
    }

    @GetMapping("/tasks/stats")
    public ResponseResult<TaskStatsVO> getTaskStats(@RequestParam(required = false) String category) {
        return ResultUtil.success(todoTaskService.getTaskStats(category));
    }

    @GetMapping("/tasks/counts")
    public ResponseResult<TodoTaskCountVO> getTaskCounts() {
        return ResultUtil.success(todoTaskService.getTaskCounts());
    }

    @PostMapping("/tasks/day-view")
    public ResponseResult<DayViewVO> getDayView(@RequestBody DayViewReq req) {
        return ResultUtil.success(todoTaskService.getDayView(req.getDate()));
    }

    @PostMapping("/tasks/week-view")
    public ResponseResult<WeekViewVO> getWeekView(@RequestBody WeekViewReq req) {
        return ResultUtil.success(todoTaskService.getWeekView(req.getYear(), req.getWeek()));
    }

    @PostMapping("/tasks/month-view")
    public ResponseResult<MonthViewVO> getMonthView(@RequestBody MonthViewReq req) {
        return ResultUtil.success(todoTaskService.getMonthView(req.getYear(), req.getMonth()));
    }
}
