package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.Date;
import org.ba7lgj.ccp.miniprogram.domain.MpUser;
import org.ba7lgj.ccp.miniprogram.mapper.MpUserMapper;
import org.ba7lgj.ccp.miniprogram.service.MpUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MpUserServiceImpl implements MpUserService {
    @Autowired
    private MpUserMapper userMapper;

    @Override
    public MpUser findByOpenId(String openId) {
        return userMapper.selectByOpenId(openId);
    }

    @Override
    public MpUser createUserWithOpenId(String openId) {
        MpUser user = new MpUser();
        user.setOpenId(openId);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insertUser(user);
        return user;
    }

    @Override
    public void updateUserPhone(Long userId, String phone) {
        MpUser user = new MpUser();
        user.setId(userId);
        user.setPhone(phone);
        user.setUpdateTime(new Date());
        userMapper.updateUserPhone(user);
    }
}
