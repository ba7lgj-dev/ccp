package org.ba7lgj.ccp.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.common.domain.School;
import org.ba7lgj.ccp.common.dto.SchoolQueryDTO;
import org.ba7lgj.ccp.common.vo.SchoolVO;

import java.util.List;

/**
 * 学校管理 Mapper。
 */
public interface CoreSchoolMapper {

    SchoolVO selectSchoolById(Long id);

    List<SchoolVO> selectSchoolList(SchoolQueryDTO queryDTO);

    School selectSchoolByName(@Param("cityId") Long cityId, @Param("schoolName") String schoolName);

    int insertSchool(School school);

    int updateSchool(School school);

    int changeStatus(@Param("id") Long id, @Param("status") Integer status);

    int deleteSchoolByIds(Long[] ids);

    int countCampusBySchoolIds(Long[] ids);
}
