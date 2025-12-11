package org.ba7lgj.ccp.miniprogram.mapper;

import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.miniprogram.domain.MpUserCampusAuth;

public interface MpUserCampusAuthMapper {
    MpUserCampusAuth selectByUserAndCampus(@Param("userId") Long userId, @Param("campusId") Long campusId);

    int insertAuth(MpUserCampusAuth auth);

    int updateAuth(MpUserCampusAuth auth);
}
