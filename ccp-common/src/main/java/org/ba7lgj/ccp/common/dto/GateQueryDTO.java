package org.ba7lgj.ccp.common.dto;

/**
 * 校门查询 DTO。
 */
public class GateQueryDTO {
    private Long campusId;
    private String gateName;
    private Integer status;

    public Long getCampusId() {
        return campusId;
    }

    public void setCampusId(Long campusId) {
        this.campusId = campusId;
    }

    public String getGateName() {
        return gateName;
    }

    public void setGateName(String gateName) {
        this.gateName = gateName;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
