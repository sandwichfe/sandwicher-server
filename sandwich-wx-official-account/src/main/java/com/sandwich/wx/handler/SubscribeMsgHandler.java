package com.sandwich.wx.handler;

import java.util.Map;

import com.sandwich.wx.config.WxConfigProperties;
import com.sandwich.wx.utils.MessageUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 关注事件处理
 * @author lww
 * @since 2024/4/2 10:13
 */
@Component
@Slf4j
public class SubscribeMsgHandler implements WxChatMsgHandler {

    @Resource
    private WxConfigProperties wxConfigProperties;

    @Override
    public WxChatMsgTypeEnum getMsgType() {
        return WxChatMsgTypeEnum.SUBSCRIBE;
    }

    @Override
    public String dealMsg(Map<String, String> messageMap) {
        log.info("触发用户关注事件！");
        return MessageUtil.buildTextMessage(messageMap, wxConfigProperties.getSubscribeReply());
    }

}
