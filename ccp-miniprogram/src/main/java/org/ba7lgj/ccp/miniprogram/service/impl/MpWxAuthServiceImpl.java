package org.ba7lgj.ccp.miniprogram.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ba7lgj.ccp.miniprogram.service.MpWxAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MpWxAuthServiceImpl implements MpWxAuthService {
    private static final Logger log = LoggerFactory.getLogger(MpWxAuthServiceImpl.class);
    private static final String WX_LOGIN_URL = "https://api.weixin.qq.com/sns/jscode2session";
    private static final String APP_ID = "demo-app-id";
    private static final String APP_SECRET = "demo-app-secret";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String getOpenIdByJsCode(String jsCode) {
        try {
          String url = WX_LOGIN_URL + "?appid=" + APP_ID + "&secret=" + APP_SECRET + "&js_code=" + jsCode + "&grant_type=authorization_code";
          String body = restTemplate.getForObject(url, String.class);
          if (body == null) {
              throw new RuntimeException("empty wx response");
          }
          JsonNode node = objectMapper.readTree(body);
          if (node.hasNonNull("openid")) {
              return node.get("openid").asText();
          }
          log.warn("wx login failed: {}", body);
          throw new RuntimeException("wx login error");
        } catch (Exception ex) {
            log.warn("failed to get openid, fallback to mock", ex);
            return "mock-openid-" + jsCode;
        }
    }
}
