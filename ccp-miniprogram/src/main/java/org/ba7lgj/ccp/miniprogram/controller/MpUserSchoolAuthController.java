package org.ba7lgj.ccp.miniprogram.controller;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.context.MpUserContextHolder;
import org.ba7lgj.ccp.miniprogram.dto.MpUserSchoolAuthApplyDTO;
import org.ba7lgj.ccp.miniprogram.service.MpUserSchoolAuthService;
import org.ba7lgj.ccp.miniprogram.vo.MpApprovedSchoolVO;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
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

    private final MpUserSchoolAuthService userSchoolAuthService;

    public MpUserSchoolAuthController(MpUserSchoolAuthService userSchoolAuthService) {
        this.userSchoolAuthService = userSchoolAuthService;
    }

    @GetMapping("/listMine")
    public MpResult<List<MpUserSchoolAuthVO>> listMine() {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        List<MpUserSchoolAuthVO> list = userSchoolAuthService.listMine(userId);
        return MpResult.ok(list);
    }

    @GetMapping("/detail")
    public MpResult<MpUserSchoolAuthVO> detail(@RequestParam("schoolId") Long schoolId) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        MpUserSchoolAuthVO vo = userSchoolAuthService.getDetail(userId, schoolId);
        return MpResult.ok(vo);
    }

    @PostMapping("/apply")
    public MpResult<Void> apply(@RequestBody MpUserSchoolAuthApplyDTO dto) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        try {
            userSchoolAuthService.apply(userId, dto);
            return MpResult.ok();
        } catch (IllegalArgumentException e) {
            return MpResult.error(400, e.getMessage());
        } catch (IllegalStateException e) {
            return MpResult.error(4003, e.getMessage());
        }
    }

    @GetMapping("/listApproved")
    public MpResult<List<MpApprovedSchoolVO>> listApproved() {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        List<MpApprovedSchoolVO> list = userSchoolAuthService.listApproved(userId);
        return MpResult.ok(list);
    }
}
