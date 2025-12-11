package org.ba7lgj.ccp.miniprogram.service;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.dto.MpUserSchoolAuthApplyDTO;
import org.ba7lgj.ccp.miniprogram.vo.MpUserApprovedSchoolVO;
import org.ba7lgj.ccp.miniprogram.vo.MpUserSchoolAuthVO;

public interface MpUserSchoolAuthService {
    List<MpUserSchoolAuthVO> listMyAuth(Long userId);

    MpUserSchoolAuthVO getDetail(Long userId, Long schoolId);

    void apply(Long userId, MpUserSchoolAuthApplyDTO dto);

    List<MpUserApprovedSchoolVO> listApproved(Long userId);
}
