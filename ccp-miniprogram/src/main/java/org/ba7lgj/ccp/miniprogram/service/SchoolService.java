package org.ba7lgj.ccp.miniprogram.service;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.vo.SchoolVO;

public interface SchoolService {
    List<SchoolVO> listAllEnabledSchools();
}
