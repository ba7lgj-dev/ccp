package org.ba7lgj.ccp.miniprogram.vo;

public class MpTripDetailCurrentUserInfo {
    private Boolean joined;
    private Boolean owner;
    private Integer memberStatus;
    private Boolean canJoin;
    private Boolean canQuit;
    private Boolean canKick;
    private Integer confirmFlag;
    private Boolean canConfirmStart;
    private Boolean canConfirm;

    public Boolean getJoined() { return joined; }
    public void setJoined(Boolean joined) { this.joined = joined; }
    public Boolean getOwner() { return owner; }
    public void setOwner(Boolean owner) { this.owner = owner; }
    public Integer getMemberStatus() { return memberStatus; }
    public void setMemberStatus(Integer memberStatus) { this.memberStatus = memberStatus; }
    public Boolean getCanJoin() { return canJoin; }
    public void setCanJoin(Boolean canJoin) { this.canJoin = canJoin; }
    public Boolean getCanQuit() { return canQuit; }
    public void setCanQuit(Boolean canQuit) { this.canQuit = canQuit; }
    public Boolean getCanKick() { return canKick; }
    public void setCanKick(Boolean canKick) { this.canKick = canKick; }
    public Integer getConfirmFlag() { return confirmFlag; }
    public void setConfirmFlag(Integer confirmFlag) { this.confirmFlag = confirmFlag; }
    public Boolean getCanConfirmStart() { return canConfirmStart; }
    public void setCanConfirmStart(Boolean canConfirmStart) { this.canConfirmStart = canConfirmStart; }
    public Boolean getCanConfirm() { return canConfirm; }
    public void setCanConfirm(Boolean canConfirm) { this.canConfirm = canConfirm; }
}
