package com.lww.sandwich;

import com.lww.sandwich.constant.AppConstants;
import com.lww.redis.util.RedisUtil;
import com.lww.sandwich.utils.wx.WxAppUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SandwichServerApplicationTest {

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private WxAppUtil wxAppUtil;

    @Resource
    private StringEncryptor stringEncryptor;

    @Test
    public void Test(){
        //redisUtil.set("tt","htr");
        Object tt = redisUtil.get("1212121");
        System.out.println(tt);
    }

    @Test
    public void testSend(){
        // https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/mp-message-management/subscribe-message/sendMessage.html
        //{{time4.DATA}}   时间
        //{{thing1.DATA}}  活动名称
        //{{thing9.DATA}}  温馨提醒
        //{{time27.DATA}}  结束时间
        Map<String,Object> data = new HashMap<>(2);
        data.put("time4",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        data.put("thing1","APP活动名称");
        data.put("thing9","APP提醒内容");
        data.put("time27",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        String telId = "5oP0dFBcEpixmI6tahxCiC1CKvlG2eWXIs0jDO6lu-Y";
        wxAppUtil.sendMsg(AppConstants.WX_USER_OPEN_ID, telId,data,"pages/home/home");
    }

    @Test
    public void testJayspy() {
        String username = stringEncryptor.encrypt("test");
        log.info("username 密文: " + username);
    }



}
