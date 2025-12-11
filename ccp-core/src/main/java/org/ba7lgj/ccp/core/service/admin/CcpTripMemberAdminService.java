package org.ba7lgj.ccp.core.service.admin;

import org.ba7lgj.ccp.common.dto.trip.CcpTripMemberNoShowDTO;
import org.ba7lgj.ccp.common.dto.trip.CcpTripMemberQuery;
import org.ba7lgj.ccp.common.vo.trip.CcpTripMemberVO;

import java.util.List;

/**
 * 管理端拼车成员服务。
 */
public interface CcpTripMemberAdminService {

    List<CcpTripMemberVO> selectMemberList(CcpTripMemberQuery query);

    List<CcpTripMemberVO> selectMemberListByTrip(CcpTripMemberQuery query);

    int markNoShow(CcpTripMemberNoShowDTO dto);
}
