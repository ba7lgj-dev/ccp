package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import java.util.Date;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.miniprogram.domain.MpTrip;

@Mapper
public interface MpTripMapper {
    int insertTrip(MpTrip trip);

    List<MpTrip> selectHallTrips(@Param("campusId") Long campusId, @Param("minTime") String minTime);

    int updateTripStatus(@Param("id") Long id, @Param("status") Integer status, @Param("updateTime") Date updateTime);

    int updateConfirmStart(@Param("id") Long id, @Param("status") Integer status, @Param("confirmStartTime") Date confirmStartTime,
        @Param("updateTime") Date updateTime);

    int updateConfirmDone(@Param("id") Long id, @Param("status") Integer status, @Param("confirmedTime") Date confirmedTime,
        @Param("updateTime") Date updateTime);

    MpTrip selectById(@Param("id") Long id);

    int updateTripCurrentPeople(@Param("id") Long id, @Param("currentPeople") Integer currentPeople, @Param("updateTime") Date updateTime);

    List<MpTrip> selectActiveTripsByUser(@Param("userId") Long userId);

    int countActiveTripByUser(@Param("userId") Long userId);

    int countActiveTripByUserExcludeTrip(@Param("userId") Long userId, @Param("tripId") Long tripId);

    List<MpTrip> selectHistoryTripsByUser(@Param("userId") Long userId, @Param("now") Date now, @Param("limit") int limit);
}

