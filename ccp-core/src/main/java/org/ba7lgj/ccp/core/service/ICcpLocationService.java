package org.ba7lgj.ccp.core.service;

import org.ba7lgj.ccp.core.domain.CcpLocation;

import java.util.List;

/**
 * 校外地点服务。
 */
public interface ICcpLocationService {

    CcpLocation selectCcpLocationById(Long id);

    List<CcpLocation> selectCcpLocationList(CcpLocation query);

    int insertCcpLocation(CcpLocation entity);

    int updateCcpLocation(CcpLocation entity);

    int deleteCcpLocationByIds(Long[] ids);

    int deleteCcpLocationById(Long id);

    List<CcpLocation> listByCampusId(Long campusId);

    int changeStatus(Long id, Integer status);
}
