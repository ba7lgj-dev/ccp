package org.ba7lgj.ccp.miniprogram.controller;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.service.GateService;
import org.ba7lgj.ccp.miniprogram.vo.GateVO;
import org.ba7lgj.ccp.miniprogram.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp/gate")
public class MpGateController {
    @Autowired
    private GateService gateService;

    @GetMapping("/listByCampus")
    public Result<List<GateVO>> listByCampus(@RequestParam("campusId") Long campusId) {
        List<GateVO> list = gateService.listEnabledGatesByCampus(campusId);
        return Result.ok(list);
    }
}
