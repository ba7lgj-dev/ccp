package org.ba7lgj.ccp.miniprogram.vo;

public class MpLoginVO {
    private String token;
    private Long userId;
    private Long selectedSchoolId;
    private Long selectedCampusId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getSelectedSchoolId() {
        return selectedSchoolId;
    }

    public void setSelectedSchoolId(Long selectedSchoolId) {
        this.selectedSchoolId = selectedSchoolId;
    }

    public Long getSelectedCampusId() {
        return selectedCampusId;
    }

    public void setSelectedCampusId(Long selectedCampusId) {
        this.selectedCampusId = selectedCampusId;
    }
}
