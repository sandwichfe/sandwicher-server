package com.lww.auth.server.user_center.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "操作日志VO")
public class OperationLogVo implements Serializable {

    @Schema(description = "日志ID")
    private Long id;

    @Schema(description = "模块")
    private String module;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "描述")
    private String description;

    @Schema(description = "方法名")
    private String method;

    @Schema(description = "参数")
    private String params;

    @Schema(description = "结果")
    private String result;

    @Schema(description = "操作者ID")
    private Long operatorId;

    @Schema(description = "操作者姓名")
    private String operatorName;

    @Schema(description = "请求IP")
    private String requestIp;

    @Schema(description = "请求URI")
    private String requestUri;

    @Schema(description = "请求地区")
    private String requestRegion;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "耗时（ms）")
    private Long duration;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}

