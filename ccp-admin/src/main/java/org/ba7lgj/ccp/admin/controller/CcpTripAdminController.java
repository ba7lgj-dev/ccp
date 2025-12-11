package org.ba7lgj.ccp.admin.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.ba7lgj.ccp.common.dto.trip.CcpTripAdminQuery;
import org.ba7lgj.ccp.common.dto.trip.CcpTripChangeStatusDTO;
import org.ba7lgj.ccp.common.vo.trip.CcpTripAdminDetailVO;
import org.ba7lgj.ccp.common.vo.trip.CcpTripAdminVO;
import org.ba7lgj.ccp.core.service.admin.CcpTripAdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 拼车订单后台管理。
 */
@RestController
@RequestMapping("/ccp/trip/trip")
public class CcpTripAdminController extends BaseController {

    @Resource
    private CcpTripAdminService ccpTripAdminService;

    @PreAuthorize("@ss.hasPermi('ccp:trip:list')")
    @GetMapping("/list")
    public R<TableDataInfo> list(CcpTripAdminQuery query) {
        startPage();
        List<CcpTripAdminVO> list = ccpTripAdminService.selectTripList(query);
        return R.ok(getDataTable(list));
    }

    @PreAuthorize("@ss.hasPermi('ccp:trip:query')")
    @GetMapping("/{id}")
    public R<CcpTripAdminDetailVO> detail(@PathVariable Long id) {
        return R.ok(ccpTripAdminService.selectTripDetailById(id));
    }

    @PreAuthorize("@ss.hasPermi('ccp:trip:edit')")
    @Log(title = "拼车订单", businessType = BusinessType.UPDATE)
    @PostMapping("/changeStatus")
    public R<?> changeStatus(@Validated @RequestBody CcpTripChangeStatusDTO dto) {
        int rows = ccpTripAdminService.changeStatus(dto);
        return rows > 0 ? R.ok() : R.error("更新失败");
    }

    @PreAuthorize("@ss.hasPermi('ccp:trip:export')")
    @Log(title = "拼车订单", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(HttpServletResponse response, CcpTripAdminQuery query) {
        List<CcpTripAdminVO> list = ccpTripAdminService.selectTripList(query);
        ExcelUtil<CcpTripAdminVO> util = new ExcelUtil<>(CcpTripAdminVO.class);
        util.exportExcel(response, list, "拼车订单数据");
    }
}
