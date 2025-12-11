package org.ba7lgj.ccp.miniprogram.controller;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.service.MpLocationService;
import org.ba7lgj.ccp.miniprogram.vo.MpLocationVO;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp/location")
public class MpLocationController {
    @Autowired
    private MpLocationService locationService;

    @GetMapping("/listByCampus")
    public MpResult<List<MpLocationVO>> listByCampus(@RequestParam("campusId") Long campusId) {
        List<MpLocationVO> list = locationService.listByCampus(campusId);
        return MpResult.ok(list);
    }
}
