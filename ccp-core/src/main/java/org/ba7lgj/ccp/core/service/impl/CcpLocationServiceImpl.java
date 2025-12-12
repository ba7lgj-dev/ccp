package org.ba7lgj.ccp.core.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import org.ba7lgj.ccp.common.vo.CampusVO;
import org.ba7lgj.ccp.core.domain.CcpLocation;
import org.ba7lgj.ccp.core.mapper.CcpLocationMapper;
import org.ba7lgj.ccp.core.mapper.CoreCampusMapper;
import org.ba7lgj.ccp.core.service.ICcpLocationService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 校外地点服务实现。
 */
@Service
public class CcpLocationServiceImpl implements ICcpLocationService {

    @Resource
    private CcpLocationMapper ccpLocationMapper;

    @Resource
    private CoreCampusMapper campusMapper;

    @Override
    public CcpLocation selectCcpLocationById(Long id) {
        return ccpLocationMapper.selectCcpLocationById(id);
    }

    @Override
    public List<CcpLocation> selectCcpLocationList(CcpLocation query) {
        return ccpLocationMapper.selectCcpLocationList(query);
    }

    @Override
    public int insertCcpLocation(CcpLocation entity) {
        validateLocation(entity, true);
        entity.setCreateTime(new Date());
        entity.setCreateBy(SecurityUtils.getUsername());
        return ccpLocationMapper.insertCcpLocation(entity);
    }

    @Override
    public int updateCcpLocation(CcpLocation entity) {
        validateLocation(entity, false);
        entity.setUpdateTime(new Date());
        entity.setUpdateBy(SecurityUtils.getUsername());
        return ccpLocationMapper.updateCcpLocation(entity);
    }

    @Override
    public int deleteCcpLocationByIds(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return ccpLocationMapper.deleteCcpLocationByIds(ids);
    }

    @Override
    public int deleteCcpLocationById(Long id) {
        if (id == null) {
            return 0;
        }
        return ccpLocationMapper.deleteCcpLocationById(id);
    }

    @Override
    public List<CcpLocation> listByCampusId(Long campusId) {
        return ccpLocationMapper.selectByCampusId(campusId);
    }

    @Override
    public int changeStatus(Long id, Integer status) {
        if (id == null || status == null) {
            throw new ServiceException("参数不完整");
        }
        return ccpLocationMapper.changeStatus(id, status);
    }

    private void validateLocation(CcpLocation entity, boolean creating) {
        if (entity == null) {
            throw new ServiceException("地点信息不能为空");
        }
        if (entity.getCampusId() == null) {
            throw new ServiceException("所属校区不能为空");
        }
        CampusVO campus = campusMapper.selectCampusById(entity.getCampusId());
        if (campus == null || campus.getStatus() == null || campus.getStatus() != 1) {
            throw new ServiceException("所属校区不存在或已停用");
        }
        if (!StringUtils.hasText(entity.getLocationName())) {
            throw new ServiceException("地点名称不能为空");
        }
        if (entity.getLocationType() == null) {
            throw new ServiceException("请选择地点类型");
        }
        validateCoordinate(entity.getLatitude(), true, "纬度不合法");
        validateCoordinate(entity.getLongitude(), false, "经度不合法");
        if (creating && entity.getStatus() == null) {
            entity.setStatus(1);
        }
    }

    private void validateCoordinate(BigDecimal coordinate, boolean latitude, String message) {
        if (coordinate == null) {
            throw new ServiceException(message);
        }
        double value = coordinate.doubleValue();
        if (latitude) {
            if (value < -90D || value > 90D) {
                throw new ServiceException(message);
            }
        } else {
            if (value < -180D || value > 180D) {
                throw new ServiceException(message);
            }
        }
    }
}
