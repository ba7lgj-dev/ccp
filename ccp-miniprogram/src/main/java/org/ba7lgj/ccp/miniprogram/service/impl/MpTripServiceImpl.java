package org.ba7lgj.ccp.miniprogram.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
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
    private static final long TEN_MINUTES_MILLIS = 10 * 60 * 1000L;

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
        Date now = new Date();
        boolean immediateFlag = Boolean.TRUE.equals(vo.getImmediate());
        Date departureTime = now;
        if (!immediateFlag) {
            departureTime = parseDepartureTime(vo.getDepartureTime());
            long diffMinutes = (departureTime.getTime() - now.getTime()) / (60 * 1000);
            if (diffMinutes <= 5) {
                immediateFlag = true;
                departureTime = now;
            } else if (departureTime.before(now)) {
                throw new IllegalArgumentException("出发时间不能早于当前时间");
            }
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
        trip.setDepartureTime(departureTime);
        tripMapper.insertTrip(trip);
    }

    @Override
    public List<MpTripVO> hallList(Long campusId) {
        String nowStr = new SimpleDateFormat(DATE_PATTERN).format(new Date(System.currentTimeMillis() - 10 * 60 * 1000));
        List<MpTrip> list = tripMapper.selectHallTrips(campusId, nowStr);
        List<MpTripVO> result = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        List<MpTrip> activeTrips = new ArrayList<>();
        for (MpTrip trip : list) {
            checkExpireIfNeeded(trip);
            if (!Objects.equals(trip.getStatus(), 5)) {
                activeTrips.add(trip);
            }
        }
        activeTrips.sort((a, b) -> {
            boolean aImmediate = isImmediateTrip(a);
            boolean bImmediate = isImmediateTrip(b);
            if (aImmediate && !bImmediate) {
                return -1;
            }
            if (!aImmediate && bImmediate) {
                return 1;
            }
            if (aImmediate && bImmediate) {
                long remainA = remainingMillis(a);
                long remainB = remainingMillis(b);
                if (remainA != remainB) {
                    return Long.compare(remainA, remainB);
                }
            }
            Date depA = a.getDepartureTime();
            Date depB = b.getDepartureTime();
            if (depA == null && depB == null) {
                return 0;
            }
            if (depA == null) {
                return 1;
            }
            if (depB == null) {
                return -1;
            }
            return depA.compareTo(depB);
        });
        for (MpTrip trip : activeTrips) {
            MpTripVO vo = new MpTripVO();
            vo.setId(trip.getId());
            vo.setStartAddress(trip.getStartAddress());
            vo.setEndAddress(trip.getEndAddress());
            vo.setDepartureTime(trip.getDepartureTime() != null ? sdf.format(trip.getDepartureTime()) : null);
            vo.setImmediate(isImmediateTrip(trip));
            vo.setCurrentPeople(trip.getCurrentPeople());
            vo.setOwnerPeopleCount(trip.getOwnerPeopleCount());
            vo.setTotalPeople(trip.getTotalPeople());
            vo.setRequireText(trip.getRequireText());
            vo.setStatus(trip.getStatus());
            result.add(vo);
        }
        return result;
    }

    private Date parseDepartureTime(String departureTime) {
        if (!StringUtils.hasText(departureTime)) {
            throw new IllegalArgumentException("预约出发时间不能为空");
        }
        try {
            return new SimpleDateFormat(DATE_PATTERN).parse(departureTime);
        } catch (ParseException e) {
            throw new IllegalArgumentException("出发时间格式不正确");
        }
    }

    private boolean isImmediateTrip(MpTrip trip) {
        if (trip == null || trip.getDepartureTime() == null) {
            return false;
        }
        return trip.getDepartureTime().getTime() <= System.currentTimeMillis();
    }

    private long remainingMillis(MpTrip trip) {
        if (trip == null || trip.getDepartureTime() == null) {
            return Long.MAX_VALUE;
        }
        return trip.getDepartureTime().getTime() + TEN_MINUTES_MILLIS - System.currentTimeMillis();
    }

    private void checkExpireIfNeeded(MpTrip trip) {
        if (trip == null || trip.getDepartureTime() == null) {
            return;
        }
        if (!isImmediateTrip(trip)) {
            return;
        }
        Date expireAt = new Date(trip.getDepartureTime().getTime() + TEN_MINUTES_MILLIS);
        if (expireAt.before(new Date()) && !Objects.equals(trip.getStatus(), 5)) {
            trip.setStatus(5);
            tripMapper.updateTripStatus(trip.getId(), 5, new Date());
        }
    }
}
