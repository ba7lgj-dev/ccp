package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.ba7lgj.ccp.miniprogram.domain.MpLocation;
import org.ba7lgj.ccp.miniprogram.mapper.MpLocationMapper;
import org.ba7lgj.ccp.miniprogram.service.MpLocationService;
import org.ba7lgj.ccp.miniprogram.vo.MpLocationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MpLocationServiceImpl implements MpLocationService {
    @Autowired
    private MpLocationMapper locationMapper;

    @Override
    public List<MpLocationVO> listByCampus(Long campusId) {
        List<MpLocation> list = locationMapper.selectEnabledByCampus(campusId);
        List<MpLocationVO> result = new ArrayList<>();
        for (MpLocation item : list) {
            MpLocationVO vo = new MpLocationVO();
            vo.setId(item.getId());
            vo.setLocationName(item.getLocationName());
            vo.setLocationType(item.getLocationType());
            vo.setLatitude(item.getLatitude());
            vo.setLongitude(item.getLongitude());
            result.add(vo);
        }
        return result;
    }
}
