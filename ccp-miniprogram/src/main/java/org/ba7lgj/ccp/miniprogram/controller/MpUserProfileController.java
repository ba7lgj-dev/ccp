package org.ba7lgj.ccp.miniprogram.controller;

import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.miniprogram.context.MpUserContextHolder;
import org.ba7lgj.ccp.miniprogram.dto.MpUserAvatarUpdateDTO;
import org.ba7lgj.ccp.miniprogram.dto.MpUserProfileUpdateDTO;
import org.ba7lgj.ccp.miniprogram.service.MpUserProfileService;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.ba7lgj.ccp.miniprogram.vo.MpUserProfileVO;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp/user")
public class MpUserProfileController {

    private final MpUserProfileService mpUserProfileService;

    public MpUserProfileController(MpUserProfileService mpUserProfileService) {
        this.mpUserProfileService = mpUserProfileService;
    }

    @GetMapping("/profile")
    public MpResult<MpUserProfileVO> profile() {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "token invalid");
        }
        MiniUser user = mpUserProfileService.getCurrentUserProfile(userId);
        if (user == null) {
            return MpResult.error(4004, "用户不存在");
        }
        return MpResult.ok(toProfileVO(user));
    }

    @PutMapping("/profile")
    public MpResult<MpUserProfileVO> updateProfile(@RequestBody MpUserProfileUpdateDTO dto) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "token invalid");
        }
        MiniUser user = mpUserProfileService.getCurrentUserProfile(userId);
        if (user == null) {
            return MpResult.error(4004, "用户不存在");
        }
        mpUserProfileService.updateUserProfile(userId, dto == null ? null : dto.getNickName(),
                dto == null ? null : dto.getGender(), dto == null ? null : dto.getRealName());
        MiniUser updated = mpUserProfileService.getCurrentUserProfile(userId);
        return MpResult.ok(toProfileVO(updated));
    }

    @PutMapping("/avatar")
    public MpResult<MpUserProfileVO> updateAvatar(@RequestBody MpUserAvatarUpdateDTO dto) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "token invalid");
        }
        if (dto == null || !StringUtils.hasText(dto.getAvatarUrl())) {
            return MpResult.error(400, "avatarUrl不能为空");
        }
        MiniUser user = mpUserProfileService.getCurrentUserProfile(userId);
        if (user == null) {
            return MpResult.error(4004, "用户不存在");
        }
        mpUserProfileService.updateUserAvatar(userId, dto.getAvatarUrl());
        MiniUser updated = mpUserProfileService.getCurrentUserProfile(userId);
        return MpResult.ok(toProfileVO(updated));
    }

    private MpUserProfileVO toProfileVO(MiniUser user) {
        if (user == null) {
            return null;
        }
        MpUserProfileVO vo = new MpUserProfileVO();
        vo.setId(user.getId());
        vo.setNickName(user.getNickName());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setPhone(user.getPhone());
        vo.setRealName(user.getRealName());
        vo.setGender(user.getGender());
        vo.setRealAuthStatus(user.getRealAuthStatus());
        vo.setOnlineStatus(user.getOnlineStatus());
        vo.setLastActiveTime(user.getLastActiveTime());
        vo.setSchoolName(null);
        vo.setCampusName(null);
        return vo;
    }
}
