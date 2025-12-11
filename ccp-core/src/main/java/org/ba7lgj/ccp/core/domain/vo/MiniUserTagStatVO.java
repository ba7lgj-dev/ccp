package org.ba7lgj.ccp.core.domain.vo;

import java.io.Serializable;

/**
 * 用户标签统计。
 */
public class MiniUserTagStatVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tag;
    private Integer totalCount;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
}
