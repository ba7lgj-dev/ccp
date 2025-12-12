package org.ba7lgj.ccp.miniprogram.service;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.vo.MpTripVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripDetailVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripMemberVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripMyActiveVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripHistoryVO;

public interface MpTripService {
    void publishTrip(MpTripVO vo);

    List<MpTripVO> hallList(Long campusId);

    MpTripDetailVO getTripDetail(Long tripId);

    void joinTrip(Long tripId, Integer joinPeopleCount);

    void quitTrip(Long tripId);

    void kickMember(Long tripId, Long targetUserId);

    void startConfirm(Long tripId);

    void confirmTrip(Long tripId);

    MpTripMyActiveVO getMyActiveTrip();

    List<MpTripHistoryVO> listMyHistoryTrips(Long userId, int limit);

    boolean hasActiveTrip(Long userId);

    boolean hasActiveTripExcludeCurrent(Long userId, Long tripId);
}
