package org.ba7lgj.ccp.common.dto;

/**
 * 学校分页查询 DTO。
 */
public class SchoolQueryDTO {
    private String schoolName;
    private Long cityId;
    private Integer status;

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
