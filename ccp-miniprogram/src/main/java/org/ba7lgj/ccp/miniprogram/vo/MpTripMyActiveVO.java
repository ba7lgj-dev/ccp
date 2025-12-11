package org.ba7lgj.ccp.miniprogram.vo;

public class MpTripMyActiveVO {
    private Long tripId;
    private String startAddress;
    private String endAddress;
    private String departureTimeDisplay;
    private String statusText;
    private Integer status;

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    public String getStartAddress() { return startAddress; }
    public void setStartAddress(String startAddress) { this.startAddress = startAddress; }
    public String getEndAddress() { return endAddress; }
    public void setEndAddress(String endAddress) { this.endAddress = endAddress; }
    public String getDepartureTimeDisplay() { return departureTimeDisplay; }
    public void setDepartureTimeDisplay(String departureTimeDisplay) { this.departureTimeDisplay = departureTimeDisplay; }
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
}
