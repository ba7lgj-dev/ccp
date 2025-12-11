package org.ba7lgj.ccp.miniprogram.vo;

import java.util.ArrayList;
import java.util.List;

public class MpUserApprovedSchoolVO {
    private Long schoolId;
    private String schoolName;
    private String schoolShortName;
    private String cityName;
    private List<Campus> campusList = new ArrayList<>();

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

    public List<Campus> getCampusList() {
        return campusList;
    }

    public void setCampusList(List<Campus> campusList) {
        this.campusList = campusList;
    }

    public static class Campus {
        private Long campusId;
        private String campusName;

        public Campus() {
        }

        public Campus(Long campusId, String campusName) {
            this.campusId = campusId;
            this.campusName = campusName;
        }

        public Long getCampusId() {
            return campusId;
        }

        public void setCampusId(Long campusId) {
            this.campusId = campusId;
        }

        public String getCampusName() {
            return campusName;
        }

        public void setCampusName(String campusName) {
            this.campusName = campusName;
        }
    }
}
