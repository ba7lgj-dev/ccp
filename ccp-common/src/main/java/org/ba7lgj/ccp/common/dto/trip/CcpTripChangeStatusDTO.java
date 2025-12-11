package org.ba7lgj.ccp.common.dto.trip;

import javax.validation.constraints.NotNull;

/**
 * 管理端修改拼车状态请求。
 */
public class CcpTripChangeStatusDTO {

    @NotNull(message = "行程ID不能为空")
    private Long tripId;

    @NotNull(message = "目标状态不能为空")
    private Integer newStatus;

    private String remark;

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }

    public Integer getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(Integer newStatus) {
        this.newStatus = newStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
