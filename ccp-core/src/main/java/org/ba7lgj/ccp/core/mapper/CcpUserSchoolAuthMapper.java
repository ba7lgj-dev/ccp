package org.ba7lgj.ccp.core.mapper;

import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.core.domain.CcpUserSchoolAuth;

import java.util.List;

/**
 * 学校学生认证Mapper。
 */
public interface CcpUserSchoolAuthMapper {

    List<CcpUserSchoolAuth> selectCcpUserSchoolAuthList(CcpUserSchoolAuth query);

    CcpUserSchoolAuth selectCcpUserSchoolAuthById(@Param("id") Long id, @Param("managerUserId") Long managerUserId);

    int insertCcpUserSchoolAuth(CcpUserSchoolAuth entity);

    int updateCcpUserSchoolAuth(CcpUserSchoolAuth entity);
}
