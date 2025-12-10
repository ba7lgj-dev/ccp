package org.ba7lgj.ccp.miniprogram.service;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.vo.MpCampusVO;

public interface MpCampusService {
    List<MpCampusVO> listEnabledCampusBySchool(Long schoolId);
}
