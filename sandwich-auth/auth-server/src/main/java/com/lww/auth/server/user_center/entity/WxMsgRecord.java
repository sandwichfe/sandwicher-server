package com.lww.auth.server.user_center.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@TableName("wx_msg_record")
@Schema(description = "微信消息记录")
public class WxMsgRecord implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @Schema(description = "消息类型")
    private String msgType;

    @Schema(description = "发送人openid")
    private String fromUser;

    @Schema(description = "发送内容")
    private String content;

    @Schema(description = "回复内容")
    private String replyContent;

    @Schema(description = "接收人")
    private String receiveUser;

    @Schema(description = "接收时间")
    private LocalDateTime receiveTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
