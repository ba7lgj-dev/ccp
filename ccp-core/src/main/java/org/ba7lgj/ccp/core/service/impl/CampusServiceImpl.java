package org.ba7lgj.ccp.core.service.impl;

import com.ruoyi.common.exception.ServiceException;
import org.ba7lgj.ccp.common.domain.Campus;
import org.ba7lgj.ccp.common.dto.CampusDTO;
import org.ba7lgj.ccp.common.dto.CampusQueryDTO;
import org.ba7lgj.ccp.common.vo.CampusVO;
import org.ba7lgj.ccp.core.mapper.CampusMapper;
import org.ba7lgj.ccp.core.service.ICampusService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 校区管理服务实现。
 */
@Service
public class CampusServiceImpl implements ICampusService {

    @Resource
    private CampusMapper campusMapper;

    @Override
    public CampusVO selectCampusById(Long id) {
        return campusMapper.selectCampusById(id);
    }

    @Override
    public List<CampusVO> selectCampusList(CampusQueryDTO queryDTO) {
        return campusMapper.selectCampusList(queryDTO);
    }

    @Override
    public boolean checkCampusUnique(Long schoolId, String campusName, Long excludeId) {
        if (schoolId == null || !StringUtils.hasText(campusName)) {
            return true;
        }
        Campus exist = campusMapper.selectCampusByName(schoolId, campusName);
        return exist == null || (excludeId != null && excludeId.equals(exist.getId()));
    }

    @Override
    public int insertCampus(CampusDTO dto) {
        Campus campus = new Campus();
        BeanUtils.copyProperties(dto, campus);
        campus.setCreateTime(new Date());
        return campusMapper.insertCampus(campus);
    }

    @Override
    public int updateCampus(CampusDTO dto) {
        Campus campus = new Campus();
        BeanUtils.copyProperties(dto, campus);
        campus.setUpdateTime(new Date());
        return campusMapper.updateCampus(campus);
    }

    @Override
    public int changeStatus(Long id, Integer status) {
        return campusMapper.changeStatus(id, status);
    }

    @Override
    public int deleteCampusByIds(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        int gateCount = campusMapper.countGateByCampusIds(ids);
        if (gateCount > 0) {
            throw new ServiceException("存在关联校门，无法删除");
        }
        return campusMapper.deleteCampusByIds(ids);
    }
}
