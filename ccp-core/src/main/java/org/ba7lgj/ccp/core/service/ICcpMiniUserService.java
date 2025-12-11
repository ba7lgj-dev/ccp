package org.ba7lgj.ccp.core.service;

import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.core.domain.vo.CcpMiniUserDetailVO;

import java.util.Date;
import java.util.List;

/**
 * 后台小程序用户管理服务接口。
 */
public interface ICcpMiniUserService {

    MiniUser selectCcpMiniUserById(Long id);

    List<MiniUser> selectCcpMiniUserList(MiniUser query);

    int updateCcpMiniUser(MiniUser user);

    int updateRealAuthStatus(Long id, int status, String failReason, Long reviewBy, Date reviewTime);

    CcpMiniUserDetailVO selectDetail(Long id);
}
