package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.enums.RealAuthStatusEnum;
import org.ba7lgj.ccp.core.service.CoreMiniUserService;
import org.ba7lgj.ccp.miniprogram.domain.MpCampus;
import org.ba7lgj.ccp.miniprogram.domain.MpSchool;
import org.ba7lgj.ccp.miniprogram.domain.MpUserSchoolAuth;
import org.ba7lgj.ccp.miniprogram.dto.MpUserSchoolAuthApplyDTO;
import org.ba7lgj.ccp.miniprogram.mapper.MpCampusMapper;
import org.ba7lgj.ccp.miniprogram.mapper.MpSchoolMapper;
import org.ba7lgj.ccp.miniprogram.mapper.MpUserSchoolAuthMapper;
import org.ba7lgj.ccp.miniprogram.service.MpUserSchoolAuthService;
import org.ba7lgj.ccp.miniprogram.vo.MpUserApprovedSchoolVO;
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

    @Resource
    private CoreMiniUserService coreMiniUserService;

    @Override
    public List<MpUserSchoolAuthVO> listMyAuth(Long userId) {
        return userSchoolAuthMapper.selectDetailListByUser(userId);
    }

    @Override
    public MpUserSchoolAuthVO getDetail(Long userId, Long schoolId) {
        return userSchoolAuthMapper.selectDetailByUserAndSchool(userId, schoolId);
    }

    @Override
    public void apply(Long userId, MpUserSchoolAuthApplyDTO dto) {
        if (dto == null || dto.getSchoolId() == null) {
            throw new IllegalArgumentException("学校不能为空");
        }
        if (!StringUtils.hasText(dto.getStudentNo()) || dto.getStudentNo().length() < 5
                || dto.getStudentNo().length() > 50) {
            throw new IllegalArgumentException("学号长度需在5-50之间");
        }
        if (!StringUtils.hasText(dto.getStudentName()) || dto.getStudentName().length() < 2
                || dto.getStudentName().length() > 50) {
            throw new IllegalArgumentException("姓名长度需在2-50之间");
        }
        if (!StringUtils.hasText(dto.getStudentCardImageUrl())) {
            throw new IllegalArgumentException("请上传学生证照片");
        }
        MpSchool school = schoolMapper.selectById(dto.getSchoolId());
        if (school == null) {
            throw new IllegalArgumentException("学校不存在");
        }
        MpCampus campus = null;
        if (dto.getCampusId() != null) {
            campus = campusMapper.selectCampusById(dto.getCampusId());
            if (campus == null || !dto.getSchoolId().equals(campus.getSchoolId())) {
                throw new IllegalArgumentException("校区信息不正确");
            }
        }
        MiniUser user = coreMiniUserService.selectMiniUserById(userId);
        if (user == null || !RealAuthStatusEnum.APPROVED.getCode().equals(user.getRealAuthStatus())) {
            throw new IllegalStateException("请先完成实名认证");
        }
        MpUserSchoolAuth existing = userSchoolAuthMapper.selectByUserAndSchool(userId, dto.getSchoolId());
        Date now = new Date();
        if (existing == null) {
            MpUserSchoolAuth auth = new MpUserSchoolAuth();
            auth.setUserId(userId);
            auth.setSchoolId(dto.getSchoolId());
            auth.setCampusId(dto.getCampusId());
            auth.setStudentNo(dto.getStudentNo());
            auth.setStudentName(dto.getStudentName());
            auth.setStudentCardImageUrl(dto.getStudentCardImageUrl());
            auth.setExtraImageUrl(dto.getExtraImageUrl());
            auth.setStatus(1);
            auth.setSubmitTime(now);
            auth.setCreateTime(now);
            auth.setUpdateTime(now);
            userSchoolAuthMapper.insertAuth(auth);
            return;
        }
        if (existing.getStatus() != null && existing.getStatus() == 1) {
            throw new IllegalStateException("审核中无法重复提交");
        }
        if (existing.getStatus() != null && existing.getStatus() == 2) {
            throw new IllegalStateException("已通过认证，无需重复提交");
        }
        existing.setCampusId(dto.getCampusId());
        existing.setStudentNo(dto.getStudentNo());
        existing.setStudentName(dto.getStudentName());
        existing.setStudentCardImageUrl(dto.getStudentCardImageUrl());
        existing.setExtraImageUrl(dto.getExtraImageUrl());
        existing.setStatus(1);
        existing.setFailReason(null);
        existing.setReviewBy(null);
        existing.setReviewTime(null);
        existing.setSubmitTime(now);
        existing.setUpdateTime(now);
        userSchoolAuthMapper.updateAuth(existing);
    }

    @Override
    public List<MpUserApprovedSchoolVO> listApproved(Long userId) {
        List<MpUserSchoolAuth> authList = userSchoolAuthMapper.selectApprovedByUser(userId);
        if (CollectionUtils.isEmpty(authList)) {
            return List.of();
        }
        Map<Long, List<MpUserSchoolAuth>> grouped = authList.stream()
                .collect(Collectors.groupingBy(MpUserSchoolAuth::getSchoolId));
        List<MpUserApprovedSchoolVO> result = new ArrayList<>();
        Map<Long, MpSchool> schoolCache = new HashMap<>();
        for (Long schoolId : grouped.keySet()) {
            MpSchool school = schoolCache.computeIfAbsent(schoolId, schoolMapper::selectById);
            if (school == null) {
                continue;
            }
            MpUserApprovedSchoolVO vo = new MpUserApprovedSchoolVO();
            vo.setSchoolId(school.getId());
            vo.setSchoolName(school.getSchoolName());
            vo.setSchoolShortName(school.getSchoolShortName());
            vo.setCityName(null);
            List<MpCampus> campusList = campusMapper.selectEnabledCampusBySchool(schoolId);
            if (!CollectionUtils.isEmpty(campusList)) {
                vo.setCampusList(campusList.stream()
                        .map(c -> new MpUserApprovedSchoolVO.Campus(c.getId(), c.getCampusName()))
                        .collect(Collectors.toList()));
            }
            result.add(vo);
        }
        return result;
    }
}
