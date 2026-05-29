package com.lww.auth.server.user_center.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "微信消息记录VO")
public class WxMsgRecordVo {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveTime;

    @Schema(description = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
