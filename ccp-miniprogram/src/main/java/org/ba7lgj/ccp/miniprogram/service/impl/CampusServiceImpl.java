package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.ba7lgj.ccp.miniprogram.domain.Campus;
import org.ba7lgj.ccp.miniprogram.mapper.CampusMapper;
import org.ba7lgj.ccp.miniprogram.service.CampusService;
import org.ba7lgj.ccp.miniprogram.vo.CampusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CampusServiceImpl implements CampusService {
    @Autowired
    private CampusMapper campusMapper;

    @Override
    public List<CampusVO> listEnabledCampusBySchool(Long schoolId) {
        List<Campus> list = campusMapper.selectEnabledCampusBySchool(schoolId);
        List<CampusVO> result = new ArrayList<>();
        for (Campus campus : list) {
            CampusVO vo = new CampusVO();
            vo.setId(campus.getId());
            vo.setCampusName(campus.getCampusName());
            vo.setAddress(campus.getAddress());
            result.add(vo);
        }
        return result;
    }
}
