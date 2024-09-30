package com.lww.common.web.Vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *   分页数据对象Vo
 * @author lww
 * @since 2022/4/20 15:11
 */
@Schema(description = "分页数据对象Vo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDataVo<T> {

    @Schema(description = "当前页数")
    private Long current;

    @Schema(description = "总条数")
    private Long total;

    @Schema(description = "响应数据")
    private T data;

}
