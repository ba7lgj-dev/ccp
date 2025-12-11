package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Resource;
import org.ba7lgj.ccp.miniprogram.domain.MpCampus;
import org.ba7lgj.ccp.miniprogram.domain.MpSchool;
import org.ba7lgj.ccp.miniprogram.domain.MpUserSchoolAuth;
import org.ba7lgj.ccp.miniprogram.dto.MpUserSchoolAuthApplyDTO;
import org.ba7lgj.ccp.miniprogram.mapper.MpCampusMapper;
import org.ba7lgj.ccp.miniprogram.mapper.MpSchoolMapper;
import org.ba7lgj.ccp.miniprogram.mapper.MpUserSchoolAuthMapper;
import org.ba7lgj.ccp.miniprogram.service.MpUserSchoolAuthService;
import org.ba7lgj.ccp.miniprogram.vo.MpApprovedSchoolVO;
import org.ba7lgj.ccp.miniprogram.vo.MpCampusVO;
import org.ba7lgj.ccp.miniprogram.vo.MpUserSchoolAuthVO;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class MpUserSchoolAuthServiceImpl implements MpUserSchoolAuthService {

    @Resource
    private MpUserSchoolAuthMapper userSchoolAuthMapper;

    @Resource
    private MpSchoolMapper schoolMapper;

    @Resource
    private MpCampusMapper campusMapper;

    @Override
    public List<MpUserSchoolAuthVO> listMine(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        List<MpUserSchoolAuth> list = userSchoolAuthMapper.selectByUserId(userId);
        List<MpUserSchoolAuthVO> result = new ArrayList<>();
        for (MpUserSchoolAuth auth : list) {
            result.add(convert(auth));
        }
        return result;
    }

    @Override
    public MpUserSchoolAuthVO getDetail(Long userId, Long schoolId) {
        if (userId == null || schoolId == null) {
            return null;
        }
        MpUserSchoolAuth auth = userSchoolAuthMapper.selectByUserAndSchool(userId, schoolId);
        return convert(auth);
    }

    @Override
    public void apply(Long userId, MpUserSchoolAuthApplyDTO dto) {
        if (userId == null) {
            throw new IllegalStateException("未登录或token无效");
        }
        if (dto == null || dto.getSchoolId() == null) {
            throw new IllegalArgumentException("学校不能为空");
        }
        MpSchool school = schoolMapper.selectById(dto.getSchoolId());
        if (school == null || !Objects.equals(school.getStatus(), 1)) {
            throw new IllegalArgumentException("学校不存在或已停用");
        }
        MpCampus campus = null;
        if (dto.getCampusId() != null) {
            campus = campusMapper.selectCampusById(dto.getCampusId());
            if (campus == null || !Objects.equals(campus.getSchoolId(), dto.getSchoolId())) {
                throw new IllegalArgumentException("校区无效");
            }
            if (!Objects.equals(campus.getStatus(), 1)) {
                throw new IllegalArgumentException("校区不可用");
            }
        }
        String studentNo = dto.getStudentNo();
        if (!StringUtils.hasText(studentNo) || studentNo.trim().length() > 50) {
            throw new IllegalArgumentException("学号不能为空且长度不能超过50");
        }
        String studentName = dto.getStudentName();
        if (!StringUtils.hasText(studentName) || studentName.trim().length() > 50) {
            throw new IllegalArgumentException("姓名不能为空且长度不能超过50");
        }
        if (!StringUtils.hasText(dto.getStudentCardImageUrl())) {
            throw new IllegalArgumentException("请上传学生证照片");
        }
        MpUserSchoolAuth exist = userSchoolAuthMapper.selectByUserAndSchool(userId, dto.getSchoolId());
        Date now = new Date();
        if (exist == null) {
            MpUserSchoolAuth entity = new MpUserSchoolAuth();
            entity.setUserId(userId);
            entity.setSchoolId(dto.getSchoolId());
            entity.setCampusId(dto.getCampusId());
            entity.setStudentNo(studentNo.trim());
            entity.setStudentName(studentName.trim());
            entity.setStudentCardImageUrl(dto.getStudentCardImageUrl());
            entity.setExtraImageUrl(dto.getExtraImageUrl());
            entity.setStatus(1);
            entity.setFailReason(null);
            entity.setSubmitTime(now);
            entity.setReviewTime(null);
            entity.setReviewBy(null);
            userSchoolAuthMapper.insert(entity);
            return;
        }
        Integer status = exist.getStatus();
        if (status != null && status == 1) {
            throw new IllegalStateException("正在审核中，无法重复提交");
        }
        if (status != null && status == 2) {
            throw new IllegalStateException("已通过认证，如需变更请联系客服或后台管理员");
        }
        exist.setCampusId(dto.getCampusId());
        exist.setStudentNo(studentNo.trim());
        exist.setStudentName(studentName.trim());
        exist.setStudentCardImageUrl(dto.getStudentCardImageUrl());
        exist.setExtraImageUrl(dto.getExtraImageUrl());
        exist.setStatus(1);
        exist.setFailReason(null);
        exist.setSubmitTime(now);
        exist.setReviewBy(null);
        exist.setReviewTime(null);
        userSchoolAuthMapper.update(exist);
    }

    @Override
    public List<MpApprovedSchoolVO> listApproved(Long userId) {
        if (userId == null) {
            return Collections.emptyList();
        }
        List<MpUserSchoolAuth> approvedList = userSchoolAuthMapper.selectApprovedByUser(userId);
        if (CollectionUtils.isEmpty(approvedList)) {
            return Collections.emptyList();
        }
        Map<Long, MpApprovedSchoolVO> map = new LinkedHashMap<>();
        for (MpUserSchoolAuth auth : approvedList) {
            MpApprovedSchoolVO vo = map.computeIfAbsent(auth.getSchoolId(), key -> {
                MpApprovedSchoolVO item = new MpApprovedSchoolVO();
                item.setSchoolId(auth.getSchoolId());
                item.setSchoolName(auth.getSchoolName());
                item.setSchoolShortName(auth.getSchoolShortName());
                item.setCityName(auth.getCityName());
                return item;
            });
            if (CollectionUtils.isEmpty(vo.getCampusList())) {
                vo.setCampusList(buildCampusList(auth.getSchoolId()));
            }
        }
        return new ArrayList<>(map.values());
    }

    @Override
    public void ensureSchoolApproved(Long userId, Long schoolId) {
        if (userId == null) {
            throw new IllegalStateException("未登录或token无效");
        }
        if (schoolId == null) {
            throw new IllegalArgumentException("学校信息缺失");
        }
        MpUserSchoolAuth auth = userSchoolAuthMapper.selectByUserAndSchool(userId, schoolId);
        if (auth == null || auth.getStatus() == null || auth.getStatus() != 2) {
            throw new IllegalStateException("请先完成该学校的学生认证");
        }
    }

    private MpUserSchoolAuthVO convert(MpUserSchoolAuth auth) {
        if (auth == null) {
            return null;
        }
        MpUserSchoolAuthVO vo = new MpUserSchoolAuthVO();
        vo.setId(auth.getId());
        vo.setSchoolId(auth.getSchoolId());
        vo.setSchoolName(auth.getSchoolName());
        vo.setSchoolShortName(auth.getSchoolShortName());
        vo.setCityName(auth.getCityName());
        vo.setCampusId(auth.getCampusId());
        vo.setCampusName(auth.getCampusName());
        vo.setStudentNo(auth.getStudentNo());
        vo.setStudentName(auth.getStudentName());
        vo.setStatus(auth.getStatus());
        vo.setFailReason(auth.getFailReason());
        vo.setSubmitTime(auth.getSubmitTime());
        vo.setReviewTime(auth.getReviewTime());
        vo.setStudentCardImageUrl(auth.getStudentCardImageUrl());
        vo.setExtraImageUrl(auth.getExtraImageUrl());
        return vo;
    }

    private List<MpCampusVO> buildCampusList(Long schoolId) {
        List<MpCampus> campusList = campusMapper.selectEnabledCampusBySchool(schoolId);
        List<MpCampusVO> result = new ArrayList<>();
        if (campusList == null) {
            return result;
        }
        for (MpCampus campus : campusList) {
            MpCampusVO vo = new MpCampusVO();
            vo.setId(campus.getId());
            vo.setCampusName(campus.getCampusName());
            vo.setAddress(campus.getAddress());
            result.add(vo);
        }
        return result;
    }
}
