package org.ba7lgj.ccp.miniprogram.vo;

import java.util.ArrayList;
import java.util.List;

public class MpApprovedSchoolVO {
    private Long schoolId;
    private String schoolName;
    private String schoolShortName;
    private String cityName;
    private List<MpCampusVO> campusList = new ArrayList<>();

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getSchoolShortName() {
        return schoolShortName;
    }

    public void setSchoolShortName(String schoolShortName) {
        this.schoolShortName = schoolShortName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public List<MpCampusVO> getCampusList() {
        return campusList;
    }

    public void setCampusList(List<MpCampusVO> campusList) {
        this.campusList = campusList;
    }
}
