package com.lww.sandwich.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "DictData对象", description = "字典数据表")
public class DictData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id主键")
    private String id;

    @ApiModelProperty("排序")
    private Integer sort;

    @ApiModelProperty("字典数据值")
    private String value;

    @ApiModelProperty("字典类型")
    private String type;

    @ApiModelProperty("状态（0正常 1停用）")
    private Integer status;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改人")
    private String updateBy;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;

    @ApiModelProperty("备注")
    private String remark;


}
