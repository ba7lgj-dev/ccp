package org.ba7lgj.ccp.miniprogram.controller;

import java.util.Date;
import com.ruoyi.framework.config.ServerConfig;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.core.service.CoreMiniUserService;
import org.ba7lgj.ccp.miniprogram.dto.MpCode2SessionResponse;
import org.ba7lgj.ccp.miniprogram.dto.MpWxLoginDTO;
import org.ba7lgj.ccp.miniprogram.util.JwtUtil;
import org.ba7lgj.ccp.miniprogram.util.MpWeChatApiService;
import org.ba7lgj.ccp.miniprogram.vo.MpLoginUserInfoVO;
import org.ba7lgj.ccp.miniprogram.vo.MpLoginVO;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp")
public class MpLoginController {

    private final CoreMiniUserService miniUserService;
    private final MpWeChatApiService weChatApiService;
    private final JwtUtil jwtUtil;
    private final ServerConfig serverConfig;

    public MpLoginController(CoreMiniUserService miniUserService, MpWeChatApiService weChatApiService, JwtUtil jwtUtil,
            ServerConfig serverConfig) {
        this.miniUserService = miniUserService;
        this.weChatApiService = weChatApiService;
        this.jwtUtil = jwtUtil;
        this.serverConfig = serverConfig;
    }

    @PostMapping("/login")
    public MpResult<MpLoginVO> login(@RequestBody MpWxLoginDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getJsCode())) {
            return MpResult.error(400, "jsCode不能为空");
        }
        MpCode2SessionResponse session;
        try {
            session = weChatApiService.code2Session(dto.getJsCode());
        } catch (Exception ex) {
            return MpResult.error(500, "微信登录失败：" + ex.getMessage());
        }
        if (session == null || !StringUtils.hasText(session.getOpenId())) {
            return MpResult.error(400, "微信登录失败：openid缺失");
        }
        Date now = new Date();
        MiniUser user = miniUserService.selectMiniUserByOpenId(session.getOpenId());
        if (user == null) {
            user = miniUserService.autoRegisterByOpenId(session.getOpenId());
        }
        MiniUser patch = new MiniUser();
        patch.setId(user.getId());
        patch.setLastActiveTime(now);
        patch.setOnlineStatus(1);
        if (StringUtils.hasText(session.getUnionId())) {
            patch.setUnionId(session.getUnionId());
            user.setUnionId(session.getUnionId());
        }
        miniUserService.updateMiniUser(patch);
        user.setLastActiveTime(now);
        user.setOnlineStatus(1);
        String token = jwtUtil.generateToken(user.getId());
        MpLoginVO vo = new MpLoginVO();
        vo.setToken(token);
        vo.setUserInfo(toLoginUserInfo(user));
        return MpResult.ok(vo);
    }

    private MpLoginUserInfoVO toLoginUserInfo(MiniUser user) {
        if (user == null) {
            return null;
        }
        MpLoginUserInfoVO vo = new MpLoginUserInfoVO();
        vo.setUserId(user.getId());
        vo.setId(user.getId());
        vo.setNickName(user.getNickName());
        vo.setAvatarUrl(buildFullUrl(user.getAvatarUrl()));
        vo.setPhone(user.getPhone());
        vo.setRealAuthStatus(user.getRealAuthStatus() == null ? 0 : user.getRealAuthStatus());
        vo.setRealName(user.getRealName());
        vo.setRealAuthFailReason(StringUtils.hasText(user.getRealAuthFailReason()) ? user.getRealAuthFailReason() : "");
        return vo;
    }

    private String buildFullUrl(String path) {
        if (!StringUtils.hasText(path)) {
            return path;
        }
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }
        String domain = serverConfig.getUrl();
        if (!StringUtils.hasText(domain)) {
            return path;
        }
        if (!path.startsWith("/")) {
            return domain + "/" + path;
        }
        return domain + path;
    }
}
