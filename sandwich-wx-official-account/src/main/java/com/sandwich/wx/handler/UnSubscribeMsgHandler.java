package com.sandwich.wx.handler;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * todo
 *
 * @author lww
 * @since 2025/12/5
 */
@Component
@Slf4j
public class UnSubscribeMsgHandler implements WxChatMsgHandler {
    @Override
    public WxChatMsgTypeEnum getMsgType() {
        return WxChatMsgTypeEnum.UNSUBSCRIBE;
    }

    @Override
    public String dealMsg(Map<String, String> messageMap) {
        log.info("触发用户取消关注事件！");
        return "";
    }
}
