package org.ba7lgj.ccp.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.common.domain.Campus;
import org.ba7lgj.ccp.common.dto.CampusQueryDTO;
import org.ba7lgj.ccp.common.vo.CampusVO;

import java.util.List;

/**
 * 校区管理 Mapper。
 */
public interface CampusMapper {

    CampusVO selectCampusById(Long id);

    List<CampusVO> selectCampusList(CampusQueryDTO queryDTO);

    Campus selectCampusByName(@Param("schoolId") Long schoolId, @Param("campusName") String campusName);

    int insertCampus(Campus campus);

    int updateCampus(Campus campus);

    int changeStatus(@Param("id") Long id, @Param("status") Integer status);

    int deleteCampusByIds(Long[] ids);

    int countGateByCampusIds(Long[] ids);
}
