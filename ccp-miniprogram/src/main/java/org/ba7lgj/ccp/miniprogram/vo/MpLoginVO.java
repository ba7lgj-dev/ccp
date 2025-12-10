package org.ba7lgj.ccp.miniprogram.vo;

public class MpLoginVO {
    private String token;
    private org.ba7lgj.ccp.miniprogram.domain.MpUser user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public org.ba7lgj.ccp.miniprogram.domain.MpUser getUser() {
        return user;
    }

    public void setUser(org.ba7lgj.ccp.miniprogram.domain.MpUser user) {
        this.user = user;
    }
}
