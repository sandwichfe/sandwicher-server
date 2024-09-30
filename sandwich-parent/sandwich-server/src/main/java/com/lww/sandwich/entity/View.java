package com.lww.sandwich.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
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
@TableName("t_view")
@Schema(description = "View对象")
public class View implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.ASSIGN_ID)
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

}
