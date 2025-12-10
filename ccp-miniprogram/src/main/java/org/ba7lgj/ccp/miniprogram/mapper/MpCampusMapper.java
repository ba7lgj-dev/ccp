package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.ba7lgj.ccp.miniprogram.domain.MpCampus;

@Mapper
public interface MpCampusMapper {
    List<MpCampus> selectEnabledCampusBySchool(Long schoolId);
}
