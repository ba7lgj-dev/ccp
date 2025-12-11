package org.ba7lgj.ccp.miniprogram.domain;

import java.util.Date;

public class MpUser {
    private Long id;
    private String openId;
    private String unionId;
    private String nickName;
    private String avatarUrl;
    private String phone;
    private String realName;
    private String idCardName;
    private String idCardNumber;
    private String faceImageUrl;
    private String faceVerifyResult;
    private Integer gender;
    private Integer status;
    private Integer realAuthStatus;
    private String realAuthFailReason;
    private Long realAuthReviewBy;
    private Date realAuthReviewTime;
    private String adminRemark;
    private Long currentSchoolId;
    private Long currentCampusId;
    private Date lastActiveTime;
    private Integer onlineStatus;
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

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIdCardName() {
        return idCardName;
    }

    public void setIdCardName(String idCardName) {
        this.idCardName = idCardName;
    }

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getFaceImageUrl() {
        return faceImageUrl;
    }

    public void setFaceImageUrl(String faceImageUrl) {
        this.faceImageUrl = faceImageUrl;
    }

    public String getFaceVerifyResult() {
        return faceVerifyResult;
    }

    public void setFaceVerifyResult(String faceVerifyResult) {
        this.faceVerifyResult = faceVerifyResult;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getRealAuthStatus() {
        return realAuthStatus;
    }

    public void setRealAuthStatus(Integer realAuthStatus) {
        this.realAuthStatus = realAuthStatus;
    }

    public String getRealAuthFailReason() {
        return realAuthFailReason;
    }

    public void setRealAuthFailReason(String realAuthFailReason) {
        this.realAuthFailReason = realAuthFailReason;
    }

    public Long getRealAuthReviewBy() {
        return realAuthReviewBy;
    }

    public void setRealAuthReviewBy(Long realAuthReviewBy) {
        this.realAuthReviewBy = realAuthReviewBy;
    }

    public Date getRealAuthReviewTime() {
        return realAuthReviewTime;
    }

    public void setRealAuthReviewTime(Date realAuthReviewTime) {
        this.realAuthReviewTime = realAuthReviewTime;
    }

    public String getAdminRemark() {
        return adminRemark;
    }

    public void setAdminRemark(String adminRemark) {
        this.adminRemark = adminRemark;
    }

    public Long getCurrentSchoolId() {
        return currentSchoolId;
    }

    public void setCurrentSchoolId(Long currentSchoolId) {
        this.currentSchoolId = currentSchoolId;
    }

    public Long getCurrentCampusId() {
        return currentCampusId;
    }

    public void setCurrentCampusId(Long currentCampusId) {
        this.currentCampusId = currentCampusId;
    }

    public Date getLastActiveTime() {
        return lastActiveTime;
    }

    public void setLastActiveTime(Date lastActiveTime) {
        this.lastActiveTime = lastActiveTime;
    }

    public Integer getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(Integer onlineStatus) {
        this.onlineStatus = onlineStatus;
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
