package com.sandwich.wx.handler;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import com.sandwich.wx.redis.RedisUtil;
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

    private static final String LOGIN_PREFIX = "loginCode";

    @Resource
    private RedisUtil redisUtil;

    @Override
    public WxChatMsgTypeEnum getMsgType() {
        return WxChatMsgTypeEnum.TEXT_MSG;
    }

    @Override
    public String dealMsg(Map<String, String> messageMap) {
        log.info("接收到文本消息事件");
        String content = messageMap.get("Content");
        // 不是验证码关键字 直接返回空处理
        if (KEY_WORD.equals(content)) {
            return sendCodeMsg(messageMap);
        }
        if ("1513".equals(content)) {
            return sendReplyMessage(messageMap);
        }

        return "";

    }

    /**
     * 发送验证码消息
     */
    private String sendCodeMsg(Map<String, String> messageMap) {
        String fromUserName = messageMap.get("FromUserName");

        Random random = new Random();
        int num = random.nextInt(1000);
        String numKey = redisUtil.buildKey(LOGIN_PREFIX, String.valueOf(num));
        redisUtil.setNx(numKey, fromUserName, 5L, TimeUnit.MINUTES);

        String numContent = "您当前的验证码是：" + num + "！ 5分钟内有效";
        return MessageUtil.buildTextMessage(messageMap, numContent);
    }

    /**
     * 发送回复消息
     */
    private String sendReplyMessage(Map<String, String> messageMap) {
        String content = "自动开启字幕插件     链接：https://pan.baidu.com/s/1FiOihZrzlI1-pwj8Hjs5hQ?pwd=1513 提取码：1513";
        return MessageUtil.buildTextMessage(messageMap, content);
    }





}
