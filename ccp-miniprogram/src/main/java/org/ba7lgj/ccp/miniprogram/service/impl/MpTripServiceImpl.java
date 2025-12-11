package org.ba7lgj.ccp.miniprogram.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.ba7lgj.ccp.miniprogram.context.MpUserContextHolder;
import org.ba7lgj.ccp.miniprogram.domain.MpCampus;
import org.ba7lgj.ccp.miniprogram.domain.MpTrip;
import org.ba7lgj.ccp.miniprogram.mapper.MpCampusMapper;
import org.ba7lgj.ccp.miniprogram.mapper.MpTripMapper;
import org.ba7lgj.ccp.miniprogram.service.MpTripService;
import org.ba7lgj.ccp.miniprogram.vo.MpTripVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MpTripServiceImpl implements MpTripService {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";

    @Autowired
    private MpTripMapper tripMapper;

    @Autowired
    private MpCampusMapper campusMapper;

    @Override
    public void publishTrip(MpTripVO vo) {
        MpCampus campus = campusMapper.selectCampusById(vo.getCampusId());
        if (campus == null) {
            throw new IllegalArgumentException("校区不存在");
        }
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            throw new IllegalStateException("未获取到用户信息");
        }
        MpTrip trip = new MpTrip();
        trip.setSchoolId(campus.getSchoolId());
        trip.setCampusId(vo.getCampusId());
        trip.setOwnerUserId(userId);
        trip.setStartGateId(vo.getStartGateId());
        trip.setStartLocationId(vo.getStartLocationId());
        trip.setStartAddress(vo.getStartAddress());
        trip.setEndGateId(vo.getEndGateId());
        trip.setEndLocationId(vo.getEndLocationId());
        trip.setEndAddress(vo.getEndAddress());
        trip.setOwnerPeopleCount(vo.getOwnerPeopleCount());
        trip.setTotalPeople(vo.getTotalPeople());
        trip.setCurrentPeople(vo.getOwnerPeopleCount());
        trip.setRequireText(vo.getRequireText());
        trip.setStatus(0);
        if (StringUtils.hasText(vo.getDepartureTime())) {
            try {
                Date dep = new SimpleDateFormat(DATE_PATTERN).parse(vo.getDepartureTime());
                trip.setDepartureTime(dep);
            } catch (ParseException e) {
                trip.setDepartureTime(new Date());
            }
        } else {
            trip.setDepartureTime(new Date());
        }
        tripMapper.insertTrip(trip);
    }

    @Override
    public List<MpTripVO> hallList(Long campusId) {
        String nowStr = new SimpleDateFormat(DATE_PATTERN).format(new Date(System.currentTimeMillis() - 10 * 60 * 1000));
        List<MpTrip> list = tripMapper.selectHallTrips(campusId, nowStr);
        List<MpTripVO> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        for (MpTrip trip : list) {
            MpTripVO vo = new MpTripVO();
            vo.setId(trip.getId());
            vo.setStartAddress(trip.getStartAddress());
            vo.setEndAddress(trip.getEndAddress());
            vo.setDepartureTime(trip.getDepartureTime() != null ? sdf.format(trip.getDepartureTime()) : null);
            vo.setCurrentPeople(trip.getCurrentPeople());
            vo.setOwnerPeopleCount(trip.getOwnerPeopleCount());
            vo.setTotalPeople(trip.getTotalPeople());
            vo.setRequireText(trip.getRequireText());
            vo.setStatus(trip.getStatus());
            result.add(vo);
        }
        return result;
    }
}
