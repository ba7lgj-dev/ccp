package org.ba7lgj.ccp.common.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 学校新增/修改 DTO。
 */
public class SchoolDTO {

    private Long id;

    @NotNull(message = "城市不能为空")
    private Long cityId;

    @NotBlank(message = "学校名称不能为空")
    @Size(max = 100, message = "学校名称长度不能超过100字符")
    private String schoolName;

    @Size(max = 100, message = "学校简称长度不能超过100字符")
    private String schoolShortName;

    @Size(max = 255, message = "Logo地址长度不能超过255字符")
    private String logoUrl;

    @Size(max = 255, message = "地址长度不能超过255字符")
    private String address;

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

    public Long getCityId() {
        return cityId;
    }

    public void setCityId(Long cityId) {
        this.cityId = cityId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolShortName() {
        return schoolShortName;
    }

    public void setSchoolShortName(String schoolShortName) {
        this.schoolShortName = schoolShortName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
