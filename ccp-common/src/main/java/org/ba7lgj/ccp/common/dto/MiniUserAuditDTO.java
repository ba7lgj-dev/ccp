package org.ba7lgj.ccp.common.dto;

import java.io.Serializable;

/**
 * 小程序用户审核 DTO。
 */
public class MiniUserAuditDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer targetRealAuthStatus;
    private String realAuthFailReason;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getTargetRealAuthStatus() {
        return targetRealAuthStatus;
    }

    public void setTargetRealAuthStatus(Integer targetRealAuthStatus) {
        this.targetRealAuthStatus = targetRealAuthStatus;
    }

    public String getRealAuthFailReason() {
        return realAuthFailReason;
    }

    public void setRealAuthFailReason(String realAuthFailReason) {
        this.realAuthFailReason = realAuthFailReason;
    }
}
