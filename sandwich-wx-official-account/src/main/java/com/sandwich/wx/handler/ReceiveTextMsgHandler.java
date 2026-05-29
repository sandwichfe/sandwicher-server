package com.sandwich.wx.handler;

import java.util.Map;
import java.util.Random;

import com.lww.redis.util.RedisUtil;
import com.sandwich.wx.config.WxConfigProperties;
import com.sandwich.wx.utils.MessageUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 接收到文本消息事件处理
 * @author lww
 * @since 2024/4/2 10:13
 */
@Component
@Slf4j
public class ReceiveTextMsgHandler implements WxChatMsgHandler {

    private static final String KEY_WORD = "验证码";

    private static final String LOGIN_PREFIX = "login:code:";

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private WxConfigProperties wxConfigProperties;

    @Override
    public WxChatMsgTypeEnum getMsgType() {
        return WxChatMsgTypeEnum.TEXT_MSG;
    }

    @Override
    public String dealMsg(Map<String, String> messageMap) {
        log.info("接收到文本消息事件");
        String content = messageMap.get("Content");
        if (KEY_WORD.equals(content)) {
            return sendCodeMsg(messageMap);
        }
        Map<String, String> autoReply = wxConfigProperties.getAutoReply();
        if (autoReply != null && autoReply.containsKey(content)) {
            return MessageUtil.buildTextMessage(messageMap, autoReply.get(content));
        }
        return "";
    }

    private String sendCodeMsg(Map<String, String> messageMap) {
        String fromUserName = messageMap.get("FromUserName");

        Random random = new Random();
        int num = random.nextInt(1000);
        String numKey = LOGIN_PREFIX + num;
        redisUtil.set(numKey, fromUserName, 300L);

        String numContent = "您当前的验证码是：" + num + "！ 5分钟内有效";
        return MessageUtil.buildTextMessage(messageMap, numContent);
    }

}
