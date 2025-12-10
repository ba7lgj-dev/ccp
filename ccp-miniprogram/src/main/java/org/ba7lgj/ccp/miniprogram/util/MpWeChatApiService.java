package org.ba7lgj.ccp.miniprogram.util;

import com.alibaba.fastjson2.JSONObject;
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

    @Value("${ccp.wx.appid}")
    private String appId;

    @Value("${ccp.wx.secret}")
    private String appSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getOpenId(String code) {
        if (!StringUtils.hasText(code)) {
            throw new IllegalArgumentException("code不能为空");
        }
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + appId
                + "&secret=" + appSecret + "&js_code=" + code + "&grant_type=authorization_code";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.warn("微信接口调用失败，code:{}, status:{}", code, response.getStatusCode());
            throw new IllegalStateException("调用微信接口失败");
        }
        JSONObject json = JSONObject.parseObject(response.getBody());
        if (json == null || !json.containsKey("openid")) {
            String errMsg = json == null ? "空响应" : json.getString("errmsg");
            throw new IllegalStateException("获取openid失败:" + errMsg);
        }
        return json.getString("openid");
    }
}
