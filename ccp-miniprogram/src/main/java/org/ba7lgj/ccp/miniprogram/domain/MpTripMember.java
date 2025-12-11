package org.ba7lgj.ccp.miniprogram.domain;

import java.util.Date;

public class MpTripMember {
    private Long id;
    private Long tripId;
    private Long userId;
    private Integer role;
    private Integer joinPeopleCount;
    private Integer status;
    private Integer confirmFlag;
    private Date joinTime;
    private Date quitTime;
    private Date createTime;
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }
    public Integer getJoinPeopleCount() { return joinPeopleCount; }
    public void setJoinPeopleCount(Integer joinPeopleCount) { this.joinPeopleCount = joinPeopleCount; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Integer getConfirmFlag() { return confirmFlag; }
    public void setConfirmFlag(Integer confirmFlag) { this.confirmFlag = confirmFlag; }
    public Date getJoinTime() { return joinTime; }
    public void setJoinTime(Date joinTime) { this.joinTime = joinTime; }
    public Date getQuitTime() { return quitTime; }
    public void setQuitTime(Date quitTime) { this.quitTime = quitTime; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
