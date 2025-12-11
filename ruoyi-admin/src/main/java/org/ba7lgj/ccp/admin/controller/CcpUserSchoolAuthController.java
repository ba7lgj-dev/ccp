package org.ba7lgj.ccp.admin.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import org.ba7lgj.ccp.core.domain.CcpUserSchoolAuth;
import org.ba7lgj.ccp.core.service.ICcpUserSchoolAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 学校学生认证后台接口。
 */
@RestController
@RequestMapping("/ccp/schoolAuth")
@Validated
public class CcpUserSchoolAuthController extends BaseController {

    @Autowired
    private ICcpUserSchoolAuthService ccpUserSchoolAuthService;

    @GetMapping("/list")
    @PreAuthorize("@ss.hasPermi('ccp:schoolAuth:list')")
    public TableDataInfo list(CcpUserSchoolAuth query) {
        startPage();
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        Long userId = currentUser.getUserId();
        boolean isAdmin = SysUser.isAdmin(userId);
        List<CcpUserSchoolAuth> list = ccpUserSchoolAuthService.selectCcpUserSchoolAuthList(query, userId, isAdmin);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    @PreAuthorize("@ss.hasPermi('ccp:schoolAuth:query')")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        Long userId = currentUser.getUserId();
        boolean isAdmin = SysUser.isAdmin(userId);
        CcpUserSchoolAuth detail = ccpUserSchoolAuthService.selectCcpUserSchoolAuthById(id, userId, isAdmin);
        if (detail == null) {
            return AjaxResult.error("无权查看或记录不存在");
        }
        return AjaxResult.success(detail);
    }

    @PostMapping("/approve")
    @PreAuthorize("@ss.hasPermi('ccp:schoolAuth:review')")
    @Log(title = "学校学生认证", businessType = BusinessType.UPDATE)
    public AjaxResult approve(@RequestBody @Validated ReviewIdDTO dto) {
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        Long userId = currentUser.getUserId();
        try {
            ccpUserSchoolAuthService.approve(dto.getId(), userId);
            return AjaxResult.success("审核通过");
        } catch (ServiceException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    @PostMapping("/reject")
    @PreAuthorize("@ss.hasPermi('ccp:schoolAuth:review')")
    @Log(title = "学校学生认证", businessType = BusinessType.UPDATE)
    public AjaxResult reject(@RequestBody @Validated ReviewRejectDTO dto) {
        SysUser currentUser = SecurityUtils.getLoginUser().getUser();
        Long userId = currentUser.getUserId();
        try {
            ccpUserSchoolAuthService.reject(dto.getId(), dto.getFailReason(), userId);
            return AjaxResult.success("已拒绝");
        } catch (ServiceException e) {
            return AjaxResult.error(e.getMessage());
        }
    }

    public static class ReviewIdDTO {
        @NotNull(message = "ID不能为空")
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }
    }

    public static class ReviewRejectDTO extends ReviewIdDTO {
        @NotBlank(message = "拒绝原因不能为空")
        private String failReason;

        public String getFailReason() {
            return failReason;
        }

        public void setFailReason(String failReason) {
            this.failReason = failReason;
        }
    }
}
