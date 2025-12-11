package org.ba7lgj.ccp.miniprogram.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.ba7lgj.ccp.miniprogram.context.MpUserContextHolder;
import org.ba7lgj.ccp.miniprogram.service.MpTripService;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
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

    @PostMapping("/publish")
    public MpResult<Void> publish(@RequestBody MpTripVO vo) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
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
        boolean immediate = Boolean.TRUE.equals(vo.getImmediate());
        if (!immediate) {
            if (!StringUtils.hasText(vo.getDepartureTime())) {
                return MpResult.error(400, "预约出发时间不能为空");
            }
            try {
                Date dep = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(vo.getDepartureTime());
                Date now = new Date();
                if (dep.before(now)) {
                    return MpResult.error(400, "出发时间不能早于当前时间");
                }
                long diffMinutes = (dep.getTime() - now.getTime()) / (60 * 1000);
                if (diffMinutes <= 5) {
                    vo.setImmediate(true);
                }
            } catch (ParseException e) {
                return MpResult.error(400, "出发时间格式不正确");
            }
        }
        tripService.publishTrip(vo);
        return MpResult.ok();
    }

    @GetMapping("/hall")
    public MpResult<List<MpTripVO>> hall(@RequestParam("campusId") Long campusId) {
        List<MpTripVO> list = tripService.hallList(campusId);
        return MpResult.ok(list);
    }

    private boolean isValidEndpoint(Long gateId, Long locationId, String address) {
        return gateId != null || locationId != null || StringUtils.hasText(address);
    }
}
