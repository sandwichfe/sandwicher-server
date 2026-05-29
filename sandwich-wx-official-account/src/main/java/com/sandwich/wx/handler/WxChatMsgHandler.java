package com.sandwich.wx.handler;

import java.util.Map;

/**
 * 微信公众号消息不同事件 对应策略
 * @author lww
 * @since 2024/4/2 10:11
 */
public interface WxChatMsgHandler {

    WxChatMsgTypeEnum getMsgType();

    String dealMsg(Map<String, String> messageMap);

}
