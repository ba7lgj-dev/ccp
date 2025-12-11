package org.ba7lgj.ccp.common.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 纯 POJO 对应 ccp_mini_user 表。
 */
public class MiniUser implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;
    /** openId */
    private String openId;
    /** unionId */
    private String unionId;
    /** 昵称 */
    private String nickName;
    /** 头像 */
    private String avatarUrl;
    /** 手机号 */
    private String phone;
    /** 真实姓名 */
    private String realName;
    /** 证件姓名 */
    private String idCardName;
    /** 证件号码 */
    private String idCardNumber;
    /** 人脸图片地址 */
    private String faceImageUrl;
    /** 人脸识别结果 */
    private String faceVerifyResult;
    /** 性别 */
    private Integer gender;
    /** 账号状态 */
    private Integer status;
    /** 实名认证状态 */
    private Integer realAuthStatus;
    /** 认证失败原因 */
    private String realAuthFailReason;
    /** 认证审核人 */
    private Long realAuthReviewBy;
    /** 认证审核时间 */
    private Date realAuthReviewTime;
    /** 管理员备注 */
    private String adminRemark;
    /** 当前绑定学校ID */
    private Long currentSchoolId;
    /** 当前绑定校区ID */
    private Long currentCampusId;
    /** 最近活跃时间 */
    private Date lastActiveTime;
    /** 在线状态 */
    private Integer onlineStatus;
    /** 创建时间 */
    private Date createTime;
    /** 更新时间 */
    private Date updateTime;
    /** 创建时间起（查询用） */
    private Date createTimeBegin;
    /** 创建时间止（查询用） */
    private Date createTimeEnd;

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

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
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

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MiniUser miniUser = (MiniUser) o;
        return java.util.Objects.equals(id, miniUser.id)
                && java.util.Objects.equals(openId, miniUser.openId)
                && java.util.Objects.equals(unionId, miniUser.unionId)
                && java.util.Objects.equals(nickName, miniUser.nickName)
                && java.util.Objects.equals(avatarUrl, miniUser.avatarUrl)
                && java.util.Objects.equals(phone, miniUser.phone)
                && java.util.Objects.equals(realName, miniUser.realName)
                && java.util.Objects.equals(idCardName, miniUser.idCardName)
                && java.util.Objects.equals(idCardNumber, miniUser.idCardNumber)
                && java.util.Objects.equals(faceImageUrl, miniUser.faceImageUrl)
                && java.util.Objects.equals(faceVerifyResult, miniUser.faceVerifyResult)
                && java.util.Objects.equals(gender, miniUser.gender)
                && java.util.Objects.equals(status, miniUser.status)
                && java.util.Objects.equals(realAuthStatus, miniUser.realAuthStatus)
                && java.util.Objects.equals(realAuthFailReason, miniUser.realAuthFailReason)
                && java.util.Objects.equals(realAuthReviewBy, miniUser.realAuthReviewBy)
                && java.util.Objects.equals(realAuthReviewTime, miniUser.realAuthReviewTime)
                && java.util.Objects.equals(adminRemark, miniUser.adminRemark)
                && java.util.Objects.equals(currentSchoolId, miniUser.currentSchoolId)
                && java.util.Objects.equals(currentCampusId, miniUser.currentCampusId)
                && java.util.Objects.equals(lastActiveTime, miniUser.lastActiveTime)
                && java.util.Objects.equals(onlineStatus, miniUser.onlineStatus)
                && java.util.Objects.equals(createTime, miniUser.createTime)
                && java.util.Objects.equals(updateTime, miniUser.updateTime);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, openId, unionId, nickName, avatarUrl, phone, realName, idCardName, idCardNumber,
                faceImageUrl, faceVerifyResult, gender, status, realAuthStatus, realAuthFailReason, realAuthReviewBy,
                realAuthReviewTime, adminRemark, currentSchoolId, currentCampusId, lastActiveTime, onlineStatus,
                createTime, updateTime);
    }

    @Override
    public String toString() {
        return "MiniUser{" +
                "id=" + id +
                ", openId='" + openId + '\'' +
                ", unionId='" + unionId + '\'' +
                ", nickName='" + nickName + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", phone='" + phone + '\'' +
                ", realName='" + realName + '\'' +
                ", idCardName='" + idCardName + '\'' +
                ", idCardNumber='" + idCardNumber + '\'' +
                ", faceImageUrl='" + faceImageUrl + '\'' +
                ", faceVerifyResult='" + faceVerifyResult + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                ", realAuthStatus=" + realAuthStatus +
                ", realAuthFailReason='" + realAuthFailReason + '\'' +
                ", realAuthReviewBy=" + realAuthReviewBy +
                ", realAuthReviewTime=" + realAuthReviewTime +
                ", adminRemark='" + adminRemark + '\'' +
                ", lastActiveTime=" + lastActiveTime +
                ", onlineStatus=" + onlineStatus +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}
