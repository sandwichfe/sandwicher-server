package com.sandwich.wx.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信事件处理工厂类
 * @author lww
 * @since 2024/4/2 10:13
 */
@Component
public class WxChatMsgFactory implements InitializingBean {

    /**
     * 注入了所有xChatMsgHandler的实现类
     */
    @Resource
    private List<WxChatMsgHandler> wxChatMsgHandlerList;

    private final Map<WxChatMsgTypeEnum, WxChatMsgHandler> handlerMap = new HashMap<>();

    /**
     * 根据消息类型获取对应的处理类
     * @param msgType 消息类型
     * @return 对应的处理类
     */
    public WxChatMsgHandler getHandlerByMsgType(String msgType) {
        WxChatMsgTypeEnum msgTypeEnum = WxChatMsgTypeEnum.getByMsgType(msgType);
        return handlerMap.get(msgTypeEnum);
    }

    /**
     * 初始化方法  把wxChatMsgHandlerList装到handlerMap中
     */
    @Override
    public void afterPropertiesSet() {
        // 这个WxChatMsgFactory实例化后 执行这里的方法 把wxChatMsgHandlerList装到handlerMap中
        for (WxChatMsgHandler wxChatMsgHandler : wxChatMsgHandlerList) {
            handlerMap.put(wxChatMsgHandler.getMsgType(), wxChatMsgHandler);
        }
    }

}
