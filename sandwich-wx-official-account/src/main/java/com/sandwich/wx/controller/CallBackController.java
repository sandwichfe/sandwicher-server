package com.sandwich.wx.controller;

import com.sandwich.wx.service.WxCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @author lww
 */
@RestController
@Slf4j
@RequiredArgsConstructor
public class CallBackController {

    private final WxCallbackService wxCallbackService;

    @RequestMapping("/test")
    public String test() {
        wxCallbackService.testSend();
        return "hello world sandwich";
    }

    /**
     * 回调消息校验  在微信公众管理接口那里 填入这个接口  微信保存接口设置时会调一遍这个接口确保通畅以及安全
     */
    @GetMapping("callback")
    public String callback(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce, @RequestParam("echostr") String echoStr) {
        return wxCallbackService.verifySignature(signature, timestamp, nonce, echoStr);
    }

    /**
     * 微信公众号 被订阅  以及有用户发送消息等相关操作   微信会回调请求到这个接口
     *
     * @param requestBody  微信回调请求体
     * @param signature    微信回调签名
     * @param timestamp    微信回调时间戳
     * @param nonce        微信回调随机数
     * @param msgSignature 微信回调消息签名
     * @return 微信回调响应体
     */
    @PostMapping(value = "callback", produces = "application/xml;charset=UTF-8")
    public String callback(@RequestBody String requestBody, @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce,
            @RequestParam(value = "msg_signature", required = false) String msgSignature) {
        return wxCallbackService.handleMessage(requestBody, signature, timestamp, nonce, msgSignature);
    }

}