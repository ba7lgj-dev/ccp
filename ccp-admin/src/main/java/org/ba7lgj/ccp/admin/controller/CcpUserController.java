package org.ba7lgj.ccp.admin.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.dto.MiniUserAuditDTO;
import org.ba7lgj.ccp.common.dto.MiniUserQueryDTO;
import org.ba7lgj.ccp.core.service.IMiniUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 后台小程序用户管理接口。
 */
@RestController
@RequestMapping("/ccp/user")
public class CcpUserController extends BaseController {

    @Resource
    private IMiniUserService miniUserService;

    @PreAuthorize("@ss.hasPermi('ccp:user:list')")
    @GetMapping("/list")
    public TableDataInfo list(MiniUserQueryDTO queryDTO) {
        startPage();
        List<MiniUser> list = miniUserService.selectMiniUserList(queryDTO);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(miniUserService.selectMiniUserById(id));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:add')")
    @Log(title = "小程序用户", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody MiniUser user) {
        return toAjax(miniUserService.insertMiniUser(user));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:edit')")
    @Log(title = "小程序用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MiniUser user) {
        user.setOpenId(null);
        return toAjax(miniUserService.updateMiniUser(user));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:remove')")
    @Log(title = "小程序用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(miniUserService.deleteMiniUserById(id));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:remove')")
    @Log(title = "小程序用户", businessType = BusinessType.DELETE)
    @DeleteMapping("/batch/{ids}")
    public AjaxResult removeBatch(@PathVariable Long[] ids) {
        return toAjax(miniUserService.deleteMiniUserByIds(ids));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:changeStatus')")
    @Log(title = "小程序用户", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public AjaxResult changeStatus(@RequestBody MiniUser user) {
        return toAjax(miniUserService.changeStatus(user.getId(), user.getStatus()));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:review')")
    @Log(title = "小程序用户审核", businessType = BusinessType.UPDATE)
    @PutMapping("/review")
    public AjaxResult review(@RequestBody MiniUserAuditDTO auditDTO) {
        Long reviewer = SecurityUtils.getUserId();
        Date now = new Date();
        return toAjax(miniUserService.reviewUser(auditDTO.getId(), auditDTO.getTargetAuthStatus(),
                auditDTO.getAuthFailReason(), reviewer, now));
    }

    @PreAuthorize("@ss.hasPermi('ccp:user:export')")
    @Log(title = "小程序用户", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void export(HttpServletResponse response, MiniUserQueryDTO queryDTO) {
        List<MiniUser> list = miniUserService.selectMiniUserExportList(queryDTO);
        ExcelUtil<MiniUser> util = new ExcelUtil<>(MiniUser.class);
        util.exportExcel(response, list, "小程序用户数据");
    }
}
