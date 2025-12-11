package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.miniprogram.domain.MpUserSchoolAuth;

@Mapper
public interface MpUserSchoolAuthMapper {
    List<MpUserSchoolAuth> selectByUserId(@Param("userId") Long userId);

    MpUserSchoolAuth selectByUserAndSchool(@Param("userId") Long userId, @Param("schoolId") Long schoolId);

    int insert(MpUserSchoolAuth entity);

    int update(MpUserSchoolAuth entity);

    List<MpUserSchoolAuth> selectApprovedByUser(@Param("userId") Long userId);
}
