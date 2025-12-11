package org.ba7lgj.ccp.miniprogram.service;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.vo.MpLocationVO;

public interface MpLocationService {
    List<MpLocationVO> listByCampus(Long campusId);
}
