package org.ba7lgj.ccp.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.core.domain.CcpLocation;

import java.util.List;

/**
 * 校外地点 Mapper。
 */
public interface CcpLocationMapper {

    CcpLocation selectCcpLocationById(Long id);

    List<CcpLocation> selectCcpLocationList(CcpLocation query);

    int insertCcpLocation(CcpLocation entity);

    int updateCcpLocation(CcpLocation entity);

    int deleteCcpLocationById(Long id);

    int deleteCcpLocationByIds(Long[] ids);

    List<CcpLocation> selectByCampusId(@Param("campusId") Long campusId);

    int changeStatus(@Param("id") Long id, @Param("status") Integer status);
}
