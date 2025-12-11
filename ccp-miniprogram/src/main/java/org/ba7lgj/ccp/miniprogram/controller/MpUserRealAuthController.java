package org.ba7lgj.ccp.miniprogram.controller;

import org.ba7lgj.ccp.miniprogram.context.MpUserContextHolder;
import org.ba7lgj.ccp.miniprogram.dto.MpUserRealAuthApplyDTO;
import org.ba7lgj.ccp.miniprogram.service.MpUserRealAuthService;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.ba7lgj.ccp.miniprogram.vo.MpUserRealAuthInfoVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp/user/realAuth")
public class MpUserRealAuthController {

    private final MpUserRealAuthService realAuthService;

    public MpUserRealAuthController(MpUserRealAuthService realAuthService) {
        this.realAuthService = realAuthService;
    }

    @GetMapping("/info")
    public MpResult<MpUserRealAuthInfoVO> info() {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        MpUserRealAuthInfoVO vo = realAuthService.getCurrentUserRealAuthInfo(userId);
        if (vo == null) {
            return MpResult.error(4004, "用户不存在");
        }
        return MpResult.ok(vo);
    }

    @PostMapping("/apply")
    public MpResult<Void> apply(@RequestBody MpUserRealAuthApplyDTO dto) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        try {
            realAuthService.applyRealAuth(userId, dto);
            return MpResult.ok();
        } catch (IllegalStateException e) {
            return MpResult.error(400, e.getMessage());
        } catch (IllegalArgumentException e) {
            return MpResult.error(400, e.getMessage());
        }
    }
}
