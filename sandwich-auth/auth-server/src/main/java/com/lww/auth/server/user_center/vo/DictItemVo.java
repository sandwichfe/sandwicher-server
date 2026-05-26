package com.lww.auth.server.user_center.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Schema(description = "字典项VO")
public class DictItemVo implements Serializable {

    @Schema(description = "字典项自增ID")
    private Long id;

    @Schema(description = "字典类型ID")
    private Long dictTypeId;

    @Schema(description = "展示标签")
    private String label;

    @Schema(description = "字典值")
    private String value;

    @Schema(description = "显示顺序")
    private Integer sort;

    @Schema(description = "备注")
    private String remark;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}

