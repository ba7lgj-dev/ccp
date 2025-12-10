package org.ba7lgj.ccp.admin.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import org.ba7lgj.ccp.common.dto.SchoolDTO;
import org.ba7lgj.ccp.common.dto.SchoolQueryDTO;
import org.ba7lgj.ccp.core.service.CoreSchoolService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 学校管理接口。
 */
@RestController
@RequestMapping("/admin/school")
public class AdminSchoolController extends BaseController {

    @Resource
    private CoreSchoolService schoolService;

    @PreAuthorize("@ss.hasPermi('ccp:school:list')")
    @GetMapping("/list")
    public R<TableDataInfo> list(SchoolQueryDTO queryDTO) {
        startPage();
        List<?> list = schoolService.selectSchoolList(queryDTO);
        return R.ok(getDataTable(list));
    }

    @PreAuthorize("@ss.hasPermi('ccp:school:query')")
    @GetMapping("/{id}")
    public R<?> getInfo(@PathVariable Long id) {
        return R.ok(schoolService.selectSchoolById(id));
    }

    @PreAuthorize("@ss.hasPermi('ccp:school:add')")
    @Log(title = "学校管理", businessType = BusinessType.INSERT)
    @PostMapping
    public R<?> add(@Validated @RequestBody SchoolDTO dto) {
        if (!schoolService.checkSchoolUnique(dto.getCityId(), dto.getSchoolName(), null)) {
            return R.error("同一城市下学校名称已存在");
        }
        return schoolService.insertSchool(dto) > 0 ? R.ok() : R.error("新增学校失败");
    }

    @PreAuthorize("@ss.hasPermi('ccp:school:edit')")
    @Log(title = "学校管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<?> edit(@Validated @RequestBody SchoolDTO dto) {
        if (dto.getId() == null) {
            return R.error("学校ID不能为空");
        }
        if (!schoolService.checkSchoolUnique(dto.getCityId(), dto.getSchoolName(), dto.getId())) {
            return R.error("同一城市下学校名称已存在");
        }
        return schoolService.updateSchool(dto) > 0 ? R.ok() : R.error("修改学校失败");
    }

    @PreAuthorize("@ss.hasPermi('ccp:school:edit')")
    @Log(title = "学校管理", businessType = BusinessType.UPDATE)
    @PutMapping("/status")
    public R<?> changeStatus(@RequestBody SchoolDTO dto) {
        if (dto.getId() == null || Objects.isNull(dto.getStatus())) {
            return R.error("参数不完整");
        }
        return schoolService.changeStatus(dto.getId(), dto.getStatus()) > 0 ? R.ok() : R.error("更新状态失败");
    }

    @PreAuthorize("@ss.hasPermi('ccp:school:remove')")
    @Log(title = "学校管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<?> remove(@PathVariable Long[] ids) {
        return schoolService.deleteSchoolByIds(ids) > 0 ? R.ok() : R.error("删除学校失败");
    }
}
