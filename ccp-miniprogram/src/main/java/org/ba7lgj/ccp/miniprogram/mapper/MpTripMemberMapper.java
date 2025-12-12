package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.miniprogram.domain.MpTripMember;

@Mapper
public interface MpTripMemberMapper {
    MpTripMember selectByTripIdAndUserId(@Param("tripId") Long tripId, @Param("userId") Long userId);

    List<MpTripMember> selectByTripId(@Param("tripId") Long tripId);

    int insertMember(MpTripMember member);

    int updateMember(MpTripMember member);

    int countActiveTripJoined(@Param("userId") Long userId);

    int updateStatusByTrip(@Param("tripId") Long tripId, @Param("status") Integer status, @Param("updateTime") java.util.Date updateTime);

    int resetConfirmFlagByTrip(@Param("tripId") Long tripId, @Param("confirmFlag") Integer confirmFlag,
        @Param("updateTime") java.util.Date updateTime);

    int updateConfirmFlagForUser(@Param("tripId") Long tripId, @Param("userId") Long userId,
        @Param("confirmFlag") Integer confirmFlag, @Param("updateTime") java.util.Date updateTime);

    int countActiveMembers(@Param("tripId") Long tripId);

    int countUnconfirmedMembers(@Param("tripId") Long tripId);

    List<Long> selectTripIdsByUserAndStatus(@Param("userId") Long userId, @Param("statuses") List<Integer> statuses);
}
