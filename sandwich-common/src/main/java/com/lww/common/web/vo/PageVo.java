package com.lww.common.web.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

/**
 * 分页接受对象
 *
 * @author lww
 * @since 2022/4/8 15:06
 */
@Data
@Schema(description = "分页请求对象")
@NoArgsConstructor
@AllArgsConstructor
public class PageVo {

    @NotNull(message = "pageNum不能为空")
    @Schema(description = "当前页数", type = "integer", example = "1")
    private Integer pageNum;

    @NotNull(message = "pageSize不能为空")
    @Schema(description = "每页条数", type = "integer", example = "10")
    private Integer pageSize;

    @Schema(description = "排序字段")
    private String sort;

    @Schema(description = "asc/desc(升序/降序)")
    private String order;

    public PageVo(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

}
