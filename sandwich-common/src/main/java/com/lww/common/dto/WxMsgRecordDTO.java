package com.lww.common.dto;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class WxMsgRecordDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String msgType;

    private String fromUser;

    private String content;

    private String replyContent;

    private String receiveUser;

    private LocalDateTime receiveTime;
}
