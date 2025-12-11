package org.ba7lgj.ccp.core.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 实名认证列表视图对象。
 */
public class CcpUserRealAuthListVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String nickName;

    private String avatarUrl;

    private String phone;

    private String realName;

    private String idCardNumber;

    private String idCardMasked;

    private Integer realAuthStatus;

    private String realAuthFailReason;

    private String realAuthReviewByName;

    private Date realAuthReviewTime;

    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getIdCardNumber() {
        return idCardNumber;
    }

    public void setIdCardNumber(String idCardNumber) {
        this.idCardNumber = idCardNumber;
    }

    public String getIdCardMasked() {
        return idCardMasked;
    }

    public void setIdCardMasked(String idCardMasked) {
        this.idCardMasked = idCardMasked;
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

    public String getRealAuthReviewByName() {
        return realAuthReviewByName;
    }

    public void setRealAuthReviewByName(String realAuthReviewByName) {
        this.realAuthReviewByName = realAuthReviewByName;
    }

    public Date getRealAuthReviewTime() {
        return realAuthReviewTime;
    }

    public void setRealAuthReviewTime(Date realAuthReviewTime) {
        this.realAuthReviewTime = realAuthReviewTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
