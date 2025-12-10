package org.ba7lgj.ccp.admin.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.ba7lgj.ccp.common.dto.CampusDTO;
import org.ba7lgj.ccp.common.dto.CampusQueryDTO;
import org.ba7lgj.ccp.core.service.ICampusService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 校区管理接口。
 */
@RestController
@RequestMapping("/admin/campus")
public class AdminCampusController extends BaseController {

    @Resource
    private ICampusService campusService;

    @PreAuthorize("@ss.hasPermi('ccp:campus:list')")
    @GetMapping("/list")
    public R<TableDataInfo> list(CampusQueryDTO queryDTO) {
        startPage();
        List<?> list = campusService.selectCampusList(queryDTO);
        return R.ok(getDataTable(list));
    }

    @PreAuthorize("@ss.hasPermi('ccp:campus:query')")
    @GetMapping("/{id}")
    public R<?> getInfo(@PathVariable Long id) {
        return R.ok(campusService.selectCampusById(id));
    }

    @PreAuthorize("@ss.hasPermi('ccp:campus:add')")
    @Log(title = "校区管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<?> add(@Validated @RequestBody CampusDTO dto) {
        if (!campusService.checkCampusUnique(dto.getSchoolId(), dto.getCampusName(), null)) {
            return R.error("同一学校下校区名称已存在");
        }
        return campusService.insertCampus(dto) > 0 ? R.ok() : R.error("新增校区失败");
    }

    @PreAuthorize("@ss.hasPermi('ccp:campus:edit')")
    @Log(title = "校区管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<?> edit(@Validated @RequestBody CampusDTO dto) {
        if (dto.getId() == null) {
            return R.error("校区ID不能为空");
        }
        if (!campusService.checkCampusUnique(dto.getSchoolId(), dto.getCampusName(), dto.getId())) {
            return R.error("同一学校下校区名称已存在");
        }
        return campusService.updateCampus(dto) > 0 ? R.ok() : R.error("修改校区失败");
    }

    @PreAuthorize("@ss.hasPermi('ccp:campus:edit')")
    @Log(title = "校区管理", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public R<?> changeStatus(@RequestBody CampusDTO dto) {
        if (dto.getId() == null || Objects.isNull(dto.getStatus())) {
            return R.error("参数不完整");
        }
        return campusService.changeStatus(dto.getId(), dto.getStatus()) > 0 ? R.ok() : R.error("更新状态失败");
    }

    @PreAuthorize("@ss.hasPermi('ccp:campus:remove')")
    @Log(title = "校区管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<?> remove(@PathVariable Long[] ids) {
        return campusService.deleteCampusByIds(ids) > 0 ? R.ok() : R.error("删除校区失败");
    }
}
