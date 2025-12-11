package org.ba7lgj.ccp.common.dto.trip;

import javax.validation.constraints.NotNull;

/**
 * 标记成员爽约参数。
 */
public class CcpTripMemberNoShowDTO {

    @NotNull(message = "成员ID不能为空")
    private Long memberId;

    private String remark;

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
