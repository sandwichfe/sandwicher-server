package com.lww.sandwich.Vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.lww.dict.Dic;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 *
 * @author lww
 * @since 2022-04-26 09:38:22
 */
@Getter
@Setter
@ToString
@ApiModel(value = "View对象VO", description = "")
public class ViewVO implements Serializable {

    private static final long serialVersionUID = 2781951723978392659L;

    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("ip")
    private String ip;

    @ApiModelProperty("地区")
    private String area;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty("访问时间")
    private LocalDateTime viewTime;


    @Dic(code = "sexType")
    @ApiModelProperty("测试字段名")
    private String testDictType;

    @ApiModelProperty("测试字典值")
    private String testDictTypeValue;

}
