package org.ba7lgj.ccp.miniprogram.vo;

public class MpUserRealAuthInfoVO {
    private Long userId;
    private String nickName;
    private String avatarUrl;
    private String phone;
    private String realName;
    private String idCardMasked;
    private String faceImageUrl;
    private Integer realAuthStatus;
    private String realAuthFailReason;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public String getIdCardMasked() {
        return idCardMasked;
    }

    public void setIdCardMasked(String idCardMasked) {
        this.idCardMasked = idCardMasked;
    }

    public String getFaceImageUrl() {
        return faceImageUrl;
    }

    public void setFaceImageUrl(String faceImageUrl) {
        this.faceImageUrl = faceImageUrl;
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
}
