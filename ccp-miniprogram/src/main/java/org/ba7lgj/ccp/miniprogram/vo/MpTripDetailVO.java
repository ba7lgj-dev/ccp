package org.ba7lgj.ccp.miniprogram.vo;

import java.util.List;

public class MpTripDetailVO {
    private Long tripId;
    private Long schoolId;
    private Long campusId;
    private Long ownerUserId;
    private String ownerNickName;
    private String ownerAvatar;
    private String ownerReputationSummary;
    private String startAddress;
    private String endAddress;
    private String departureTime;
    private String departureTimeDisplay;
    private Integer status;
    private String statusText;
    private Integer totalPeople;
    private Integer ownerPeopleCount;
    private Integer currentPeople;
    private Integer remainPeople;
    private String requireText;
    private List<MpTripMemberVO> members;
    private MpTripDetailCurrentUserInfo currentUserInfo;
    private Boolean immediateTrip;
    private Integer remainMinutesForImmediate;
    private Boolean needRedirect;
    private Integer unreadChatCount;

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    public Long getSchoolId() { return schoolId; }
    public void setSchoolId(Long schoolId) { this.schoolId = schoolId; }
    public Long getCampusId() { return campusId; }
    public void setCampusId(Long campusId) { this.campusId = campusId; }
    public Long getOwnerUserId() { return ownerUserId; }
    public void setOwnerUserId(Long ownerUserId) { this.ownerUserId = ownerUserId; }
    public String getOwnerNickName() { return ownerNickName; }
    public void setOwnerNickName(String ownerNickName) { this.ownerNickName = ownerNickName; }
    public String getOwnerAvatar() { return ownerAvatar; }
    public void setOwnerAvatar(String ownerAvatar) { this.ownerAvatar = ownerAvatar; }
    public String getOwnerReputationSummary() { return ownerReputationSummary; }
    public void setOwnerReputationSummary(String ownerReputationSummary) { this.ownerReputationSummary = ownerReputationSummary; }
    public String getStartAddress() { return startAddress; }
    public void setStartAddress(String startAddress) { this.startAddress = startAddress; }
    public String getEndAddress() { return endAddress; }
    public void setEndAddress(String endAddress) { this.endAddress = endAddress; }
    public String getDepartureTime() { return departureTime; }
    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }
    public String getDepartureTimeDisplay() { return departureTimeDisplay; }
    public void setDepartureTimeDisplay(String departureTimeDisplay) { this.departureTimeDisplay = departureTimeDisplay; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public Integer getTotalPeople() { return totalPeople; }
    public void setTotalPeople(Integer totalPeople) { this.totalPeople = totalPeople; }
    public Integer getOwnerPeopleCount() { return ownerPeopleCount; }
    public void setOwnerPeopleCount(Integer ownerPeopleCount) { this.ownerPeopleCount = ownerPeopleCount; }
    public Integer getCurrentPeople() { return currentPeople; }
    public void setCurrentPeople(Integer currentPeople) { this.currentPeople = currentPeople; }
    public Integer getRemainPeople() { return remainPeople; }
    public void setRemainPeople(Integer remainPeople) { this.remainPeople = remainPeople; }
    public String getRequireText() { return requireText; }
    public void setRequireText(String requireText) { this.requireText = requireText; }
    public List<MpTripMemberVO> getMembers() { return members; }
    public void setMembers(List<MpTripMemberVO> members) { this.members = members; }
    public MpTripDetailCurrentUserInfo getCurrentUserInfo() { return currentUserInfo; }
    public void setCurrentUserInfo(MpTripDetailCurrentUserInfo currentUserInfo) { this.currentUserInfo = currentUserInfo; }
    public Boolean getImmediateTrip() { return immediateTrip; }
    public void setImmediateTrip(Boolean immediateTrip) { this.immediateTrip = immediateTrip; }
    public Integer getRemainMinutesForImmediate() { return remainMinutesForImmediate; }
    public void setRemainMinutesForImmediate(Integer remainMinutesForImmediate) { this.remainMinutesForImmediate = remainMinutesForImmediate; }
    public Boolean getNeedRedirect() { return needRedirect; }
    public void setNeedRedirect(Boolean needRedirect) { this.needRedirect = needRedirect; }
    public Integer getUnreadChatCount() { return unreadChatCount; }
    public void setUnreadChatCount(Integer unreadChatCount) { this.unreadChatCount = unreadChatCount; }
}
