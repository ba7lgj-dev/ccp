package org.ba7lgj.ccp.miniprogram.vo;

public class MpLoginUserInfoVO {
    private Long userId;
    private Long id;
    private String nickName;
    private String avatarUrl;
    private String phone;
    private Integer realAuthStatus;
    private String realName;
    private String realAuthFailReason;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public Integer getRealAuthStatus() {
        return realAuthStatus;
    }

    public void setRealAuthStatus(Integer realAuthStatus) {
        this.realAuthStatus = realAuthStatus;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRealAuthFailReason() {
        return realAuthFailReason;
    }

    public void setRealAuthFailReason(String realAuthFailReason) {
        this.realAuthFailReason = realAuthFailReason;
    }
}
