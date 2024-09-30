package com.lww.sandwich.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "DictType对象", description = "字典类型表")
public class DictType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
    @Schema(description = "id主键")
    private String dictId;

    @Schema(description = "字典名称")
    private String dictName;

    @Schema(description = "子典类型")
    private String dictType;

    @Schema(description = "状态（0正常 1停用）")
    private Integer status;

    @Schema(description = "创建人")
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime createTime;

    @Schema(description = "修改人")
    private String updateBy;

    @TableField(fill = FieldFill.UPDATE)
    @Schema(description = "修改时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime updateTime;

    @Schema(description = "备注")
    private String remark;


}
