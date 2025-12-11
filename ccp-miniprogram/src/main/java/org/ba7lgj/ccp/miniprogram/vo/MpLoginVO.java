package org.ba7lgj.ccp.miniprogram.vo;

public class MpLoginVO {
    private String token;
    private MpLoginUserInfoVO userInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MpLoginUserInfoVO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(MpLoginUserInfoVO userInfo) {
        this.userInfo = userInfo;
    }
}
