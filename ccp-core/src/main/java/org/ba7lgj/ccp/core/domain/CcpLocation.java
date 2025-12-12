package org.ba7lgj.ccp.core.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import java.math.BigDecimal;

/**
 * 校外地点管理实体，对应表 ccp_location。
 */
public class CcpLocation extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 所属校区ID */
    @Excel(name = "所属校区")
    private Long campusId;

    /** 地点名称 */
    @Excel(name = "地点名称")
    private String locationName;

    /** 地点类型 1地铁站 2商超 3公交站 4公寓 5其他 */
    @Excel(name = "地点类型", readConverterExp = "1=地铁站,2=商超,3=公交站,4=公寓,5=其他")
    private Integer locationType;

    /** 纬度 */
    @Excel(name = "纬度")
    private BigDecimal latitude;

    /** 经度 */
    @Excel(name = "经度")
    private BigDecimal longitude;

    /** 封面图片URL */
    @Excel(name = "封面图片")
    private String coverImageUrl;

    /** 富文本描述 */
    private String descriptionText;

    /** 状态 1正常 0停用 */
    @Excel(name = "状态", readConverterExp = "1=正常,0=停用")
    private Integer status;

    /** 所属学校ID（冗余方便查询展示） */
    private Long schoolId;

    /** 学校名称 */
    private String schoolName;

    /** 校区名称 */
    private String campusName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCampusId() {
        return campusId;
    }

    public void setCampusId(Long campusId) {
        this.campusId = campusId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public Integer getLocationType() {
        return locationType;
    }

    public void setLocationType(Integer locationType) {
        this.locationType = locationType;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

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

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }
}
