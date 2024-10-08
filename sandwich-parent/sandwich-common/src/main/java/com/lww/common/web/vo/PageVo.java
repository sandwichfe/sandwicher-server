package com.lww.common.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

/**
 *   分页接受对象
 * @author lww
 * @date 2022/4/8 15:06
 */
@Data
@Schema(description = "分页请求对象")
public class PageVo {

    @NotNull(message = "pageNum不能为空")
    @Schema(description = "当前页数")
    private Integer pageNum;

    @NotNull(message = "pageSize不能为空")
    @Schema(description = "每页条数")
    private Integer pageSize;

    @Schema(description = "排序字段")
    private String sort;

    @Schema(description = "asc/desc(升序/降序)")
    private String order;


}
