package org.ba7lgj.ccp.core.service.impl;

import org.ba7lgj.ccp.common.domain.Gate;
import org.ba7lgj.ccp.common.dto.GateDTO;
import org.ba7lgj.ccp.common.dto.GateQueryDTO;
import org.ba7lgj.ccp.common.vo.GateVO;
import org.ba7lgj.ccp.core.mapper.CoreGateMapper;
import org.ba7lgj.ccp.core.service.CoreGateService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 校门管理服务实现。
 */
@Service
public class CoreGateServiceImpl implements CoreGateService {

    @Resource
    private CoreGateMapper gateMapper;

    @Override
    public GateVO selectGateById(Long id) {
        return gateMapper.selectGateById(id);
    }

    @Override
    public List<GateVO> selectGateList(GateQueryDTO queryDTO) {
        return gateMapper.selectGateList(queryDTO);
    }

    @Override
    public boolean checkGateUnique(Long campusId, String gateName, Long excludeId) {
        if (campusId == null || !StringUtils.hasText(gateName)) {
            return true;
        }
        Gate exist = gateMapper.selectGateByName(campusId, gateName);
        return exist == null || (excludeId != null && excludeId.equals(exist.getId()));
    }

    @Override
    public int insertGate(GateDTO dto) {
        Gate gate = new Gate();
        BeanUtils.copyProperties(dto, gate);
        gate.setCreateTime(new Date());
        return gateMapper.insertGate(gate);
    }

    @Override
    public int updateGate(GateDTO dto) {
        Gate gate = new Gate();
        BeanUtils.copyProperties(dto, gate);
        gate.setUpdateTime(new Date());
        return gateMapper.updateGate(gate);
    }

    @Override
    public int changeStatus(Long id, Integer status) {
        return gateMapper.changeStatus(id, status);
    }

    @Override
    public int deleteGateByIds(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return gateMapper.deleteGateByIds(ids);
    }
}
