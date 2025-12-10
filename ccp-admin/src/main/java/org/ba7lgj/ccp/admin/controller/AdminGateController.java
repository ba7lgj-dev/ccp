package org.ba7lgj.ccp.admin.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.ba7lgj.ccp.common.dto.GateDTO;
import org.ba7lgj.ccp.common.dto.GateQueryDTO;
import org.ba7lgj.ccp.core.service.CoreGateService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 校门管理接口。
 */
@RestController
@RequestMapping("/admin/gate")
public class AdminGateController extends BaseController {

    @Resource
    private CoreGateService gateService;

    @PreAuthorize("@ss.hasPermi('ccp:gate:list')")
    @GetMapping("/list")
    public R<TableDataInfo> list(GateQueryDTO queryDTO) {
        startPage();
        List<?> list = gateService.selectGateList(queryDTO);
        return R.ok(getDataTable(list));
    }

    @PreAuthorize("@ss.hasPermi('ccp:gate:query')")
    @GetMapping("/{id}")
    public R<?> getInfo(@PathVariable Long id) {
        return R.ok(gateService.selectGateById(id));
    }

    @PreAuthorize("@ss.hasPermi('ccp:gate:add')")
    @Log(title = "校门管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<?> add(@Validated @RequestBody GateDTO dto) {
        if (!gateService.checkGateUnique(dto.getCampusId(), dto.getGateName(), null)) {
            return R.error("同一校区下校门名称已存在");
        }
        return gateService.insertGate(dto) > 0 ? R.ok() : R.error("新增校门失败");
    }

    @PreAuthorize("@ss.hasPermi('ccp:gate:edit')")
    @Log(title = "校门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<?> edit(@Validated @RequestBody GateDTO dto) {
        if (dto.getId() == null) {
            return R.error("校门ID不能为空");
        }
        if (!gateService.checkGateUnique(dto.getCampusId(), dto.getGateName(), dto.getId())) {
            return R.error("同一校区下校门名称已存在");
        }
        return gateService.updateGate(dto) > 0 ? R.ok() : R.error("修改校门失败");
    }

    @PreAuthorize("@ss.hasPermi('ccp:gate:edit')")
    @Log(title = "校门管理", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public R<?> changeStatus(@RequestBody GateDTO dto) {
        if (dto.getId() == null || Objects.isNull(dto.getStatus())) {
            return R.error("参数不完整");
        }
        return gateService.changeStatus(dto.getId(), dto.getStatus()) > 0 ? R.ok() : R.error("更新状态失败");
    }

    @PreAuthorize("@ss.hasPermi('ccp:gate:remove')")
    @Log(title = "校门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<?> remove(@PathVariable Long[] ids) {
        return gateService.deleteGateByIds(ids) > 0 ? R.ok() : R.error("删除校门失败");
    }
}
