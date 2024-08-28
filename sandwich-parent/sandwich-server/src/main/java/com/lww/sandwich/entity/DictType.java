package com.lww.sandwich.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典类型表
 * @author lww
 * @since 2022-04-07 16:00:02
 */
@Getter
@Setter
@TableName("t_dict_type")
@ApiModel(value = "DictType对象", description = "字典类型表")
public class DictType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty("id主键")
    private String dictId;

    @ApiModelProperty("字典名称")
    private String dictName;

    @ApiModelProperty("子典类型")
    private String dictType;

    @ApiModelProperty("状态（0正常 1停用）")
    private Integer status;

    @ApiModelProperty("创建人")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    private String updateBy;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty("修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime updateTime;

    @ApiModelProperty("备注")
    private String remark;


}
