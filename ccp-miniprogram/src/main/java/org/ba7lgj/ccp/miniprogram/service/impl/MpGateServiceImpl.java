package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.ba7lgj.ccp.miniprogram.domain.MpGate;
import org.ba7lgj.ccp.miniprogram.mapper.MpGateMapper;
import org.ba7lgj.ccp.miniprogram.service.MpGateService;
import org.ba7lgj.ccp.miniprogram.vo.MpGateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MpGateServiceImpl implements MpGateService {
    @Autowired
    private MpGateMapper gateMapper;

    @Override
    public List<MpGateVO> listEnabledGatesByCampus(Long campusId) {
        List<MpGate> gates = gateMapper.selectEnabledGatesByCampus(campusId);
        List<MpGateVO> result = new ArrayList<>();
        for (MpGate gate : gates) {
            MpGateVO vo = new MpGateVO();
            vo.setId(gate.getId());
            vo.setGateName(gate.getGateName());
            vo.setLatitude(gate.getLatitude());
            vo.setLongitude(gate.getLongitude());
            vo.setSort(gate.getSort());
            result.add(vo);
        }
        return result;
    }
}
