package org.ba7lgj.ccp.core.service;

import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.dto.MiniUserQueryDTO;

import java.util.Date;
import java.util.List;

/**
 * 小程序用户业务接口。
 */
public interface CoreMiniUserService {
    MiniUser selectMiniUserById(Long id);

    MiniUser selectMiniUserByOpenId(String openId);

    List<MiniUser> selectMiniUserList(MiniUserQueryDTO queryDTO);

    List<MiniUser> selectMiniUserExportList(MiniUserQueryDTO queryDTO);

    MiniUser autoRegisterByOpenId(String openId);

    int insertMiniUser(MiniUser user);

    int updateMiniUser(MiniUser user);

    int changeStatus(Long id, Integer status);

    int reviewUser(Long id, Integer targetRealAuthStatus, String failReason, Long reviewBy, Date reviewTime);

    int deleteMiniUserById(Long id);

    int deleteMiniUserByIds(Long[] ids);
}
