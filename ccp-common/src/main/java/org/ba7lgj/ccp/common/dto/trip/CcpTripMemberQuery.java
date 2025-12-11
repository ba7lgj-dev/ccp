package org.ba7lgj.ccp.common.dto.trip;

/**
 * 后台拼车成员查询条件。
 */
public class CcpTripMemberQuery {

    private Long tripId;

    private Long userId;

    private String phone;

    private String nickName;

    private Integer role;

    private Integer memberStatus;

    private String beginJoinTime;

    private String endJoinTime;

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public Integer getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Integer memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getBeginJoinTime() {
        return beginJoinTime;
    }

    public void setBeginJoinTime(String beginJoinTime) {
        this.beginJoinTime = beginJoinTime;
    }

    public String getEndJoinTime() {
        return endJoinTime;
    }

    public void setEndJoinTime(String endJoinTime) {
        this.endJoinTime = endJoinTime;
    }
}
