package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.ba7lgj.ccp.miniprogram.domain.School;
import org.ba7lgj.ccp.miniprogram.mapper.SchoolMapper;
import org.ba7lgj.ccp.miniprogram.service.SchoolService;
import org.ba7lgj.ccp.miniprogram.vo.SchoolVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolServiceImpl implements SchoolService {
    @Autowired
    private SchoolMapper schoolMapper;

    @Override
    public List<SchoolVO> listAllEnabledSchools() {
        List<School> schools = schoolMapper.selectEnabledSchools();
        List<SchoolVO> result = new ArrayList<>();
        for (School school : schools) {
            SchoolVO vo = new SchoolVO();
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
