package org.ba7lgj.ccp.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.common.dto.trip.CcpTripAdminQuery;
import org.ba7lgj.ccp.common.vo.trip.CcpTripAdminDetailVO;
import org.ba7lgj.ccp.common.vo.trip.CcpTripAdminVO;

import java.util.List;

/**
 * 拼车订单后台 Mapper。
 */
public interface CcpTripAdminMapper {

    List<CcpTripAdminVO> selectTripList(CcpTripAdminQuery query);

    CcpTripAdminDetailVO selectTripDetailById(@Param("id") Long id);

    int updateTripStatus(@Param("id") Long id, @Param("status") Integer status, @Param("remark") String remark);
}
