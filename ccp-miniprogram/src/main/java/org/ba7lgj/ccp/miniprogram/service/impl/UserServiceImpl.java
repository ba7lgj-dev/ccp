package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.Date;
import org.ba7lgj.ccp.miniprogram.domain.User;
import org.ba7lgj.ccp.miniprogram.mapper.UserMapper;
import org.ba7lgj.ccp.miniprogram.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByOpenId(String openId) {
        return userMapper.selectByOpenId(openId);
    }

    @Override
    public User createUserWithOpenId(String openId) {
        User user = new User();
        user.setOpenId(openId);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insertUser(user);
        return user;
    }

    @Override
    public void updateUserPhone(Long userId, String phone) {
        User user = new User();
        user.setId(userId);
        user.setPhone(phone);
        user.setUpdateTime(new Date());
        userMapper.updateUserPhone(user);
    }
}
