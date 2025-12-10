package org.ba7lgj.ccp.common.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 校区新增/修改 DTO。
 */
public class CampusDTO {

    private Long id;

    @NotNull(message = "所属学校不能为空")
    private Long schoolId;

    @NotBlank(message = "校区名称不能为空")
    @Size(max = 100, message = "校区名称长度不能超过100字符")
    private String campusName;

    @Size(max = 255, message = "地址长度不能超过255字符")
    private String address;

    private Double latitude;

    private Double longitude;

    private Long managerUserId;

    @NotNull(message = "状态不能为空")
    private Integer status;

    @Size(max = 500, message = "备注长度不能超过500字符")
    private String remark;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Long getManagerUserId() {
        return managerUserId;
    }

    public void setManagerUserId(Long managerUserId) {
        this.managerUserId = managerUserId;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
