package org.ba7lgj.ccp.admin.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.core.domain.vo.CcpMiniUserDetailVO;
import org.ba7lgj.ccp.core.service.ICcpMiniUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

/**
 * 微信小程序用户管理。
 */
@RestController
@RequestMapping("/ccp/miniUser")
public class CcpMiniUserController extends BaseController {

    @Resource
    private ICcpMiniUserService ccpMiniUserService;

    @PreAuthorize("@ss.hasPermi('ccp:miniUser:list')")
    @GetMapping("/list")
    public TableDataInfo list(MiniUser query) {
        startPage();
        List<MiniUser> list = ccpMiniUserService.selectCcpMiniUserList(query);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('ccp:miniUser:query')")
    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        CcpMiniUserDetailVO detail = ccpMiniUserService.selectDetail(id);
        if (detail == null) {
            return AjaxResult.error("用户不存在");
        }
        return AjaxResult.success(detail);
    }

    @PreAuthorize("@ss.hasPermi('ccp:miniUser:edit')")
    @Log(title = "小程序用户", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody MiniUser user) {
        MiniUser dbUser = ccpMiniUserService.selectCcpMiniUserById(user.getId());
        if (dbUser == null) {
            return AjaxResult.error("用户不存在");
        }
        MiniUser update = new MiniUser();
        update.setId(user.getId());
        update.setStatus(user.getStatus());
        update.setAdminRemark(user.getAdminRemark());
        update.setRealName(user.getRealName());
        update.setPhone(user.getPhone());
        update.setUpdateTime(new Date());
        return toAjax(ccpMiniUserService.updateCcpMiniUser(update));
    }

    @PreAuthorize("@ss.hasPermi('ccp:miniUser:auth')")
    @Log(title = "小程序用户实名认证审核", businessType = BusinessType.UPDATE)
    @PostMapping("/realAuth/approve")
    public AjaxResult approve(@RequestBody MiniUser user) {
        MiniUser dbUser = ccpMiniUserService.selectCcpMiniUserById(user.getId());
        if (dbUser == null) {
            return AjaxResult.error("用户不存在");
        }
        if (dbUser.getRealAuthStatus() == null || dbUser.getRealAuthStatus() != 1) {
            return AjaxResult.error("当前状态不可审核通过");
        }
        Long reviewer = SecurityUtils.getUserId();
        Date now = new Date();
        int rows = ccpMiniUserService.updateRealAuthStatus(user.getId(), 2, null, reviewer, now);
        if (rows > 0 && user.getAdminRemark() != null) {
            MiniUser remarkUpdate = new MiniUser();
            remarkUpdate.setId(user.getId());
            remarkUpdate.setAdminRemark(user.getAdminRemark());
            remarkUpdate.setUpdateTime(now);
            ccpMiniUserService.updateCcpMiniUser(remarkUpdate);
        }
        return toAjax(rows);
    }

    @PreAuthorize("@ss.hasPermi('ccp:miniUser:auth')")
    @Log(title = "小程序用户实名认证审核", businessType = BusinessType.UPDATE)
    @PostMapping("/realAuth/reject")
    public AjaxResult reject(@RequestBody MiniUser user) {
        MiniUser dbUser = ccpMiniUserService.selectCcpMiniUserById(user.getId());
        if (dbUser == null) {
            return AjaxResult.error("用户不存在");
        }
        if (dbUser.getRealAuthStatus() == null || dbUser.getRealAuthStatus() != 1) {
            return AjaxResult.error("当前状态不可拒绝");
        }
        if (user.getRealAuthFailReason() == null || user.getRealAuthFailReason().isEmpty()) {
            return AjaxResult.error("请填写拒绝原因");
        }
        Long reviewer = SecurityUtils.getUserId();
        Date now = new Date();
        return toAjax(ccpMiniUserService.updateRealAuthStatus(user.getId(), 3, user.getRealAuthFailReason(), reviewer, now));
    }

    @PreAuthorize("@ss.hasPermi('ccp:miniUser:export')")
    @Log(title = "小程序用户", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, MiniUser query) {
        List<MiniUser> list = ccpMiniUserService.selectCcpMiniUserList(query);
        ExcelUtil<MiniUser> util = new ExcelUtil<>(MiniUser.class);
        util.exportExcel(response, list, "微信小程序用户数据");
    }
}
