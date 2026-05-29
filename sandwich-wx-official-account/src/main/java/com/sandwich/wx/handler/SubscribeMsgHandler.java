package com.sandwich.wx.handler;

import com.sandwich.wx.utils.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 关注事件处理
 * @author lww
 * @since 2024/4/2 10:13
 */
@Component
@Slf4j
public class SubscribeMsgHandler implements WxChatMsgHandler {

    @Override
    public WxChatMsgTypeEnum getMsgType() {
        return WxChatMsgTypeEnum.SUBSCRIBE;
    }

    @Override
    public String dealMsg(Map<String, String> messageMap) {
        log.info("触发用户关注事件！");
        String subscribeContent = "你好，这里是一如往眉梢！";
        return MessageUtil.buildTextMessage(messageMap, subscribeContent);
    }

}
