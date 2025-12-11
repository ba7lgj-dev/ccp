package org.ba7lgj.ccp.core.domain.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 黑名单状态信息。
 */
public class MiniUserBlacklistVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Integer status;
    private Date expireTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }
}
