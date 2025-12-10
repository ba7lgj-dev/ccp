package org.ba7lgj.ccp.miniprogram.service;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.vo.MpGateVO;

public interface MpGateService {
    List<MpGateVO> listEnabledGatesByCampus(Long campusId);
}
