package org.ba7lgj.ccp.miniprogram.controller;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.service.MpGateService;
import org.ba7lgj.ccp.miniprogram.vo.MpGateVO;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp/gate")
public class MpGateController {
    @Autowired
    private MpGateService gateService;

    @GetMapping("/listByCampus")
    public MpResult<List<MpGateVO>> listByCampus(@RequestParam("campusId") Long campusId) {
        List<MpGateVO> list = gateService.listEnabledGatesByCampus(campusId);
        return MpResult.ok(list);
    }
}
