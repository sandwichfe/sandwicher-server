package com.lww.common.web.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 
 * </p>
 *
 * @author lww
 * @since 2025-06-13 15:53:28
 */
@Getter
@Setter
@TableName("t_operation_log")
@Schema(name = "OperationLog", description = "")
@Builder
public class OperationLog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String module;

    private String type;

    private String description;

    private String method;

    private String params;

    private String result;

    private Long operatorId;

    private String operatorName;

    private String requestIp;

    private String requestUri;

    private String requestRegion;

    private LocalDateTime startTime;

    private Long duration;

    private LocalDateTime createTime;
}
