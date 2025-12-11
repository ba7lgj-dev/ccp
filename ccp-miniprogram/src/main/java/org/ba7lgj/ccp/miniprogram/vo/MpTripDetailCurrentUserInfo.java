package org.ba7lgj.ccp.miniprogram.vo;

public class MpTripDetailCurrentUserInfo {
    private Boolean joined;
    private Boolean owner;
    private Integer memberStatus;
    private Boolean canJoin;
    private Boolean canQuit;
    private Boolean canKick;

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
}
