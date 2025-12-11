package org.ba7lgj.ccp.miniprogram.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.ba7lgj.ccp.miniprogram.context.MpUserContextHolder;
import org.ba7lgj.ccp.miniprogram.domain.MpCampus;
import org.ba7lgj.ccp.miniprogram.domain.MpTrip;
import org.ba7lgj.ccp.miniprogram.domain.MpTripMember;
import org.ba7lgj.ccp.miniprogram.domain.MpUser;
import org.ba7lgj.ccp.miniprogram.mapper.MpTripMemberMapper;
import org.ba7lgj.ccp.miniprogram.mapper.MpCampusMapper;
import org.ba7lgj.ccp.miniprogram.mapper.MpTripMapper;
import org.ba7lgj.ccp.miniprogram.mapper.MpUserMapper;
import org.ba7lgj.ccp.miniprogram.service.MpUserSchoolAuthService;
import org.ba7lgj.ccp.miniprogram.service.MpTripService;
import org.ba7lgj.ccp.miniprogram.vo.MpTripDetailCurrentUserInfo;
import org.ba7lgj.ccp.miniprogram.vo.MpTripDetailVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripMemberVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripMyActiveVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripHistoryVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MpTripServiceImpl implements MpTripService {
    private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final long TEN_MINUTES_MILLIS = 10 * 60 * 1000L;
    private static final Map<Integer, String> STATUS_TEXT;

    static {
        Map<Integer, String> map = new HashMap<>();
        map.put(0, "招募中");
        map.put(1, "待确认");
        map.put(2, "已确认");
        map.put(3, "已完成");
        map.put(4, "已取消");
        map.put(5, "已过期");
        STATUS_TEXT = Collections.unmodifiableMap(map);
    }

    @Autowired
    private MpTripMapper tripMapper;

    @Autowired
    private MpCampusMapper campusMapper;

    @Autowired
    private MpTripMemberMapper tripMemberMapper;

    @Autowired
    private MpUserMapper userMapper;

    @Autowired
    private MpUserSchoolAuthService userSchoolAuthService;

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
        ensureCampusAuth(userId, vo.getCampusId());
        if (hasActiveTrip(userId)) {
            throw new IllegalStateException("你当前已有进行中的拼车，请先完成或退出后再发起新的拼车");
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
        MpTripMember ownerMember = new MpTripMember();
        ownerMember.setTripId(trip.getId());
        ownerMember.setUserId(userId);
        ownerMember.setRole(1);
        ownerMember.setJoinPeopleCount(vo.getOwnerPeopleCount());
        ownerMember.setStatus(1);
        ownerMember.setConfirmFlag(1);
        ownerMember.setJoinTime(now);
        ownerMember.setCreateTime(now);
        ownerMember.setUpdateTime(now);
        tripMemberMapper.insertMember(ownerMember);
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

    @Override
    public MpTripDetailVO getTripDetail(Long tripId) {
        MpTrip trip = tripMapper.selectById(tripId);
        if (trip == null) {
            return null;
        }
        checkExpireIfNeeded(trip);
        Long userId = MpUserContextHolder.getUserId();
        MpTripDetailVO vo = new MpTripDetailVO();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        vo.setTripId(trip.getId());
        vo.setSchoolId(trip.getSchoolId());
        vo.setCampusId(trip.getCampusId());
        vo.setOwnerUserId(trip.getOwnerUserId());
        vo.setStartAddress(Optional.ofNullable(trip.getStartAddress()).orElse("未填写起点"));
        vo.setEndAddress(Optional.ofNullable(trip.getEndAddress()).orElse("未填写终点"));
        vo.setDepartureTime(trip.getDepartureTime() != null ? sdf.format(trip.getDepartureTime()) : null);
        vo.setStatus(trip.getStatus());
        vo.setStatusText(STATUS_TEXT.get(trip.getStatus()));
        vo.setTotalPeople(trip.getTotalPeople());
        vo.setOwnerPeopleCount(trip.getOwnerPeopleCount());
        vo.setCurrentPeople(trip.getCurrentPeople());
        vo.setRemainPeople(trip.getTotalPeople() != null && trip.getCurrentPeople() != null
            ? Math.max(0, trip.getTotalPeople() - trip.getCurrentPeople()) : null);
        vo.setRequireText(trip.getRequireText());
        vo.setImmediateTrip(isImmediateTrip(trip));
        vo.setRemainMinutesForImmediate(calculateRemainMinutes(trip));
        vo.setDepartureTimeDisplay(buildDepartureDisplay(trip));
        vo.setUnreadChatCount(0);
        vo.setNeedRedirect(trip.getStatus() != null && (trip.getStatus() == 3 || trip.getStatus() == 4 || trip.getStatus() == 5));

        List<MpTripMember> members = tripMemberMapper.selectByTripId(tripId);
        Map<Long, MpUser> userMap = fetchUserMap(members, trip.getOwnerUserId());
        List<MpTripMemberVO> memberVOS = new ArrayList<>();
        MpTripMember currentMember = null;
        for (MpTripMember member : members) {
            if (userId != null && userId.equals(member.getUserId())) {
                currentMember = member;
            }
            MpTripMemberVO mvo = new MpTripMemberVO();
            mvo.setUserId(member.getUserId());
            MpUser u = userMap.get(member.getUserId());
            mvo.setNickName(u != null ? u.getNickName() : "成员");
            mvo.setAvatarUrl(u != null ? u.getAvatarUrl() : null);
            mvo.setRole(member.getRole());
            mvo.setJoinPeopleCount(member.getJoinPeopleCount());
            mvo.setMemberStatus(member.getStatus());
            mvo.setConfirmFlag(member.getConfirmFlag());
            mvo.setCurrentUser(userId != null && userId.equals(member.getUserId()));
            memberVOS.add(mvo);
        }
        vo.setMembers(memberVOS);

        MpUser owner = userMap.get(trip.getOwnerUserId());
        vo.setOwnerNickName(owner != null ? owner.getNickName() : null);
        vo.setOwnerAvatar(owner != null ? owner.getAvatarUrl() : null);

        MpTripDetailCurrentUserInfo currentUserInfo = new MpTripDetailCurrentUserInfo();
        boolean isOwner = userId != null && userId.equals(trip.getOwnerUserId());
        boolean joined = currentMember != null && Objects.equals(currentMember.getStatus(), 1);
        currentUserInfo.setOwner(isOwner);
        currentUserInfo.setJoined(joined);
        currentUserInfo.setMemberStatus(currentMember != null ? currentMember.getStatus() : null);
        boolean activeFull = vo.getRemainPeople() != null && vo.getRemainPeople() <= 0;
        boolean hasOtherActive = userId != null && hasActiveTripExcludeCurrent(userId, tripId);
        boolean canJoin = trip.getStatus() != null && trip.getStatus() == 0 && !activeFull && !joined && !isOwner && !hasOtherActive;
        boolean canQuit = trip.getStatus() != null && trip.getStatus() == 0 && joined && !isOwner;
        boolean canKick = trip.getStatus() != null && trip.getStatus() == 0 && isOwner;
        currentUserInfo.setCanJoin(canJoin);
        currentUserInfo.setCanQuit(canQuit);
        currentUserInfo.setCanKick(canKick);
        vo.setCurrentUserInfo(currentUserInfo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void joinTrip(Long tripId, Integer joinPeopleCount) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            throw new IllegalStateException("未登录");
        }
        if (joinPeopleCount == null || joinPeopleCount < 1) {
            throw new IllegalArgumentException("人数至少1人");
        }
        MpTrip trip = tripMapper.selectById(tripId);
        if (trip == null) {
            throw new IllegalArgumentException("拼单不存在");
        }
        checkExpireIfNeeded(trip);
        if (!Objects.equals(trip.getStatus(), 0)) {
            throw new IllegalStateException("当前状态不可加入");
        }
        ensureCampusAuth(userId, trip.getCampusId());
        if (isExpiredImmediateTrip(trip)) {
            tripMapper.updateTripStatus(trip.getId(), 5, new Date());
            throw new IllegalStateException("订单已过期");
        }
        MpTripMember member = tripMemberMapper.selectByTripIdAndUserId(tripId, userId);
        if (member != null && Objects.equals(member.getStatus(), 1)) {
            return;
        }
        if (hasActiveTripExcludeCurrent(userId, tripId)) {
            throw new IllegalStateException("你当前已在其他拼车中，不能加入新的拼车");
        }
        if (trip.getCurrentPeople() + joinPeopleCount > trip.getTotalPeople()) {
            throw new IllegalStateException("人数已满");
        }
        Date now = new Date();
        if (member == null) {
            MpTripMember newMember = new MpTripMember();
            newMember.setTripId(tripId);
            newMember.setUserId(userId);
            newMember.setRole(2);
            newMember.setJoinPeopleCount(joinPeopleCount);
            newMember.setStatus(1);
            newMember.setConfirmFlag(0);
            newMember.setJoinTime(now);
            newMember.setCreateTime(now);
            newMember.setUpdateTime(now);
            tripMemberMapper.insertMember(newMember);
        } else {
            member.setStatus(1);
            member.setJoinPeopleCount(joinPeopleCount);
            member.setQuitTime(null);
            member.setUpdateTime(now);
            member.setJoinTime(Optional.ofNullable(member.getJoinTime()).orElse(now));
            tripMemberMapper.updateMember(member);
        }
        tripMapper.updateTripCurrentPeople(trip.getId(), trip.getCurrentPeople() + joinPeopleCount, now);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void quitTrip(Long tripId) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            throw new IllegalStateException("未登录");
        }
        MpTrip trip = tripMapper.selectById(tripId);
        if (trip == null) {
            throw new IllegalArgumentException("拼单不存在");
        }
        MpTripMember member = tripMemberMapper.selectByTripIdAndUserId(tripId, userId);
        if (!Objects.equals(trip.getStatus(), 0)) {
            throw new IllegalStateException("当前状态不可退出");
        }
        Date now = new Date();
        if (userId.equals(trip.getOwnerUserId())) {
            tripMapper.updateTripStatus(tripId, 4, now);
            tripMemberMapper.updateStatusByTrip(tripId, 2, now);
            tripMapper.updateTripCurrentPeople(tripId, 0, now);
            return;
        }
        if (member == null || !Objects.equals(member.getStatus(), 1)) {
            throw new IllegalStateException("当前不在拼单中");
        }
        member.setStatus(2);
        member.setQuitTime(now);
        member.setUpdateTime(now);
        tripMemberMapper.updateMember(member);
        int newCurrent = Math.max(0, trip.getCurrentPeople() - member.getJoinPeopleCount());
        tripMapper.updateTripCurrentPeople(tripId, newCurrent, now);
        if (newCurrent <= 0) {
            tripMapper.updateTripStatus(tripId, 4, now);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void kickMember(Long tripId, Long targetUserId) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            throw new IllegalStateException("未登录");
        }
        MpTrip trip = tripMapper.selectById(tripId);
        if (trip == null) {
            throw new IllegalArgumentException("拼单不存在");
        }
        if (!Objects.equals(trip.getOwnerUserId(), userId)) {
            throw new IllegalStateException("仅发起人可移除成员");
        }
        if (!Objects.equals(trip.getStatus(), 0)) {
            throw new IllegalStateException("当前状态不可操作");
        }
        if (trip.getOwnerUserId().equals(targetUserId)) {
            throw new IllegalStateException("不可移除发起人");
        }
        MpTripMember target = tripMemberMapper.selectByTripIdAndUserId(tripId, targetUserId);
        if (target == null || !Objects.equals(target.getStatus(), 1)) {
            throw new IllegalStateException("成员不在拼单中");
        }
        Date now = new Date();
        target.setStatus(3);
        target.setQuitTime(now);
        target.setUpdateTime(now);
        tripMemberMapper.updateMember(target);
        int newCurrent = Math.max(0, trip.getCurrentPeople() - target.getJoinPeopleCount());
        tripMapper.updateTripCurrentPeople(tripId, newCurrent, now);
        // TODO: 系统消息通知待实现
    }

    @Override
    public MpTripMyActiveVO getMyActiveTrip() {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return null;
        }
        List<MpTrip> list = tripMapper.selectActiveTripsByUser(userId);
        if (list == null || list.isEmpty()) {
            return null;
        }
        MpTrip trip = list.get(0);
        MpTripMyActiveVO vo = new MpTripMyActiveVO();
        vo.setTripId(trip.getId());
        vo.setStartAddress(trip.getStartAddress());
        vo.setEndAddress(trip.getEndAddress());
        vo.setDepartureTimeDisplay(buildDepartureDisplay(trip));
        vo.setStatus(trip.getStatus());
        vo.setStatusText(STATUS_TEXT.get(trip.getStatus()));
        return vo;
    }

    @Override
    public List<MpTripHistoryVO> listMyHistoryTrips(Long userId, int limit) {
        if (userId == null) {
            throw new IllegalStateException("未获取到用户信息");
        }
        int size = limit > 0 ? limit : 20;
        Date now = new Date();
        List<MpTrip> trips = tripMapper.selectHistoryTripsByUser(userId, now, size);
        List<MpTripHistoryVO> result = new ArrayList<>();
        if (trips != null) {
            for (MpTrip trip : trips) {
                MpTripHistoryVO vo = new MpTripHistoryVO();
                vo.setTripId(trip.getId());
                vo.setStartAddress(trip.getStartAddress());
                vo.setEndAddress(trip.getEndAddress());
                vo.setDepartureTime(trip.getDepartureTime());
                vo.setStatus(trip.getStatus());
                vo.setIsOwner(userId.equals(trip.getOwnerUserId()));
                result.add(vo);
            }
        }
        return result;
    }

    @Override
    public boolean hasActiveTrip(Long userId) {
        if (userId == null) {
            return false;
        }
        return tripMapper.countActiveTripByUser(userId) > 0;
    }

    @Override
    public boolean hasActiveTripExcludeCurrent(Long userId, Long tripId) {
        if (userId == null) {
            return false;
        }
        if (tripId == null) {
            return hasActiveTrip(userId);
        }
        return tripMapper.countActiveTripByUserExcludeTrip(userId, tripId) > 0;
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

    private void ensureCampusAuth(Long userId, Long campusId) {
        if (campusId == null) {
            throw new IllegalStateException("校区信息缺失");
        }
        MpCampus campus = campusMapper.selectCampusById(campusId);
        if (campus == null) {
            throw new IllegalStateException("校区不存在");
        }
        if (!Objects.equals(campus.getStatus(), 1)) {
            throw new IllegalStateException("校区不可用");
        }
        userSchoolAuthService.ensureSchoolApproved(userId, campus.getSchoolId());
    }

    private Integer calculateRemainMinutes(MpTrip trip) {
        if (!isImmediateTrip(trip)) {
            return null;
        }
        long remainMillis = trip.getDepartureTime().getTime() + TEN_MINUTES_MILLIS - System.currentTimeMillis();
        return (int) Math.max(0, remainMillis / (60 * 1000));
    }

    private String buildDepartureDisplay(MpTrip trip) {
        if (trip == null || trip.getDepartureTime() == null) {
            return "待定";
        }
        Date dep = trip.getDepartureTime();
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN);
        if (isImmediateTrip(trip)) {
            long remainMillis = dep.getTime() + TEN_MINUTES_MILLIS - now.getTime();
            long minutes = Math.max(0, remainMillis / (60 * 1000));
            return "立即出行 · 剩余" + minutes + "分钟";
        }
        SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
        String depDay = dayFormat.format(dep);
        String nowDay = dayFormat.format(now);
        Date tomorrow = new Date(now.getTime() + 24 * 60 * 60 * 1000);
        String tomorrowDay = dayFormat.format(tomorrow);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        if (depDay.equals(nowDay)) {
            return "今天 " + timeFormat.format(dep);
        }
        if (depDay.equals(tomorrowDay)) {
            return "明天 " + timeFormat.format(dep);
        }
        return sdf.format(dep);
    }

    private Map<Long, MpUser> fetchUserMap(List<MpTripMember> members, Long ownerUserId) {
        List<Long> ids = new ArrayList<>();
        if (ownerUserId != null) {
            ids.add(ownerUserId);
        }
        if (members != null) {
            for (MpTripMember m : members) {
                if (m.getUserId() != null && !ids.contains(m.getUserId())) {
                    ids.add(m.getUserId());
                }
            }
        }
        if (ids.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<Long, MpUser> map = new HashMap<>();
        for (MpUser user : userMapper.selectByIds(ids)) {
            map.put(user.getId(), user);
        }
        return map;
    }

    private boolean isExpiredImmediateTrip(MpTrip trip) {
        if (!isImmediateTrip(trip)) {
            return false;
        }
        Date expireAt = new Date(trip.getDepartureTime().getTime() + TEN_MINUTES_MILLIS);
        return expireAt.before(new Date());
    }
}
