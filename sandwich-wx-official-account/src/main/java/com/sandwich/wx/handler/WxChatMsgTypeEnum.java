package com.sandwich.wx.handler;

/**
 * 微信公众号事件类型枚举
 * @author lww
 * @since 2024/4/2 10:12
 */

public enum WxChatMsgTypeEnum {

    /**
     * 用户关注事件
     */
    SUBSCRIBE("event.subscribe", "用户关注事件"),
    /**
     * 用户取消关注事件
     */
    UNSUBSCRIBE("event.unsubscribe", "用户取消关注事件"),
    /**
     * 接收用户文本消息
     */
    TEXT_MSG("text", "接收用户文本消息");

    private final String msgType;

    private final String desc;

    WxChatMsgTypeEnum(String msgType, String desc) {
        this.msgType = msgType;
        this.desc = desc;
    }

    /**
     * 根据消息类型获取对应的枚举
     * @param msgType 消息类型
     * @return 对应的枚举
     */
    public static WxChatMsgTypeEnum getByMsgType(String msgType) {
        for (WxChatMsgTypeEnum wxChatMsgTypeEnum : WxChatMsgTypeEnum.values()) {
            if (wxChatMsgTypeEnum.msgType.equals(msgType)) {
                return wxChatMsgTypeEnum;
            }
        }
        return null;
    }

}
