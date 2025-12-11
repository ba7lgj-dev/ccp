package org.ba7lgj.ccp.core.domain.vo;

import java.io.Serializable;

/**
 * 用户信誉统计。
 */
public class MiniUserReputationVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Double avgRating;
    private Integer totalNoShow;
    private Integer totalReviews;

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getTotalNoShow() {
        return totalNoShow;
    }

    public void setTotalNoShow(Integer totalNoShow) {
        this.totalNoShow = totalNoShow;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }
}
