package org.ba7lgj.ccp.miniprogram.service;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.vo.MpTripVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripDetailVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripMemberVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripMyActiveVO;

public interface MpTripService {
    void publishTrip(MpTripVO vo);

    List<MpTripVO> hallList(Long campusId);

    MpTripDetailVO getTripDetail(Long tripId);

    void joinTrip(Long tripId, Integer joinPeopleCount);

    void quitTrip(Long tripId);

    void kickMember(Long tripId, Long targetUserId);

    MpTripMyActiveVO getMyActiveTrip();
}
