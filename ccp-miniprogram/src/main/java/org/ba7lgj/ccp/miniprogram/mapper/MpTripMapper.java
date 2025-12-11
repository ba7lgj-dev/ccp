package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.ba7lgj.ccp.miniprogram.domain.MpTrip;

@Mapper
public interface MpTripMapper {
    int insertTrip(MpTrip trip);

    List<MpTrip> selectHallTrips(Long campusId, String minTime);
}
