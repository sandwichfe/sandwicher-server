package com.lww.auth.server.portal.vo;

import com.lww.common.web.vo.PageVo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Schema(description = "字典项分页查询对象")
public class DictItemPageQuery extends PageVo {

    @Schema(description = "字典类型ID")
    private Long dictTypeId;

    @Schema(description = "关键词（标签/值）")
    private String keyword;
}

