package org.ba7lgj.ccp.miniprogram.controller;

import org.ba7lgj.ccp.miniprogram.domain.User;
import org.ba7lgj.ccp.miniprogram.dto.PhoneBindDTO;
import org.ba7lgj.ccp.miniprogram.dto.WxLoginDTO;
import org.ba7lgj.ccp.miniprogram.service.UserService;
import org.ba7lgj.ccp.miniprogram.service.WxAuthService;
import org.ba7lgj.ccp.miniprogram.util.JwtTokenUtil;
import org.ba7lgj.ccp.miniprogram.vo.LoginVO;
import org.ba7lgj.ccp.miniprogram.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private WxAuthService wxAuthService;
    @Autowired
    private UserService userService;

    @PostMapping("/wxLogin")
    public Result<LoginVO> wxLogin(@RequestBody WxLoginDTO dto) {
        String openId = wxAuthService.getOpenIdByJsCode(dto.getJsCode());
        User user = userService.findByOpenId(openId);
        if (user == null) {
            user = userService.createUserWithOpenId(openId);
        }
        String token = JwtTokenUtil.generateToken(user.getId());
        LoginVO vo = new LoginVO();
        vo.setToken(token);
        vo.setUserId(user.getId());
        vo.setSelectedSchoolId(user.getSelectedSchoolId());
        vo.setSelectedCampusId(user.getSelectedCampusId());
        return Result.ok(vo);
    }

    @PostMapping("/wxPhoneBind")
    public Result<Void> wxPhoneBind(@RequestBody PhoneBindDTO dto,
                                    @RequestHeader(value = "Authorization", required = false) String authorization) {
        // simplified phone bind handling
        String phone = dto.getPhone();
        if (phone == null || phone.isEmpty()) {
            phone = "";
        }
        // Here we can decode encryptedData and iv in future iterations
        // currently assume userId is stored in token parsing
        Long userId = JwtTokenUtil.getCurrentUserId();
        if (userId == null && authorization != null && authorization.startsWith("Bearer ")) {
            userId = JwtTokenUtil.parseUserId(authorization.substring("Bearer ".length()));
        }
        if (userId != null && phone != null && !phone.isEmpty()) {
            userService.updateUserPhone(userId, phone);
        }
        return Result.ok(null);
    }
}
