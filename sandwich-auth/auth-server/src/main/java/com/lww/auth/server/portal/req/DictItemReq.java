package com.lww.auth.server.portal.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
@Schema(description = "字典项请求参数")
public class DictItemReq implements Serializable {

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
}

