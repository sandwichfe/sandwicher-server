package com.lww.auth.server.portal.mq;

import com.lww.mq.constant.MqConstants;
import com.lww.mq.dto.WxMsgRecordDTO;
import com.lww.auth.server.portal.entity.WxMsgRecord;
import com.lww.auth.server.portal.service.WxMsgRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @author lww
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WxMsgRecordConsumer {

    private final WxMsgRecordService wxMsgRecordService;

    @RabbitListener(queues = MqConstants.WX_MSG_QUEUE)
    public void handleMessage(WxMsgRecordDTO dto) {
        log.info("收到微信消息记录MQ消息：{}", dto);
        WxMsgRecord record = new WxMsgRecord();
        record.setMsgType(dto.getMsgType());
        record.setFromUser(dto.getFromUser());
        record.setContent(dto.getContent());
        record.setReplyContent(dto.getReplyContent());
        record.setReceiveUser(dto.getReceiveUser());
        record.setReceiveTime(dto.getReceiveTime());
        record.setCreateTime(LocalDateTime.now());
        wxMsgRecordService.save(record);
    }
}
