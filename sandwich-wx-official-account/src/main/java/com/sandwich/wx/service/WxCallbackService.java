package com.sandwich.wx.service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import com.lww.mq.constant.MqConstants;
import com.lww.mq.dto.WxMsgRecordDTO;
import com.sandwich.wx.config.WxConfigProperties;
import com.sandwich.wx.handler.WxChatMsgFactory;
import com.sandwich.wx.handler.WxChatMsgHandler;
import com.sandwich.wx.utils.MessageUtil;
import com.sandwich.wx.utils.SecuritySha1Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 微信回调服务
 * @author lww
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class WxCallbackService {

    private final WxConfigProperties wxConfigProperties;
    private final WxChatMsgFactory wxChatMsgFactory;
    private final RabbitTemplate rabbitTemplate;

    /**
     * 验证微信回调签名
     * @param signature 微信签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param echostr 随机字符串
     * @return 验证结果
     */
    public String verifySignature(String signature, String timestamp, String nonce, String echostr) {
        log.info("get验签请求参数：signature:{}，timestamp:{}，nonce:{}，echostr:{}",
                signature, timestamp, nonce, echostr);
        String shaStr = SecuritySha1Utils.shaEncode(wxConfigProperties.getToken(), timestamp, nonce, "");
        if (signature.equals(shaStr)) {
            return echostr;
        }
        return "unknown";
    }

    /**
     * 处理微信消息回调
     * @param requestBody 请求体
     * @param signature 签名
     * @param timestamp 时间戳
     * @param nonce 随机数
     * @param msgSignature 消息签名
     * @return 响应内容
     */
    public String handleMessage(String requestBody, String signature, String timestamp, 
                               String nonce, String msgSignature) {
        log.info("接收到微信消息：requestBody：{}", requestBody);
        
        // 解析XML消息
        Map<String, String> messageMap = MessageUtil.parseXml(requestBody);
        
        // 获取消息类型和事件类型
        String msgType = messageMap.get("MsgType");
        String event = messageMap.get("Event") == null ? "" : messageMap.get("Event");
        log.info("msgType:{},event:{}", msgType, event);

        // 构建消息类型键
        String msgTypeKey = buildMsgTypeKey(msgType, event);
        
        // 根据消息类型获取对应的处理器
        WxChatMsgHandler wxChatMsgHandler = wxChatMsgFactory.getHandlerByMsgType(msgTypeKey);
        if (Objects.isNull(wxChatMsgHandler)) {
            log.warn("未找到对应的消息处理器，msgTypeKey: {}", msgTypeKey);
            return "unknown";
        }
        
        // 处理消息并返回响应
        String replyContent = wxChatMsgHandler.dealMsg(messageMap);
        log.info("replyContent:{}", replyContent);

        // 发送消息记录到MQ
        sendMsgRecord(messageMap, msgTypeKey, replyContent);

        return replyContent;
    }

    private void sendMsgRecord(Map<String, String> messageMap, String msgType, String replyContent) {
        try {
            WxMsgRecordDTO dto = new WxMsgRecordDTO();
            dto.setMsgType(msgType);
            dto.setFromUser(messageMap.get("FromUserName"));
            dto.setContent(messageMap.get("Content"));
            dto.setReplyContent(replyContent);
            dto.setReceiveUser(messageMap.get("ToUserName"));
            dto.setReceiveTime(LocalDateTime.now());
            rabbitTemplate.convertAndSend(MqConstants.WX_MSG_EXCHANGE, MqConstants.WX_MSG_ROUTING_KEY, dto);
        } catch (Exception e) {
            log.error("发送微信消息记录到MQ失败", e);
        }
    }

    /**
     * 构建消息类型键
     * @param msgType 消息类型
     * @param event 事件类型
     * @return 消息类型键
     */
    private String buildMsgTypeKey(String msgType, String event) {
        StringBuilder sb = new StringBuilder();
        sb.append(msgType);
        if (StringUtils.hasLength(event)) {
            sb.append(".");
            sb.append(event);
        }
        return sb.toString();
    }
}
