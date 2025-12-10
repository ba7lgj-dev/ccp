package org.ba7lgj.ccp.miniprogram.service;

import org.ba7lgj.ccp.miniprogram.domain.User;

public interface UserService {
    User findByOpenId(String openId);

    User createUserWithOpenId(String openId);

    void updateUserPhone(Long userId, String phone);
}
