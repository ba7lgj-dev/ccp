package org.ba7lgj.ccp.miniprogram.service;

import org.ba7lgj.ccp.common.domain.MiniUser;

public interface MpUserProfileService {
    MiniUser getCurrentUserProfile(Long userId);

    void updateUserProfile(Long userId, String nickName, Integer gender, String realName);

    void updateUserAvatar(Long userId, String avatarUrl);
}
