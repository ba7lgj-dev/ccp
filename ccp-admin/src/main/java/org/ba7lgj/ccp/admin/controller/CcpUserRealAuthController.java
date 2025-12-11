package org.ba7lgj.ccp.admin.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.config.ServerConfig;
import org.ba7lgj.ccp.common.dto.CcpUserRealAuthQueryDTO;
import org.ba7lgj.ccp.core.domain.vo.CcpUserRealAuthDetailVO;
import org.ba7lgj.ccp.core.domain.vo.CcpUserRealAuthListVO;
import org.ba7lgj.ccp.core.service.CcpUserRealAuthService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/ccp/userRealAuth")
public class CcpUserRealAuthController extends BaseController {

    @Resource
    private CcpUserRealAuthService ccpUserRealAuthService;

    @Resource
    private ServerConfig serverConfig;

    @PreAuthorize("@ss.hasPermi('ccp:userRealAuth:list')")
    @GetMapping("/list")
    public TableDataInfo list(CcpUserRealAuthQueryDTO queryDTO) {
        startPage();
        List<CcpUserRealAuthListVO> list = ccpUserRealAuthService.selectUserRealAuthList(queryDTO);
        list.forEach(this::fillUserAvatar);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('ccp:userRealAuth:query')")
    @GetMapping("/{userId}")
    public AjaxResult getInfo(@PathVariable Long userId) {
        CcpUserRealAuthDetailVO detail = ccpUserRealAuthService.selectUserRealAuthById(userId);
        if (detail == null) {
            return AjaxResult.error("用户不存在");
        }
        fillDetailUrls(detail);
        return AjaxResult.success(detail);
    }

    @PreAuthorize("@ss.hasPermi('ccp:userRealAuth:approve')")
    @Log(title = "实名认证审核", businessType = BusinessType.UPDATE)
    @PostMapping("/approve")
    public AjaxResult approve(@RequestBody CcpUserRealAuthDetailVO req) {
        Long userId = req.getId();
        if (userId == null) {
            return AjaxResult.error("缺少用户ID");
        }
        Long reviewer = SecurityUtils.getUserId();
        int rows = ccpUserRealAuthService.approveUserRealAuth(userId, reviewer, req.getAdminRemark());
        return toAjax(rows);
    }

    @PreAuthorize("@ss.hasPermi('ccp:userRealAuth:reject')")
    @Log(title = "实名认证审核", businessType = BusinessType.UPDATE)
    @PostMapping("/reject")
    public AjaxResult reject(@RequestBody CcpUserRealAuthDetailVO req) {
        Long userId = req.getId();
        if (userId == null) {
            return AjaxResult.error("缺少用户ID");
        }
        if (StringUtils.isBlank(req.getRealAuthFailReason())) {
            return AjaxResult.error("请填写拒绝原因");
        }
        Long reviewer = SecurityUtils.getUserId();
        int rows = ccpUserRealAuthService.rejectUserRealAuth(userId, reviewer, req.getRealAuthFailReason(), req.getAdminRemark());
        return toAjax(rows);
    }

    private void fillUserAvatar(CcpUserRealAuthListVO vo) {
        if (vo == null) {
            return;
        }
        if (StringUtils.isNotBlank(vo.getAvatarUrl()) && !StringUtils.startsWithAny(vo.getAvatarUrl(), "http://", "https://")) {
            vo.setAvatarUrl(serverConfig.getUrl() + vo.getAvatarUrl());
        }
    }

    private void fillDetailUrls(CcpUserRealAuthDetailVO detail) {
        if (detail == null) {
            return;
        }
        if (StringUtils.isNotBlank(detail.getAvatarUrl()) && !StringUtils.startsWithAny(detail.getAvatarUrl(), "http://", "https://")) {
            detail.setAvatarUrl(serverConfig.getUrl() + detail.getAvatarUrl());
        }
        if (StringUtils.isNotBlank(detail.getFaceImageUrl()) && !StringUtils.startsWithAny(detail.getFaceImageUrl(), "http://", "https://")) {
            detail.setFaceImageUrl(serverConfig.getUrl() + detail.getFaceImageUrl());
        }
    }
}
