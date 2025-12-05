package org.ba7lgj.ccp.miniprogram.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.framework.web.service.TokenService;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.vo.MiniUserVO;
import org.ba7lgj.ccp.core.service.IMiniUserService;
import org.ba7lgj.ccp.miniprogram.util.WeChatApiService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 小程序登录接口。
 */
@RestController
@RequestMapping("/ccp/app")
public class CcpAppLoginController {

    @Resource
    private IMiniUserService miniUserService;

    @Resource
    private WeChatApiService weChatApiService;

    @Resource
    private TokenService tokenService;

    @PostMapping("/login")
    public AjaxResult login(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        String openId = weChatApiService.getOpenId(code);
        MiniUser miniUser = miniUserService.selectMiniUserByOpenId(openId);
        if (miniUser == null) {
            miniUser = miniUserService.autoRegisterByOpenId(openId);
        }
        SysUser sysUser = new SysUser();
        sysUser.setUserId(miniUser.getId());
        sysUser.setUserName("ccp_" + miniUser.getId());
        LoginUser loginUser = new LoginUser(sysUser, Collections.emptySet());
        String token = tokenService.createToken(loginUser);

        MiniUserVO vo = new MiniUserVO();
        BeanUtils.copyProperties(miniUser, vo);
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", vo);
        return AjaxResult.success(result);
    }
}
