package org.ba7lgj.ccp.miniprogram.domain;

import java.util.Date;

public class User {
    private Long id;
    private String openId;
    private String phone;
    private Long selectedSchoolId;
    private Long selectedCampusId;
    private Date createTime;
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getSelectedSchoolId() {
        return selectedSchoolId;
    }

    public void setSelectedSchoolId(Long selectedSchoolId) {
        this.selectedSchoolId = selectedSchoolId;
    }

    public Long getSelectedCampusId() {
        return selectedCampusId;
    }

    public void setSelectedCampusId(Long selectedCampusId) {
        this.selectedCampusId = selectedCampusId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
