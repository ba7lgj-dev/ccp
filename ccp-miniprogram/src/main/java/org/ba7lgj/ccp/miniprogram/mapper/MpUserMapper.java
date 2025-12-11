package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.miniprogram.domain.MpUser;

@Mapper
public interface MpUserMapper {
    MpUser selectByOpenId(String openId);

    MpUser selectById(Long id);

    int insertUser(MpUser user);

    int updateUser(MpUser user);

    List<MpUser> selectByIds(@Param("ids") List<Long> ids);
}
