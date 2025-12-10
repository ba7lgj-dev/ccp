package org.ba7lgj.ccp.miniprogram.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.ba7lgj.ccp.miniprogram.domain.MpUser;

@Mapper
public interface MpUserMapper {
    MpUser selectByOpenId(String openId);

    int insertUser(MpUser user);

    int updateUserPhone(MpUser user);
}
