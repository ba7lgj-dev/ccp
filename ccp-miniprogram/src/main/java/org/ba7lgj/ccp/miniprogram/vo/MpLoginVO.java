package org.ba7lgj.ccp.miniprogram.vo;

import org.ba7lgj.ccp.common.domain.MiniUser;

public class MpLoginVO {
    private String token;
    private MiniUser userInfo;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public MiniUser getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(MiniUser userInfo) {
        this.userInfo = userInfo;
    }
}
