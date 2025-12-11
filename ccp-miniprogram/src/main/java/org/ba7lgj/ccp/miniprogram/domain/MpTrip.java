package org.ba7lgj.ccp.miniprogram.domain;

import java.util.Date;

public class MpTrip {
    private Long id;
    private Long campusId;
    private Long startGateId;
    private Long startLocationId;
    private String startAddress;
    private Long endGateId;
    private Long endLocationId;
    private String endAddress;
    private Integer ownerPeopleCount;
    private Integer totalPeople;
    private Integer currentPeople;
    private Date departureTime;
    private String requireText;
    private Integer status;
    private Date createTime;
    private Date updateTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCampusId() { return campusId; }
    public void setCampusId(Long campusId) { this.campusId = campusId; }
    public Long getStartGateId() { return startGateId; }
    public void setStartGateId(Long startGateId) { this.startGateId = startGateId; }
    public Long getStartLocationId() { return startLocationId; }
    public void setStartLocationId(Long startLocationId) { this.startLocationId = startLocationId; }
    public String getStartAddress() { return startAddress; }
    public void setStartAddress(String startAddress) { this.startAddress = startAddress; }
    public Long getEndGateId() { return endGateId; }
    public void setEndGateId(Long endGateId) { this.endGateId = endGateId; }
    public Long getEndLocationId() { return endLocationId; }
    public void setEndLocationId(Long endLocationId) { this.endLocationId = endLocationId; }
    public String getEndAddress() { return endAddress; }
    public void setEndAddress(String endAddress) { this.endAddress = endAddress; }
    public Integer getOwnerPeopleCount() { return ownerPeopleCount; }
    public void setOwnerPeopleCount(Integer ownerPeopleCount) { this.ownerPeopleCount = ownerPeopleCount; }
    public Integer getTotalPeople() { return totalPeople; }
    public void setTotalPeople(Integer totalPeople) { this.totalPeople = totalPeople; }
    public Integer getCurrentPeople() { return currentPeople; }
    public void setCurrentPeople(Integer currentPeople) { this.currentPeople = currentPeople; }
    public Date getDepartureTime() { return departureTime; }
    public void setDepartureTime(Date departureTime) { this.departureTime = departureTime; }
    public String getRequireText() { return requireText; }
    public void setRequireText(String requireText) { this.requireText = requireText; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public Date getUpdateTime() { return updateTime; }
    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }
}
