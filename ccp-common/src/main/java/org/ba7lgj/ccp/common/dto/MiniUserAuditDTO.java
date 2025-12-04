package org.ba7lgj.ccp.common.dto;

import java.io.Serializable;

/**
 * 小程序用户审核 DTO。
 */
public class MiniUserAuditDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer targetAuthStatus;
    private String authFailReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTargetAuthStatus() {
        return targetAuthStatus;
    }

    public void setTargetAuthStatus(Integer targetAuthStatus) {
        this.targetAuthStatus = targetAuthStatus;
    }

    public String getAuthFailReason() {
        return authFailReason;
    }

    public void setAuthFailReason(String authFailReason) {
        this.authFailReason = authFailReason;
    }
}
