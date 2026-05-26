package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.server.user_center.vo.OperationLogPageQuery;
import com.lww.auth.server.user_center.vo.OperationLogVo;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.log.entity.OperationLog;
import com.lww.common.web.log.service.OperationLogService;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Tag(name = "操作日志管理")
@RestController
@RequestMapping("/api/sys/operation-log")
@RequiredArgsConstructor
public class AdminOperationLogController {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final OperationLogService operationLogService;

    @Operation(summary = "操作日志分页列表")
    @PostMapping("/list")
    public ResponseResult<IPage<OperationLogVo>> list(@RequestBody OperationLogPageQuery query) {
        Page<OperationLog> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<OperationLog> wrapper = new LambdaQueryWrapper<OperationLog>()
                .orderByDesc(OperationLog::getId);

        if (StringUtils.hasText(query.getModule())) {
            wrapper.eq(OperationLog::getModule, query.getModule());
        }
        if (StringUtils.hasText(query.getType())) {
            wrapper.eq(OperationLog::getType, query.getType());
        }
        if (StringUtils.hasText(query.getOperatorName())) {
            wrapper.like(OperationLog::getOperatorName, query.getOperatorName());
        }
        if (StringUtils.hasText(query.getKeyword())) {
            String keyword = query.getKeyword();
            wrapper.and(w -> w.like(OperationLog::getDescription, keyword)
                    .or()
                    .like(OperationLog::getRequestUri, keyword)
                    .or()
                    .like(OperationLog::getOperatorName, keyword)
                    .or()
                    .like(OperationLog::getParams, keyword));
        }

        LocalDateTime from = parseDateTime(query.getStartTimeFrom());
        if (from != null) {
            wrapper.ge(OperationLog::getStartTime, from);
        }
        LocalDateTime to = parseDateTime(query.getStartTimeTo());
        if (to != null) {
            wrapper.le(OperationLog::getStartTime, to);
        }

        IPage<OperationLogVo> result = operationLogService.page(page, wrapper)
                .convert(e -> CustomBeanUtils.copyProperties(e, OperationLogVo.class));
        return ResultUtil.success(result);
    }

    private LocalDateTime parseDateTime(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }
        return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
    }
}

