package org.ba7lgj.ccp.miniprogram.service;

import org.ba7lgj.ccp.miniprogram.dto.MpUserRealAuthApplyDTO;
import org.ba7lgj.ccp.miniprogram.vo.MpUserRealAuthInfoVO;

public interface MpUserRealAuthService {
    MpUserRealAuthInfoVO getCurrentUserRealAuthInfo(Long userId);

    void applyRealAuth(Long userId, MpUserRealAuthApplyDTO dto);

    void ensureApproved(Long userId);
}
