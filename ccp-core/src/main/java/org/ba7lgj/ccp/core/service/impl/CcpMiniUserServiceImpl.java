package org.ba7lgj.ccp.core.service.impl;

import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.core.domain.vo.CcpMiniUserDetailVO;
import org.ba7lgj.ccp.core.domain.vo.MiniUserBlacklistVO;
import org.ba7lgj.ccp.core.domain.vo.MiniUserReputationVO;
import org.ba7lgj.ccp.core.domain.vo.MiniUserTagStatVO;
import org.ba7lgj.ccp.core.mapper.CcpMiniUserMapper;
import org.ba7lgj.ccp.core.service.ICcpMiniUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CcpMiniUserServiceImpl implements ICcpMiniUserService {

    @Resource
    private CcpMiniUserMapper ccpMiniUserMapper;

    @Override
    public MiniUser selectCcpMiniUserById(Long id) {
        return ccpMiniUserMapper.selectCcpMiniUserById(id);
    }

    @Override
    public List<MiniUser> selectCcpMiniUserList(MiniUser query) {
        return ccpMiniUserMapper.selectCcpMiniUserList(query);
    }

    @Override
    public int updateCcpMiniUser(MiniUser user) {
        if (user.getUpdateTime() == null) {
            user.setUpdateTime(new Date());
        }
        return ccpMiniUserMapper.updateCcpMiniUser(user);
    }

    @Override
    public int updateRealAuthStatus(Long id, int status, String failReason, Long reviewBy, Date reviewTime) {
        MiniUser update = new MiniUser();
        update.setId(id);
        update.setRealAuthStatus(status);
        update.setRealAuthFailReason(failReason);
        update.setRealAuthReviewBy(reviewBy);
        update.setRealAuthReviewTime(reviewTime);
        update.setUpdateTime(new Date());
        return ccpMiniUserMapper.updateRealAuthStatus(update);
    }

    @Override
    public CcpMiniUserDetailVO selectDetail(Long id) {
        MiniUser user = ccpMiniUserMapper.selectCcpMiniUserById(id);
        if (user == null) {
            return null;
        }
        CcpMiniUserDetailVO vo = new CcpMiniUserDetailVO();
        vo.setUser(user);
        MiniUserReputationVO reputation = ccpMiniUserMapper.selectUserReputation(id);
        vo.setReputation(reputation);
        List<MiniUserTagStatVO> tagStats = ccpMiniUserMapper.selectUserTagStats(id);
        if (!CollectionUtils.isEmpty(tagStats)) {
            vo.setTagStats(tagStats);
        }
        MiniUserBlacklistVO blacklist = ccpMiniUserMapper.selectUserBlacklist(id);
        vo.setBlacklist(blacklist);
        Integer contactCount = ccpMiniUserMapper.selectEmergencyContactCount(id);
        vo.setEmergencyContactCount(contactCount);
        return vo;
    }
}
