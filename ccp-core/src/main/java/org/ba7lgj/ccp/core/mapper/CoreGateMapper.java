package org.ba7lgj.ccp.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.common.domain.Gate;
import org.ba7lgj.ccp.common.dto.GateQueryDTO;
import org.ba7lgj.ccp.common.vo.GateVO;

import java.util.List;

/**
 * 校门管理 Mapper。
 */
public interface CoreGateMapper {

    GateVO selectGateById(Long id);

    List<GateVO> selectGateList(GateQueryDTO queryDTO);

    Gate selectGateByName(@Param("campusId") Long campusId, @Param("gateName") String gateName);

    int insertGate(Gate gate);

    int updateGate(Gate gate);

    int changeStatus(@Param("id") Long id, @Param("status") Integer status);

    int deleteGateByIds(Long[] ids);
}
