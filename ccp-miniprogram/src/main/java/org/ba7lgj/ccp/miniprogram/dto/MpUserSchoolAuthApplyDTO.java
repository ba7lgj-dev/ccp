package org.ba7lgj.ccp.miniprogram.dto;

public class MpUserSchoolAuthApplyDTO {
    private Long schoolId;
    private Long campusId;
    private String studentNo;
    private String studentName;
    private String studentCardImageUrl;
    private String extraImageUrl;

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getCampusId() {
        return campusId;
    }

    public void setCampusId(Long campusId) {
        this.campusId = campusId;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentCardImageUrl() {
        return studentCardImageUrl;
    }

    public void setStudentCardImageUrl(String studentCardImageUrl) {
        this.studentCardImageUrl = studentCardImageUrl;
    }

    public String getExtraImageUrl() {
        return extraImageUrl;
    }

    public void setExtraImageUrl(String extraImageUrl) {
        this.extraImageUrl = extraImageUrl;
    }
}
