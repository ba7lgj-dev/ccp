package org.ba7lgj.ccp.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.common.dto.trip.CcpTripMemberQuery;
import org.ba7lgj.ccp.common.vo.trip.CcpTripMemberVO;

import java.util.List;

/**
 * 拼车成员后台 Mapper。
 */
public interface CcpTripMemberAdminMapper {

    List<CcpTripMemberVO> selectMemberList(CcpTripMemberQuery query);

    int markNoShow(@Param("memberId") Long memberId, @Param("remark") String remark);
}
