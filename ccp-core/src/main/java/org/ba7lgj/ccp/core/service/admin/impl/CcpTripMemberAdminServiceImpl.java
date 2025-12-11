package org.ba7lgj.ccp.core.service.admin.impl;

import org.ba7lgj.ccp.common.dto.trip.CcpTripMemberNoShowDTO;
import org.ba7lgj.ccp.common.dto.trip.CcpTripMemberQuery;
import org.ba7lgj.ccp.common.vo.trip.CcpTripMemberVO;
import org.ba7lgj.ccp.core.mapper.CcpTripMemberAdminMapper;
import org.ba7lgj.ccp.core.service.admin.CcpTripMemberAdminService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 管理端拼车成员实现。
 */
@Service
public class CcpTripMemberAdminServiceImpl implements CcpTripMemberAdminService {

    @Resource
    private CcpTripMemberAdminMapper ccpTripMemberAdminMapper;

    @Override
    public List<CcpTripMemberVO> selectMemberList(CcpTripMemberQuery query) {
        return ccpTripMemberAdminMapper.selectMemberList(query);
    }

    @Override
    public int markNoShow(CcpTripMemberNoShowDTO dto) {
        return ccpTripMemberAdminMapper.markNoShow(dto.getMemberId(), dto.getRemark());
    }
}
