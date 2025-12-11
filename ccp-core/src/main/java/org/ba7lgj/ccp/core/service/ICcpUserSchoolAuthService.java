package org.ba7lgj.ccp.core.service;

import org.ba7lgj.ccp.core.domain.CcpUserSchoolAuth;

import java.util.List;

/**
 * 学校学生认证服务。
 */
public interface ICcpUserSchoolAuthService {

    List<CcpUserSchoolAuth> selectCcpUserSchoolAuthList(CcpUserSchoolAuth query, Long managerUserId, boolean isAdmin);

    CcpUserSchoolAuth selectCcpUserSchoolAuthById(Long id, Long managerUserId, boolean isAdmin);

    int approve(Long id, Long reviewUserId);

    int reject(Long id, String failReason, Long reviewUserId);
}
