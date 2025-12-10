package org.ba7lgj.ccp.miniprogram.controller;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.service.SchoolService;
import org.ba7lgj.ccp.miniprogram.vo.Result;
import org.ba7lgj.ccp.miniprogram.vo.SchoolVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/school")
public class SchoolController {
    @Autowired
    private SchoolService schoolService;

    @GetMapping("/list")
    public Result<List<SchoolVO>> list() {
        List<SchoolVO> list = schoolService.listAllEnabledSchools();
        return Result.ok(list);
    }
}
