package org.ba7lgj.ccp.core.service.impl;

import com.ruoyi.common.exception.ServiceException;
import org.ba7lgj.ccp.common.domain.School;
import org.ba7lgj.ccp.common.dto.SchoolDTO;
import org.ba7lgj.ccp.common.dto.SchoolQueryDTO;
import org.ba7lgj.ccp.common.vo.SchoolVO;
import org.ba7lgj.ccp.core.mapper.CoreSchoolMapper;
import org.ba7lgj.ccp.core.service.CoreSchoolService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 学校管理服务实现。
 */
@Service
public class CoreSchoolServiceImpl implements CoreSchoolService {

    @Resource
    private CoreSchoolMapper schoolMapper;

    @Override
    public SchoolVO selectSchoolById(Long id) {
        return schoolMapper.selectSchoolById(id);
    }

    @Override
    public List<SchoolVO> selectSchoolList(SchoolQueryDTO queryDTO) {
        return schoolMapper.selectSchoolList(queryDTO);
    }

    @Override
    public boolean checkSchoolUnique(Long cityId, String schoolName, Long excludeId) {
        if (cityId == null || !StringUtils.hasText(schoolName)) {
            return true;
        }
        School exist = schoolMapper.selectSchoolByName(cityId, schoolName);
        return exist == null || (excludeId != null && excludeId.equals(exist.getId()));
    }

    @Override
    public int insertSchool(SchoolDTO dto) {
        School school = new School();
        BeanUtils.copyProperties(dto, school);
        school.setCreateTime(new Date());
        return schoolMapper.insertSchool(school);
    }

    @Override
    public int updateSchool(SchoolDTO dto) {
        School school = new School();
        BeanUtils.copyProperties(dto, school);
        school.setUpdateTime(new Date());
        return schoolMapper.updateSchool(school);
    }

    @Override
    public int changeStatus(Long id, Integer status) {
        return schoolMapper.changeStatus(id, status);
    }

    @Override
    public int deleteSchoolByIds(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        int campusCount = schoolMapper.countCampusBySchoolIds(ids);
        if (campusCount > 0) {
            throw new ServiceException("存在关联校区，无法删除");
        }
        return schoolMapper.deleteSchoolByIds(ids);
    }
}
