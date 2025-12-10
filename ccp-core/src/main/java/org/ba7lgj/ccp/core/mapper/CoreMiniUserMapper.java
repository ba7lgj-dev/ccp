package org.ba7lgj.ccp.core.mapper;

import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.dto.MiniUserQueryDTO;

import java.util.List;

/**
 * 小程序用户 Mapper
 */
public interface CoreMiniUserMapper {
    MiniUser selectMiniUserById(Long id);

    MiniUser selectMiniUserByOpenId(String openId);

    List<MiniUser> selectMiniUserList(MiniUserQueryDTO queryDTO);

    List<MiniUser> selectMiniUserExportList(MiniUserQueryDTO queryDTO);

    int insertMiniUser(MiniUser user);

    int updateMiniUser(MiniUser user);

    int deleteMiniUserById(Long id);

    int deleteMiniUserByIds(Long[] ids);
}
