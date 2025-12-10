package org.ba7lgj.ccp.miniprogram.controller;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.service.MpCampusService;
import org.ba7lgj.ccp.miniprogram.vo.MpCampusVO;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp/campus")
public class MpCampusController {
    @Autowired
    private MpCampusService campusService;

    @GetMapping("/listBySchool")
    public MpResult<List<MpCampusVO>> listBySchool(@RequestParam("schoolId") Long schoolId) {
        List<MpCampusVO> list = campusService.listEnabledCampusBySchool(schoolId);
        return MpResult.ok(list);
    }
}
