package org.ba7lgj.ccp.miniprogram.service;

import org.ba7lgj.ccp.miniprogram.domain.MpUser;

public interface MpUserService {
    MpUser findByOpenId(String openId);

    MpUser createUserWithOpenId(String openId);

    void updateUserPhone(Long userId, String phone);

    void refreshActiveInfo(Long userId, java.util.Date lastActiveTime, Integer onlineStatus);
}
