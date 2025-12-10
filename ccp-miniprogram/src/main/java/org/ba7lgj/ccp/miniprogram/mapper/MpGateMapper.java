package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.ba7lgj.ccp.miniprogram.domain.MpGate;

@Mapper
public interface MpGateMapper {
    List<MpGate> selectEnabledGatesByCampus(Long campusId);
}
