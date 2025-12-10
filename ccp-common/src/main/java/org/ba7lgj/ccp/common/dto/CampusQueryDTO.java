package org.ba7lgj.ccp.common.dto;

/**
 * 校区查询 DTO。
 */
public class CampusQueryDTO {
    private Long schoolId;
    private String campusName;
    private Integer status;

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
