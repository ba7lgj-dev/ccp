package org.ba7lgj.ccp.core.service;

import org.ba7lgj.ccp.common.dto.CampusDTO;
import org.ba7lgj.ccp.common.dto.CampusQueryDTO;
import org.ba7lgj.ccp.common.vo.CampusVO;

import java.util.List;

/**
 * 校区管理服务。
 */
public interface ICampusService {

    CampusVO selectCampusById(Long id);

    List<CampusVO> selectCampusList(CampusQueryDTO queryDTO);

    boolean checkCampusUnique(Long schoolId, String campusName, Long excludeId);

    int insertCampus(CampusDTO dto);

    int updateCampus(CampusDTO dto);

    int changeStatus(Long id, Integer status);

    int deleteCampusByIds(Long[] ids);
}
