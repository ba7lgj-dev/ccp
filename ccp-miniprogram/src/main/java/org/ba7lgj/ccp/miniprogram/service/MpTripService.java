package org.ba7lgj.ccp.miniprogram.service;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.vo.MpTripVO;

public interface MpTripService {
    void publishTrip(MpTripVO vo);

    List<MpTripVO> hallList(Long campusId);
}
