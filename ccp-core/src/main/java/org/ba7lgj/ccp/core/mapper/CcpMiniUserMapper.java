package org.ba7lgj.ccp.core.mapper;

import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.core.domain.vo.MiniUserBlacklistVO;
import org.ba7lgj.ccp.core.domain.vo.MiniUserReputationVO;
import org.ba7lgj.ccp.core.domain.vo.MiniUserTagStatVO;
import org.ba7lgj.ccp.common.dto.CcpUserRealAuthQueryDTO;
import org.ba7lgj.ccp.core.domain.vo.CcpUserRealAuthDetailVO;
import org.ba7lgj.ccp.core.domain.vo.CcpUserRealAuthListVO;

import java.util.List;

/**
 * 后台小程序用户管理 Mapper。
 */
public interface CcpMiniUserMapper {

    MiniUser selectCcpMiniUserById(Long id);

    List<MiniUser> selectCcpMiniUserList(MiniUser query);

    int updateCcpMiniUser(MiniUser user);

    int updateRealAuthStatus(MiniUser user);

    List<CcpUserRealAuthListVO> selectUserRealAuthList(CcpUserRealAuthQueryDTO queryDTO);

    CcpUserRealAuthDetailVO selectUserRealAuthDetail(Long userId);

    MiniUserReputationVO selectUserReputation(Long userId);

    List<MiniUserTagStatVO> selectUserTagStats(Long userId);

    MiniUserBlacklistVO selectUserBlacklist(Long userId);

    Integer selectEmergencyContactCount(Long userId);
}
