package org.ba7lgj.ccp.miniprogram.service;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.vo.CampusVO;

public interface CampusService {
    List<CampusVO> listEnabledCampusBySchool(Long schoolId);
}
