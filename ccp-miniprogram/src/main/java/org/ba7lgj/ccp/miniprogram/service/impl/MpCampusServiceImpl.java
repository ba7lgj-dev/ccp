package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.ba7lgj.ccp.miniprogram.domain.MpCampus;
import org.ba7lgj.ccp.miniprogram.mapper.MpCampusMapper;
import org.ba7lgj.ccp.miniprogram.service.MpCampusService;
import org.ba7lgj.ccp.miniprogram.vo.MpCampusVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MpCampusServiceImpl implements MpCampusService {
    @Autowired
    private MpCampusMapper campusMapper;

    @Override
    public List<MpCampusVO> listEnabledCampusBySchool(Long schoolId) {
        List<MpCampus> list = campusMapper.selectEnabledCampusBySchool(schoolId);
        List<MpCampusVO> result = new ArrayList<>();
        for (MpCampus campus : list) {
            MpCampusVO vo = new MpCampusVO();
            vo.setId(campus.getId());
            vo.setCampusName(campus.getCampusName());
            vo.setAddress(campus.getAddress());
            result.add(vo);
        }
        return result;
    }
}
