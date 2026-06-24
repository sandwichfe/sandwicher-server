package com.lww.auth.server.portal.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.lww.common.web.entity.BaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@TableName("t_dict_item")
@Schema(name = "DictItem", description = "字典项表")
public class DictItem extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "字典项自增ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "字典类型ID")
    private Long dictTypeId;

    @Schema(description = "展示标签")
    private String label;

    @Schema(description = "字典值")
    private String value;

    @Schema(description = "备注")
    private String remark;
}

