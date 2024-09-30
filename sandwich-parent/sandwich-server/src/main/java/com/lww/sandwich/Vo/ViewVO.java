package com.lww.sandwich.Vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lww.common.dict.DictConvert;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author lww
 * @since 2022-04-26 09:38:22
 */
@Getter
@Setter
@ToString
@Schema(description =  "View对象VO")
public class ViewVO implements Serializable {

    private static final long serialVersionUID = 2781951723978392659L;

    @Schema(description = "id")
    private String id;

    @Schema(description = "用户名")
    private String userName;

    @Schema(description = "ip")
    private String ip;

    @Schema(description = "地区")
    private String area;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @Schema(description = "访问时间")
    private LocalDateTime viewTime;


    @DictConvert(code = "sexType")
    @Schema(description = "测试字段名")
    private String testDictType;


    // 打开了字典转化不生效 后续再开
    // @ApiModelProperty("测试字典值")
    // private String testDictTypeValue;

}
