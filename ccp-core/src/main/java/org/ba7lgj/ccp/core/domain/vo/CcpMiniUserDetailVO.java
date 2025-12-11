package org.ba7lgj.ccp.core.domain.vo;

import org.ba7lgj.ccp.common.domain.MiniUser;

import java.io.Serializable;
import java.util.List;

/**
 * 小程序用户详情视图对象。
 */
public class CcpMiniUserDetailVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private MiniUser user;
    private MiniUserReputationVO reputation;
    private List<MiniUserTagStatVO> tagStats;
    private MiniUserBlacklistVO blacklist;
    private Integer emergencyContactCount;

    public MiniUser getUser() {
        return user;
    }

    public void setUser(MiniUser user) {
        this.user = user;
    }

    public MiniUserReputationVO getReputation() {
        return reputation;
    }

    public void setReputation(MiniUserReputationVO reputation) {
        this.reputation = reputation;
    }

    public List<MiniUserTagStatVO> getTagStats() {
        return tagStats;
    }

    public void setTagStats(List<MiniUserTagStatVO> tagStats) {
        this.tagStats = tagStats;
    }

    public MiniUserBlacklistVO getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(MiniUserBlacklistVO blacklist) {
        this.blacklist = blacklist;
    }

    public Integer getEmergencyContactCount() {
        return emergencyContactCount;
    }

    public void setEmergencyContactCount(Integer emergencyContactCount) {
        this.emergencyContactCount = emergencyContactCount;
    }
}
