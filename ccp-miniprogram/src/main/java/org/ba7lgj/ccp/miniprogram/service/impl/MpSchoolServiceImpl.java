package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.ba7lgj.ccp.miniprogram.domain.MpSchool;
import org.ba7lgj.ccp.miniprogram.mapper.MpSchoolMapper;
import org.ba7lgj.ccp.miniprogram.service.MpSchoolService;
import org.ba7lgj.ccp.miniprogram.vo.MpSchoolVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MpSchoolServiceImpl implements MpSchoolService {
    @Autowired
    private MpSchoolMapper schoolMapper;

    @Override
    public List<MpSchoolVO> listAllEnabledSchools() {
        List<MpSchool> schools = schoolMapper.selectEnabledSchools();
        List<MpSchoolVO> result = new ArrayList<>();
        for (MpSchool school : schools) {
            MpSchoolVO vo = new MpSchoolVO();
            vo.setId(school.getId());
            vo.setSchoolName(school.getSchoolName());
            vo.setSchoolShortName(school.getSchoolShortName());
            vo.setLogoUrl(school.getLogoUrl());
            vo.setCityName("");
            result.add(vo);
        }
        return result;
    }
}
