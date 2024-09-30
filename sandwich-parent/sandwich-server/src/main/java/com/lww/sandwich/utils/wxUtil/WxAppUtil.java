package com.lww.sandwich.utils.wxUtil;

import com.alibaba.fastjson.JSONObject;
import com.lww.sandwich.constant.AppConstants;
import com.lww.sandwich.utils.AesUtil;
import com.lww.sandwich.utils.HttpClientUtil;
import com.lww.sandwich.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 *  
 * @author lww
 * @since 2023/7/14 16:13
 */
@Slf4j
@Component
public class WxAppUtil {

    @Resource
    private RedisUtil redisUtil;

    public String getWxAccessToken(String appId, String appSecret){
        String key = "sand_app_id_"+appId;
        // aes密钥必须要为16位
        String salt = "sandwich_Key_Key";
        // redis存在
        if (redisUtil.hasKey(key)){
            String accessToken = redisUtil.get(key);
            // 解密返回
            return AesUtil.aesDecrypt(accessToken, salt);
        }
        // redis不存在 获取 放入redis并返回
        String accessToken = getWxAccessTokenInterface(appId, appSecret);
        if (!StringUtils.hasText(accessToken)){
            return null;
        }
        // 加密存入redis 两小时过期时间
        String encrypt = AesUtil.aesEncrypt(accessToken, salt);
        redisUtil.set(key,encrypt,60*60*2);
        return accessToken;
    }


    /**
     * 获取微信小程序AccessToken  每两个小时会过期 同时每天有调取限制  建议调取完就放到Redis里 两小时再更新
     * @author lww
     * @since 2023/7/14 16:38
     * @param appId
	 * @param appSecret
     * @return
     */
    private String getWxAccessTokenInterface(String appId, String appSecret) {
        log.info("开始获取AccessToken:");
        String tokenUrl = AppConstants.WX_GET_TOKEN_URL + "&appid=" + appId + "&secret=" + appSecret;
        String data = HttpClientUtil.doGet(tokenUrl);
        JSONObject jsonObject = JSONObject.parseObject(data);
        String accessToken = jsonObject.getString("access_token");
        if (!StringUtils.hasText(accessToken)) {
            log.error("调取接口微信小程序accessToken为空：",accessToken);
            return null;
        }
        return  accessToken;
    }

    /**
     * 微信小程序发送消息提醒
     * @author lww
     * @since 2023/7/14 16:19
     * @param openId
     * @param temId
     * @param data
     * @param page
     */
    public void sendMsg(String openId, String temId, Map<String,Object> data, String page){
        // 微信接口跳去所需token
        String accessToken = getWxAccessToken(AppConstants.WX_APP_ID,AppConstants.WX_APP_SECRET);
        if (!StringUtils.hasText(accessToken)){
            log.info("小程序token为空！");
            return;
        }
        // 微信消息发送url
        String subMsgUrl = AppConstants.WX_MSG_SEND_URL+accessToken;

        JSONObject jsonObjectReq = new JSONObject();
        // 用户openId
        jsonObjectReq.put("touser", openId);
        // 消息模板id
        jsonObjectReq.put("template_id", temId);
        // 消息内容
        jsonObjectReq.put("data", convertData(data));
        // 跳转小程序类型  developer为开发版；trial为体验版；formal为正式版；默认为正式版
        jsonObjectReq.put("miniprogram_state", "trial");
        // 点击后跳转对应页面  支持传递参数 index?aa=value
        if (StringUtils.hasText(page)){
            jsonObjectReq.put("page",page);
        }
        String response = HttpClientUtil.doPostJson(subMsgUrl, jsonObjectReq.toJSONString());
        JSONObject ret = JSONObject.parseObject(response);
        Integer errcode = (Integer) ret.get("errcode");
        if (errcode == 0) {
            log.info("消息提醒发送成功！");
        } else {
            String errmsg = (String) ret.get("errmsg");
            log.info("消息提醒发送失败！errorMsg:" + errmsg);
        }
    }

    /**
     * 将消息数据转换为wx小程序需要的格式
     * @author lww
     * @since 2023/7/14 16:30
     * @param data
     * @return
     */
    public Map<String,Map<String,Object>> convertData(Map<String,Object> data){
        Map<String,Map<String,Object>> retData = new HashMap<>(5);
        if (data==null||data.isEmpty()){
            return retData;
        }
        for (Map.Entry<String,Object> entry:data.entrySet()){
            String key = entry.getKey();
            Object value = entry.getValue();
            Map<String,Object> param = new HashMap<>();
            param.put("value",value);
            retData.put(key,param);
        }
        return retData;
    }

}
