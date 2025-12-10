package org.ba7lgj.ccp.core.service;

import org.ba7lgj.ccp.common.dto.GateDTO;
import org.ba7lgj.ccp.common.dto.GateQueryDTO;
import org.ba7lgj.ccp.common.vo.GateVO;

import java.util.List;

/**
 * 校门管理服务。
 */
public interface CoreGateService {

    GateVO selectGateById(Long id);

    List<GateVO> selectGateList(GateQueryDTO queryDTO);

    boolean checkGateUnique(Long campusId, String gateName, Long excludeId);

    int insertGate(GateDTO dto);

    int updateGate(GateDTO dto);

    int changeStatus(Long id, Integer status);

    int deleteGateByIds(Long[] ids);
}
