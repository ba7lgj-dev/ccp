package org.ba7lgj.ccp.core.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 学校学生认证记录。
 */
public class CcpUserSchoolAuth extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 小程序用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 学校ID */
    @Excel(name = "学校ID")
    private Long schoolId;

    /** 校区ID */
    @Excel(name = "校区ID")
    private Long campusId;

    /** 学号 */
    @Excel(name = "学号")
    private String studentNo;

    /** 学生姓名 */
    @Excel(name = "学生姓名")
    private String studentName;

    /** 学生证图片 */
    @Excel(name = "学生证图片")
    private String studentCardImageUrl;

    /** 附加图片 */
    @Excel(name = "附加图片")
    private String extraImageUrl;

    /** 状态：1待审核，2已通过，3不通过，4已过期 */
    @Excel(name = "状态", readConverterExp = "1=待审核,2=已通过,3=不通过,4=已过期")
    private Integer status;

    /** 审核失败原因 */
    @Excel(name = "拒绝原因")
    private String failReason;

    /** 提交时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date submitTime;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reviewTime;

    /** 审核人（后台用户ID） */
    @Excel(name = "审核人ID")
    private Long reviewBy;

    /** 过期时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "过期时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    /** 查询辅助：提交开始时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date beginSubmitTime;

    /** 查询辅助：提交结束时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endSubmitTime;

    /** 数据权限过滤：校区管理员ID */
    private Long managerUserId;

    /** 学校名称 */
    private String schoolName;

    /** 学校简称 */
    private String schoolShortName;

    /** 校区名称 */
    private String campusName;

    /** 城市名称 */
    private String cityName;

    /** 用户昵称 */
    private String userNickName;

    /** 用户手机号 */
    private String userPhone;

    /** 用户OpenId */
    private String userOpenId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getFailReason() {
        return failReason;
    }

    public void setFailReason(String failReason) {
        this.failReason = failReason;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Date getReviewTime() {
        return reviewTime;
    }

    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }

    public Long getReviewBy() {
        return reviewBy;
    }

    public void setReviewBy(Long reviewBy) {
        this.reviewBy = reviewBy;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Date getBeginSubmitTime() {
        return beginSubmitTime;
    }

    public void setBeginSubmitTime(Date beginSubmitTime) {
        this.beginSubmitTime = beginSubmitTime;
    }

    public Date getEndSubmitTime() {
        return endSubmitTime;
    }

    public void setEndSubmitTime(Date endSubmitTime) {
        this.endSubmitTime = endSubmitTime;
    }

    public Long getManagerUserId() {
        return managerUserId;
    }

    public void setManagerUserId(Long managerUserId) {
        this.managerUserId = managerUserId;
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

    public String getCampusName() {
        return campusName;
    }

    public void setCampusName(String campusName) {
        this.campusName = campusName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }
}
