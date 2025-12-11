package org.ba7lgj.ccp.core.service.admin;

import org.ba7lgj.ccp.common.dto.trip.CcpTripAdminQuery;
import org.ba7lgj.ccp.common.dto.trip.CcpTripChangeStatusDTO;
import org.ba7lgj.ccp.common.vo.trip.CcpTripAdminDetailVO;
import org.ba7lgj.ccp.common.vo.trip.CcpTripAdminVO;

import java.util.List;

/**
 * 管理端拼车订单服务。
 */
public interface CcpTripAdminService {

    List<CcpTripAdminVO> selectTripList(CcpTripAdminQuery query);

    CcpTripAdminDetailVO selectTripDetailById(Long id);

    int changeStatus(CcpTripChangeStatusDTO dto);
}
