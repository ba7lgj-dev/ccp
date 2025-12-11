package org.ba7lgj.ccp.miniprogram.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.ba7lgj.ccp.miniprogram.context.MpUserContextHolder;
import org.ba7lgj.ccp.miniprogram.service.MpTripService;
import org.ba7lgj.ccp.miniprogram.service.MpUserRealAuthService;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.ba7lgj.ccp.miniprogram.vo.MpTripDetailVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripMyActiveVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripHistoryVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp/trip")
public class MpTripController {
    @Autowired
    private MpTripService tripService;

    @Autowired
    private MpUserRealAuthService userRealAuthService;

    @PostMapping("/publish")
    public MpResult<Void> publish(@RequestBody MpTripVO vo) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        try {
            userRealAuthService.ensureApproved(userId);
        } catch (IllegalStateException e) {
            return MpResult.error(4003, "请先完成实名认证后再发布拼车");
        }
        if (vo == null || vo.getCampusId() == null) {
            return MpResult.error(400, "校区不能为空");
        }
        if (!isValidEndpoint(vo.getStartGateId(), vo.getStartLocationId(), vo.getStartAddress())) {
            return MpResult.error(400, "起点必须填写校门、地点或地址");
        }
        if (!isValidEndpoint(vo.getEndGateId(), vo.getEndLocationId(), vo.getEndAddress())) {
            return MpResult.error(400, "终点必须填写校门、地点或地址");
        }
        if (vo.getOwnerPeopleCount() == null || vo.getOwnerPeopleCount() < 1) {
            return MpResult.error(400, "我方人数至少为1");
        }
        if (vo.getTotalPeople() == null || vo.getTotalPeople() < vo.getOwnerPeopleCount()) {
            return MpResult.error(400, "总人数必须大于或等于我方人数");
        }
        if (tripService.hasActiveTrip(userId)) {
            return MpResult.error(4002, "你当前已有进行中的拼车，请先完成或退出后再发起新的拼车");
        }
        boolean immediate = Boolean.TRUE.equals(vo.getImmediate());
        if (!immediate) {
            if (!StringUtils.hasText(vo.getDepartureTime())) {
                return MpResult.error(400, "预约出发时间不能为空");
            }
            try {
                Date dep = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(vo.getDepartureTime());
                Date now = new Date();
                //if (dep.before(now)) {
                //    return MpResult.error(400, "出发时间不能早于当前时间");
                //}
                long diffMinutes = (dep.getTime() - now.getTime()) / (60 * 1000);
                if (diffMinutes <= 5) {
                    vo.setImmediate(true);
                }
            } catch (ParseException e) {
                return MpResult.error(400, "出发时间格式不正确");
            }
        }
        try {
            tripService.publishTrip(vo);
            return MpResult.ok();
        } catch (IllegalArgumentException e) {
            return MpResult.error(400, e.getMessage());
        } catch (IllegalStateException e) {
            return MpResult.error(4001, e.getMessage());
        }
    }

    @GetMapping("/hall")
    public MpResult<List<MpTripVO>> hall(@RequestParam("campusId") Long campusId) {
        List<MpTripVO> list = tripService.hallList(campusId);
        return MpResult.ok(list);
    }

    @GetMapping("/detail")
    public MpResult<MpTripDetailVO> detail(@RequestParam("tripId") Long tripId) {
        MpTripDetailVO detail = tripService.getTripDetail(tripId);
        if (detail == null) {
            return MpResult.error(404, "拼单不存在");
        }
        return MpResult.ok(detail);
    }

    @PostMapping("/join")
    public MpResult<Void> join(@RequestBody MpTripVO vo) {
        if (vo == null || vo.getId() == null || vo.getJoinPeopleCount() == null) {
            return MpResult.error(400, "参数不完整");
        }
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        try {
            userRealAuthService.ensureApproved(userId);
        } catch (IllegalStateException e) {
            return MpResult.error(4003, "请先完成实名认证后再加入拼车");
        }
        if (tripService.hasActiveTripExcludeCurrent(userId, vo.getId())) {
            return MpResult.error(4002, "你当前已在其他拼车中，不能加入新的拼车");
        }
        try {
            tripService.joinTrip(vo.getId(), vo.getJoinPeopleCount());
        } catch (IllegalArgumentException e) {
            return MpResult.error(400, e.getMessage());
        } catch (IllegalStateException e) {
            return MpResult.error(4001, e.getMessage());
        }
        return MpResult.ok();
    }

    @PostMapping("/quit")
    public MpResult<Void> quit(@RequestBody MpTripVO vo) {
        if (vo == null || vo.getId() == null) {
            return MpResult.error(400, "参数不完整");
        }
        try {
            tripService.quitTrip(vo.getId());
        } catch (IllegalArgumentException e) {
            return MpResult.error(400, e.getMessage());
        } catch (IllegalStateException e) {
            return MpResult.error(4001, e.getMessage());
        }
        return MpResult.ok();
    }

    @PostMapping("/kick")
    public MpResult<Void> kick(@RequestBody MpTripVO vo) {
        if (vo == null || vo.getId() == null || vo.getTargetUserId() == null) {
            return MpResult.error(400, "参数不完整");
        }
        try {
            tripService.kickMember(vo.getId(), vo.getTargetUserId());
        } catch (IllegalArgumentException e) {
            return MpResult.error(400, e.getMessage());
        } catch (IllegalStateException e) {
            return MpResult.error(4001, e.getMessage());
        }
        return MpResult.ok();
    }

    @GetMapping("/myActive")
    public MpResult<MpTripMyActiveVO> myActive() {
        MpTripMyActiveVO vo = tripService.getMyActiveTrip();
        return MpResult.ok(vo);
    }

    @GetMapping("/myHistory")
    public MpResult<List<MpTripHistoryVO>> myHistory() {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        List<MpTripHistoryVO> list = tripService.getMyHistoryTrips(userId);
        return MpResult.ok(list);
    }

    private boolean isValidEndpoint(Long gateId, Long locationId, String address) {
        return gateId != null || locationId != null || StringUtils.hasText(address);
    }
}
