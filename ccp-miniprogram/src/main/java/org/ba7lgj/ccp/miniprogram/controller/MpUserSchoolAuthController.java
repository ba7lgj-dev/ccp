package org.ba7lgj.ccp.miniprogram.controller;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.context.MpUserContextHolder;
import org.ba7lgj.ccp.miniprogram.dto.MpUserSchoolAuthApplyDTO;
import org.ba7lgj.ccp.miniprogram.service.MpUserSchoolAuthService;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.ba7lgj.ccp.miniprogram.vo.MpUserApprovedSchoolVO;
import org.ba7lgj.ccp.miniprogram.vo.MpUserSchoolAuthVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp/user/schoolAuth")
public class MpUserSchoolAuthController {

    private final MpUserSchoolAuthService mpUserSchoolAuthService;

    public MpUserSchoolAuthController(MpUserSchoolAuthService mpUserSchoolAuthService) {
        this.mpUserSchoolAuthService = mpUserSchoolAuthService;
    }

    @GetMapping("/listMine")
    public MpResult<List<MpUserSchoolAuthVO>> listMine() {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "token invalid");
        }
        return MpResult.ok(mpUserSchoolAuthService.listMyAuth(userId));
    }

    @GetMapping("/detail")
    public MpResult<MpUserSchoolAuthVO> detail(@RequestParam("schoolId") Long schoolId) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "token invalid");
        }
        MpUserSchoolAuthVO vo = mpUserSchoolAuthService.getDetail(userId, schoolId);
        return MpResult.ok(vo);
    }

    @PostMapping("/apply")
    public MpResult<Void> apply(@RequestBody MpUserSchoolAuthApplyDTO dto) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "token invalid");
        }
        mpUserSchoolAuthService.apply(userId, dto);
        return MpResult.ok("提交成功，等待审核");
    }

    @GetMapping("/listApproved")
    public MpResult<List<MpUserApprovedSchoolVO>> listApproved() {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "token invalid");
        }
        return MpResult.ok(mpUserSchoolAuthService.listApproved(userId));
    }
}
