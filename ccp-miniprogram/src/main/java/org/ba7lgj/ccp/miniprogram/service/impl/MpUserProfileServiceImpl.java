package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.Date;
import javax.annotation.Resource;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.core.service.CoreMiniUserService;
import org.ba7lgj.ccp.miniprogram.service.MpUserProfileService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MpUserProfileServiceImpl implements MpUserProfileService {

    @Resource
    private CoreMiniUserService coreMiniUserService;

    @Override
    public MiniUser getCurrentUserProfile(Long userId) {
        if (userId == null) {
            return null;
        }
        return coreMiniUserService.selectMiniUserById(userId);
    }

    @Override
    public void updateUserProfile(Long userId, String nickName, Integer gender, String realName) {
        MiniUser target = coreMiniUserService.selectMiniUserById(userId);
        if (target == null) {
            return;
        }
        MiniUser patch = new MiniUser();
        patch.setId(userId);
        if (nickName != null) {
            patch.setNickName(StringUtils.hasText(nickName) ? nickName.trim() : "");
        }
        if (gender != null) {
            patch.setGender(gender);
        }
        if (realName != null) {
            patch.setRealName(StringUtils.hasText(realName) ? realName.trim() : "");
        }
        patch.setUpdateTime(new Date());
        coreMiniUserService.updateMiniUser(patch);
    }

    @Override
    public void updateUserAvatar(Long userId, String avatarUrl) {
        MiniUser target = coreMiniUserService.selectMiniUserById(userId);
        if (target == null) {
            return;
        }
        MiniUser patch = new MiniUser();
        patch.setId(userId);
        patch.setAvatarUrl(avatarUrl);
        patch.setUpdateTime(new Date());
        coreMiniUserService.updateMiniUser(patch);
    }
}
