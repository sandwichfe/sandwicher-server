package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.auth.server.user_center.service.AdminOperationLogService;
import com.lww.auth.server.user_center.vo.OperationLogPageQuery;
import com.lww.auth.server.user_center.vo.OperationLogVo;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "操作日志管理")
@RestController
@RequestMapping("/api/sys/operation-log")
@RequiredArgsConstructor
public class AdminOperationLogController {

    private final AdminOperationLogService adminOperationLogService;

    @Operation(summary = "操作日志分页列表")
    @PostMapping("/list")
    public ResponseResult<IPage<OperationLogVo>> list(@RequestBody OperationLogPageQuery query) {
        return ResultUtil.success(adminOperationLogService.listOperationLog(query));
    }
}
