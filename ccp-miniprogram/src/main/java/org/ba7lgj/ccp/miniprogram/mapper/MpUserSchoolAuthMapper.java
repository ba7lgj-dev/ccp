package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.miniprogram.domain.MpUserSchoolAuth;
import org.ba7lgj.ccp.miniprogram.vo.MpUserSchoolAuthVO;

public interface MpUserSchoolAuthMapper {
    MpUserSchoolAuth selectByUserAndSchool(@Param("userId") Long userId, @Param("schoolId") Long schoolId);

    List<MpUserSchoolAuth> selectApprovedByUser(@Param("userId") Long userId);

    List<MpUserSchoolAuthVO> selectDetailListByUser(@Param("userId") Long userId);

    MpUserSchoolAuthVO selectDetailByUserAndSchool(@Param("userId") Long userId, @Param("schoolId") Long schoolId);

    int insertAuth(MpUserSchoolAuth auth);

    int updateAuth(MpUserSchoolAuth auth);
}
