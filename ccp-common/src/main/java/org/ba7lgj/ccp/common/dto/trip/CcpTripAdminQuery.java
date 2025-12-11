package org.ba7lgj.ccp.common.dto.trip;

/**
 * 拼车订单后台查询条件。
 */
public class CcpTripAdminQuery {

    private Long schoolId;

    private Long campusId;

    private Long ownerUserId;

    private Integer status;

    private String beginDepartureTime;

    private String endDepartureTime;

    private String keyword;

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getCampusId() {
        return campusId;
    }

    public void setCampusId(Long campusId) {
        this.campusId = campusId;
    }

    public Long getOwnerUserId() {
        return ownerUserId;
    }

    public void setOwnerUserId(Long ownerUserId) {
        this.ownerUserId = ownerUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getBeginDepartureTime() {
        return beginDepartureTime;
    }

    public void setBeginDepartureTime(String beginDepartureTime) {
        this.beginDepartureTime = beginDepartureTime;
    }

    public String getEndDepartureTime() {
        return endDepartureTime;
    }

    public void setEndDepartureTime(String endDepartureTime) {
        this.endDepartureTime = endDepartureTime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
