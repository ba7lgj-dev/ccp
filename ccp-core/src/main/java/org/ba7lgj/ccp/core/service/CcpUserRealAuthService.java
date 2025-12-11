package org.ba7lgj.ccp.core.service;

import org.ba7lgj.ccp.common.dto.CcpUserRealAuthQueryDTO;
import org.ba7lgj.ccp.core.domain.vo.CcpUserRealAuthDetailVO;
import org.ba7lgj.ccp.core.domain.vo.CcpUserRealAuthListVO;

import java.util.List;

public interface CcpUserRealAuthService {

    List<CcpUserRealAuthListVO> selectUserRealAuthList(CcpUserRealAuthQueryDTO queryDTO);

    CcpUserRealAuthDetailVO selectUserRealAuthById(Long userId);

    int approveUserRealAuth(Long userId, Long reviewerUserId, String remark);

    int rejectUserRealAuth(Long userId, Long reviewerUserId, String failReason, String remark);
}
