package org.ba7lgj.ccp.admin.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.ba7lgj.ccp.common.dto.trip.CcpTripMemberNoShowDTO;
import org.ba7lgj.ccp.common.dto.trip.CcpTripMemberQuery;
import org.ba7lgj.ccp.common.vo.trip.CcpTripMemberVO;
import org.ba7lgj.ccp.core.service.admin.CcpTripMemberAdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 拼车成员后台管理。
 */
@RestController
@RequestMapping("/ccp/trip/member")
public class CcpTripMemberAdminController extends BaseController {

    @Resource
    private CcpTripMemberAdminService ccpTripMemberAdminService;

    @PreAuthorize("@ss.hasPermi('ccp:tripMember:list')")
    @GetMapping("/list")
    public R<TableDataInfo> list(CcpTripMemberQuery query) {
        startPage();
        List<CcpTripMemberVO> list = ccpTripMemberAdminService.selectMemberList(query);
        return R.ok(getDataTable(list));
    }

    @PreAuthorize("@ss.hasPermi('ccp:tripMember:list')")
    @GetMapping("/listByTrip")
    public R<TableDataInfo> listByTrip(CcpTripMemberQuery query) {
        if (query.getTripId() == null) {
            return R.error("tripId不能为空");
        }
        startPage();
        List<CcpTripMemberVO> list = ccpTripMemberAdminService.selectMemberListByTrip(query);
        return R.ok(getDataTable(list));
    }

    @PreAuthorize("@ss.hasPermi('ccp:tripMember:edit')")
    @Log(title = "拼车成员", businessType = BusinessType.UPDATE)
    @PostMapping("/markNoShow")
    public R<?> markNoShow(@Validated @RequestBody CcpTripMemberNoShowDTO dto) {
        int rows = ccpTripMemberAdminService.markNoShow(dto);
        return rows > 0 ? R.ok() : R.error("标记失败");
    }
}
