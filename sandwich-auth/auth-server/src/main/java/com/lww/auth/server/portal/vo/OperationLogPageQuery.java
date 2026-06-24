package com.lww.auth.server.portal.vo;

import com.lww.common.web.vo.PageVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "操作日志分页查询对象")
public class OperationLogPageQuery extends PageVo {

    @Schema(description = "关键词（描述/URI/操作者/参数）")
    private String keyword;

    @Schema(description = "操作者姓名")
    private String operatorName;

    @Schema(description = "模块")
    private String module;

    @Schema(description = "类型")
    private String type;

    @Schema(description = "开始时间（yyyy-MM-dd HH:mm:ss）")
    private String startTimeFrom;

    @Schema(description = "结束时间（yyyy-MM-dd HH:mm:ss）")
    private String startTimeTo;
}

