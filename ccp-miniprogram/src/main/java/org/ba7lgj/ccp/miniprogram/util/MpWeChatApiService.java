package org.ba7lgj.ccp.miniprogram.util;

import com.alibaba.fastjson2.JSONObject;
import org.ba7lgj.ccp.miniprogram.dto.MpCode2SessionResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

/**
 * 微信接口调用工具。
 */
@Component
public class MpWeChatApiService {
    private static final Logger log = LoggerFactory.getLogger(MpWeChatApiService.class);
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Value("${ccp.wx.appid}")
    private String appId;

    @Value("${ccp.wx.secret}")
    private String appSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public MpCode2SessionResponse code2Session(String jsCode) {
        if (!StringUtils.hasText(jsCode)) {
            throw new IllegalArgumentException("jsCode不能为空");
        }
        String url = WX_LOGIN_URL + "?appid=" + appId + "&secret=" + appSecret + "&js_code=" + jsCode
                + "&grant_type=authorization_code";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.warn("微信接口调用失败，code:{}, status:{}", jsCode, response.getStatusCode());
            throw new IllegalStateException("调用微信接口失败");
        }
        JSONObject json = JSONObject.parseObject(response.getBody());
        MpCode2SessionResponse result = new MpCode2SessionResponse();
        if (json != null) {
            result.setOpenId(json.getString("openid"));
            result.setSessionKey(json.getString("session_key"));
            result.setUnionId(json.getString("unionid"));
            result.setErrCode(json.getInteger("errcode"));
            result.setErrMsg(json.getString("errmsg"));
        }
        if (!StringUtils.hasText(result.getOpenId())) {
            String errMsg = result.getErrMsg() == null ? "空响应" : result.getErrMsg();
            log.warn("获取openid失败，jsCode:{}，响应:{}", jsCode, response.getBody());
            throw new IllegalStateException("获取openid失败:" + errMsg);
        }
        return result;
    }
}
