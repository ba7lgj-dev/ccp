package org.ba7lgj.ccp.miniprogram.vo;

public class MpTripMemberVO {
    private Long userId;
    private String nickName;
    private String avatarUrl;
    private Integer role;
    private Integer joinPeopleCount;
    private Integer memberStatus;
    private Integer confirmFlag;
    private Boolean currentUser;

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getNickName() { return nickName; }
    public void setNickName(String nickName) { this.nickName = nickName; }
    public String getAvatarUrl() { return avatarUrl; }
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    public Integer getRole() { return role; }
    public void setRole(Integer role) { this.role = role; }
    public Integer getJoinPeopleCount() { return joinPeopleCount; }
    public void setJoinPeopleCount(Integer joinPeopleCount) { this.joinPeopleCount = joinPeopleCount; }
    public Integer getMemberStatus() { return memberStatus; }
    public void setMemberStatus(Integer memberStatus) { this.memberStatus = memberStatus; }
    public Integer getConfirmFlag() { return confirmFlag; }
    public void setConfirmFlag(Integer confirmFlag) { this.confirmFlag = confirmFlag; }
    public Boolean getCurrentUser() { return currentUser; }
    public void setCurrentUser(Boolean currentUser) { this.currentUser = currentUser; }
}
