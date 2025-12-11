package org.ba7lgj.ccp.common.vo.trip;

import com.ruoyi.common.annotation.Excel;

/**
 * 拼车订单后台列表展示。
 */
public class CcpTripAdminVO {

    @Excel(name = "订单ID")
    private Long id;

    @Excel(name = "学校")
    private String schoolName;

    @Excel(name = "校区")
    private String campusName;

    @Excel(name = "发起人")
    private String ownerNickName;

    @Excel(name = "起点")
    private String startAddress;

    @Excel(name = "终点")
    private String endAddress;

    @Excel(name = "出发时间")
    private String departureTime;

    @Excel(name = "状态")
    private Integer status;

    @Excel(name = "状态标签")
    private String statusLabel;

    @Excel(name = "当前人数")
    private Integer currentPeople;

    @Excel(name = "总人数")
    private Integer totalPeople;

    @Excel(name = "创建时间")
    private String createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getOwnerNickName() {
        return ownerNickName;
    }

    public void setOwnerNickName(String ownerNickName) {
        this.ownerNickName = ownerNickName;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(String statusLabel) {
        this.statusLabel = statusLabel;
    }

    public Integer getCurrentPeople() {
        return currentPeople;
    }

    public void setCurrentPeople(Integer currentPeople) {
        this.currentPeople = currentPeople;
    }

    public Integer getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(Integer totalPeople) {
        this.totalPeople = totalPeople;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
