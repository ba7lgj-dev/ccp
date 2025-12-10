package org.ba7lgj.ccp.miniprogram.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ba7lgj.ccp.miniprogram.domain.User;

@Mapper
public interface UserMapper {
    User selectByOpenId(String openId);

    int insertUser(User user);

    int updateUserPhone(User user);
}
