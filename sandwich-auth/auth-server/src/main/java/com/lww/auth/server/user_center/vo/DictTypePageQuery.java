package com.lww.auth.server.user_center.vo;

import com.lww.common.web.vo.PageVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "字典类型分页查询对象")
public class DictTypePageQuery extends PageVo {

    @Schema(description = "关键词（编码/名称）")
    private String keyword;
}

