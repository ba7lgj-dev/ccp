package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.ba7lgj.ccp.miniprogram.domain.MpLocation;

@Mapper
public interface MpLocationMapper {
    List<MpLocation> selectEnabledByCampus(Long campusId);
}
