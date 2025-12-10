package org.ba7lgj.ccp.miniprogram.controller;

import org.ba7lgj.ccp.miniprogram.domain.MpUser;
import org.ba7lgj.ccp.miniprogram.dto.MpPhoneBindDTO;
import org.ba7lgj.ccp.miniprogram.dto.MpWxLoginDTO;
import org.ba7lgj.ccp.miniprogram.service.MpUserService;
import org.ba7lgj.ccp.miniprogram.service.MpWxAuthService;
import org.ba7lgj.ccp.miniprogram.util.MpJwtTokenUtil;
import org.ba7lgj.ccp.miniprogram.vo.MpLoginVO;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/mp/auth")
public class MpAuthController {
    @Autowired
    private MpWxAuthService wxAuthService;
    @Autowired
    private MpUserService userService;

    @PostMapping("/wxLogin")
    public MpResult<MpLoginVO> wxLogin(@RequestBody MpWxLoginDTO dto) {
        String openId = wxAuthService.getOpenIdByJsCode(dto.getJsCode());
        MpUser user = userService.findByOpenId(openId);
        if (user == null) {
            user = userService.createUserWithOpenId(openId);
        }
        Date now = new Date();
        userService.refreshActiveInfo(user.getId(), now, 1);
        user.setLastActiveTime(now);
        user.setOnlineStatus(1);
        String token = MpJwtTokenUtil.generateToken(user.getId());
        MpLoginVO vo = new MpLoginVO();
        vo.setToken(token);
        vo.setUser(user);
        return MpResult.ok(vo);
    }

    @PostMapping("/wxPhoneBind")
    public MpResult<Void> wxPhoneBind(@RequestBody MpPhoneBindDTO dto,
                                    @RequestHeader(value = "Authorization", required = false) String authorization) {
        // simplified phone bind handling
        String phone = dto.getPhone();
        if (phone == null || phone.isEmpty()) {
            phone = "";
        }
        // Here we can decode encryptedData and iv in future iterations
        // currently assume userId is stored in token parsing
        Long userId = MpJwtTokenUtil.getCurrentUserId();
        if (userId == null && authorization != null && authorization.startsWith("Bearer ")) {
            userId = MpJwtTokenUtil.parseUserId(authorization.substring("Bearer ".length()));
        }
        if (userId != null && phone != null && !phone.isEmpty()) {
            userService.updateUserPhone(userId, phone);
        }
        return MpResult.ok(null);
    }
}
