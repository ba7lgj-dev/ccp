package org.ba7lgj.ccp.core.service.admin.impl;

import org.ba7lgj.ccp.common.dto.trip.CcpTripMemberNoShowDTO;
import org.ba7lgj.ccp.common.dto.trip.CcpTripMemberQuery;
import org.ba7lgj.ccp.common.vo.trip.CcpTripMemberVO;
import org.ba7lgj.ccp.core.service.admin.CcpTripMemberAdminService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 管理端拼车成员占位实现。
 */
@Service
public class CcpTripMemberAdminServiceImpl implements CcpTripMemberAdminService {

    @Override
    public List<CcpTripMemberVO> selectMemberList(CcpTripMemberQuery query) {
        return new ArrayList<>();
    }

    @Override
    public int markNoShow(CcpTripMemberNoShowDTO dto) {
        return 1;
    }
}
