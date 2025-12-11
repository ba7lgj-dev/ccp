package org.ba7lgj.ccp.core.service.admin.impl;

import org.ba7lgj.ccp.common.dto.trip.CcpTripAdminQuery;
import org.ba7lgj.ccp.common.dto.trip.CcpTripChangeStatusDTO;
import org.ba7lgj.ccp.common.vo.trip.CcpTripAdminDetailVO;
import org.ba7lgj.ccp.common.vo.trip.CcpTripAdminVO;
import org.ba7lgj.ccp.core.mapper.CcpTripAdminMapper;
import org.ba7lgj.ccp.core.service.admin.CcpTripAdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理端拼车订单服务实现。
 */
@Service
public class CcpTripAdminServiceImpl implements CcpTripAdminService {

    @Resource
    private CcpTripAdminMapper ccpTripAdminMapper;

    @Override
    public List<CcpTripAdminVO> selectTripList(CcpTripAdminQuery query) {
        return ccpTripAdminMapper.selectTripList(query);
    }

    @Override
    public CcpTripAdminDetailVO selectTripDetailById(Long id) {
        return ccpTripAdminMapper.selectTripDetailById(id);
    }

    @Override
    public int changeStatus(CcpTripChangeStatusDTO dto) {
        return ccpTripAdminMapper.updateTripStatus(dto.getTripId(), dto.getNewStatus(), dto.getRemark());
    }
}
