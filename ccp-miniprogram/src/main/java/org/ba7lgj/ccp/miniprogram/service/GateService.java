package org.ba7lgj.ccp.miniprogram.service;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.vo.GateVO;

public interface GateService {
    List<GateVO> listEnabledGatesByCampus(Long campusId);
}
