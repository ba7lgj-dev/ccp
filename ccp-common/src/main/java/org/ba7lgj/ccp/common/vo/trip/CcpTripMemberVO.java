package org.ba7lgj.ccp.common.vo.trip;

import com.ruoyi.common.annotation.Excel;

/**
 * 拼车成员列表展示。
 */
public class CcpTripMemberVO {

    @Excel(name = "成员ID")
    private Long memberId;

    @Excel(name = "订单ID")
    private Long tripId;

    @Excel(name = "用户ID")
    private Long userId;

    @Excel(name = "昵称")
    private String nickName;

    private String realName;

    private Integer gender;

    @Excel(name = "手机号")
    private String phone;

    private Integer role;

    @Excel(name = "角色")
    private String roleLabel;

    @Excel(name = "人数")
    private Integer joinPeopleCount;

    private Integer memberStatus;

    @Excel(name = "状态")
    private String memberStatusLabel;

    @Excel(name = "确认状态")
    private String confirmFlagLabel;

    @Excel(name = "加入时间")
    private String joinTime;

    @Excel(name = "退出时间")
    private String quitTime;

    private Double avgRating;

    private Integer totalNoShow;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getRoleLabel() {
        return roleLabel;
    }

    public void setRoleLabel(String roleLabel) {
        this.roleLabel = roleLabel;
    }

    public Integer getJoinPeopleCount() {
        return joinPeopleCount;
    }

    public void setJoinPeopleCount(Integer joinPeopleCount) {
        this.joinPeopleCount = joinPeopleCount;
    }

    public Integer getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(Integer memberStatus) {
        this.memberStatus = memberStatus;
    }

    public String getMemberStatusLabel() {
        return memberStatusLabel;
    }

    public void setMemberStatusLabel(String memberStatusLabel) {
        this.memberStatusLabel = memberStatusLabel;
    }

    public String getConfirmFlagLabel() {
        return confirmFlagLabel;
    }

    public void setConfirmFlagLabel(String confirmFlagLabel) {
        this.confirmFlagLabel = confirmFlagLabel;
    }

    public String getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(String joinTime) {
        this.joinTime = joinTime;
    }

    public String getQuitTime() {
        return quitTime;
    }

    public void setQuitTime(String quitTime) {
        this.quitTime = quitTime;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getTotalNoShow() {
        return totalNoShow;
    }

    public void setTotalNoShow(Integer totalNoShow) {
        this.totalNoShow = totalNoShow;
    }
}
