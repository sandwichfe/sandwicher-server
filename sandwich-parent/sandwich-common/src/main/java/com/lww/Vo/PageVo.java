package com.lww.Vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.logging.log4j.message.Message;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description 分页接受对象
 * @author lww
 * @date 2022/4/8 15:06
 */
@Data
@ApiModel("分页请求对象")
public class PageVo {

    @NotNull(message = "pageNum不能为空")
    @ApiModelProperty("当前页数")
    private Integer pageNum;

    @NotNull(message = "pageSize不能为空")
    @ApiModelProperty("每页条数")
    private Integer pageSize;

    @ApiModelProperty("排序字段")
    private String sort;

    @ApiModelProperty("asc/desc(升序/降序)")
    private String order;


}
