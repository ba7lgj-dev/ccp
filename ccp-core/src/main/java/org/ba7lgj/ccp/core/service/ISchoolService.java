package org.ba7lgj.ccp.core.service;

import org.ba7lgj.ccp.common.dto.SchoolDTO;
import org.ba7lgj.ccp.common.dto.SchoolQueryDTO;
import org.ba7lgj.ccp.common.vo.SchoolVO;

import java.util.List;

/**
 * 学校管理服务。
 */
public interface ISchoolService {

    SchoolVO selectSchoolById(Long id);

    List<SchoolVO> selectSchoolList(SchoolQueryDTO queryDTO);

    boolean checkSchoolUnique(Long cityId, String schoolName, Long excludeId);

    int insertSchool(SchoolDTO dto);

    int updateSchool(SchoolDTO dto);

    int changeStatus(Long id, Integer status);

    int deleteSchoolByIds(Long[] ids);
}
