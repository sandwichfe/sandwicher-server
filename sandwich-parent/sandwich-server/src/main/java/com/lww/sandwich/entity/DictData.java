package com.lww.sandwich.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Getter;
import lombok.Setter;

/**
 * 字典数据表
 * @author lww
 * @since 2022-04-07 16:00:02
 */
@Getter
@Setter
@TableName("t_dict_data")
@Tag(name = "DictData对象", description = "字典数据表")
public class DictData implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "id主键")
    private String id;

    @Schema(description = "排序")
    private Integer sort;

    @Schema(description = "字典数据值")
    private String value;

    @Schema(description = "字典类型")
    private String type;

    @Schema(description = "状态（0正常 1停用）")
    private Integer status;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改人")
    private String updateBy;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @Schema(description = "备注")
    private String remark;


}
