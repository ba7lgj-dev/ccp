package org.ba7lgj.ccp.miniprogram.controller;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.service.CampusService;
import org.ba7lgj.ccp.miniprogram.vo.CampusVO;
import org.ba7lgj.ccp.miniprogram.vo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/campus")
public class CampusController {
    @Autowired
    private CampusService campusService;

    @GetMapping("/listBySchool")
    public Result<List<CampusVO>> listBySchool(@RequestParam("schoolId") Long schoolId) {
        List<CampusVO> list = campusService.listEnabledCampusBySchool(schoolId);
        return Result.ok(list);
    }
}
