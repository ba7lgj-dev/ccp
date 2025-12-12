package org.ba7lgj.ccp.admin.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.ba7lgj.ccp.core.domain.CcpLocation;
import org.ba7lgj.ccp.core.service.ICcpLocationService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 校外地点后台管理。
 */
@RestController
@RequestMapping("/ccp/location")
public class CcpLocationController extends BaseController {

    @Resource
    private ICcpLocationService ccpLocationService;

    @PreAuthorize("@ss.hasPermi('ccp:location:list')")
    @GetMapping("/list")
    public TableDataInfo list(CcpLocation query) {
        startPage();
        List<CcpLocation> list = ccpLocationService.selectCcpLocationList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('ccp:location:export')")
    @Log(title = "地点管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, CcpLocation query) {
        List<CcpLocation> list = ccpLocationService.selectCcpLocationList(query);
        ExcelUtil<CcpLocation> util = new ExcelUtil<>(CcpLocation.class);
        util.exportExcel(response, list, "地点数据");
    }

    @PreAuthorize("@ss.hasPermi('ccp:location:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(ccpLocationService.selectCcpLocationById(id));
    }

    @PreAuthorize("@ss.hasPermi('ccp:location:add')")
    @Log(title = "地点管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody CcpLocation ccpLocation) {
        int rows = ccpLocationService.insertCcpLocation(ccpLocation);
        return toAjax(rows);
    }

    @PreAuthorize("@ss.hasPermi('ccp:location:edit')")
    @Log(title = "地点管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody CcpLocation ccpLocation) {
        int rows = ccpLocationService.updateCcpLocation(ccpLocation);
        return toAjax(rows);
    }

    @PreAuthorize("@ss.hasPermi('ccp:location:remove')")
    @Log(title = "地点管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(ccpLocationService.deleteCcpLocationByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('ccp:location:edit')")
    @Log(title = "地点管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody CcpLocation ccpLocation) {
        int rows = ccpLocationService.changeStatus(ccpLocation.getId(), ccpLocation.getStatus());
        return toAjax(rows);
    }
}
