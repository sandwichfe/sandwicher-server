package com.lww.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *   分页数据对象Vo
 * @author lww
 * @since 2022/4/20 15:11
 */
@ApiModel("分页数据对象Vo")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDataVo<T> {

    @ApiModelProperty("当前页数")
    private Long current;

    @ApiModelProperty("总条数")
    private Long total;

    @ApiModelProperty("响应数据")
    private T data;

}
