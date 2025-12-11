package org.ba7lgj.ccp.miniprogram.controller;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.service.MpTripService;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.ba7lgj.ccp.miniprogram.vo.MpTripVO;
import org.springframework.beans.factory.annotation.Autowired;
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
        tripService.publishTrip(vo);
        return MpResult.ok();
    }

    @GetMapping("/hall")
    public MpResult<List<MpTripVO>> hall(@RequestParam("campusId") Long campusId) {
        List<MpTripVO> list = tripService.hallList(campusId);
        return MpResult.ok(list);
    }
}
