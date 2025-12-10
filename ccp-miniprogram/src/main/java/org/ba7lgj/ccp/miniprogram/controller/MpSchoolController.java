package org.ba7lgj.ccp.miniprogram.controller;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.service.MpSchoolService;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.ba7lgj.ccp.miniprogram.vo.MpSchoolVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp/school")
public class MpSchoolController {
    @Autowired
    private MpSchoolService schoolService;

    @GetMapping("/list")
    public MpResult<List<MpSchoolVO>> list() {
        List<MpSchoolVO> list = schoolService.listAllEnabledSchools();
        return MpResult.ok(list);
    }
}
