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
}
