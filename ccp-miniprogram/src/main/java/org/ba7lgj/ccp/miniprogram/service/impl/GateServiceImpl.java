package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.ba7lgj.ccp.miniprogram.domain.Gate;
import org.ba7lgj.ccp.miniprogram.mapper.GateMapper;
import org.ba7lgj.ccp.miniprogram.service.GateService;
import org.ba7lgj.ccp.miniprogram.vo.GateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GateServiceImpl implements GateService {
    @Autowired
    private GateMapper gateMapper;

    @Override
    public List<GateVO> listEnabledGatesByCampus(Long campusId) {
        List<Gate> gates = gateMapper.selectEnabledGatesByCampus(campusId);
        List<GateVO> result = new ArrayList<>();
        for (Gate gate : gates) {
            GateVO vo = new GateVO();
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
