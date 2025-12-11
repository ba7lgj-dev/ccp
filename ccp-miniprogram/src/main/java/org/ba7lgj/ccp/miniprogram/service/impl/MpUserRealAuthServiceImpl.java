package org.ba7lgj.ccp.miniprogram.service.impl;

import java.util.Date;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.enums.RealAuthStatusEnum;
import org.ba7lgj.ccp.core.service.CoreMiniUserService;
import org.ba7lgj.ccp.miniprogram.dto.MpUserRealAuthApplyDTO;
import org.ba7lgj.ccp.miniprogram.service.MpUserRealAuthService;
import org.ba7lgj.ccp.miniprogram.vo.MpUserRealAuthInfoVO;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class MpUserRealAuthServiceImpl implements MpUserRealAuthService {

    private final CoreMiniUserService coreMiniUserService;

    public MpUserRealAuthServiceImpl(CoreMiniUserService coreMiniUserService) {
        this.coreMiniUserService = coreMiniUserService;
    }

    @Override
    public MpUserRealAuthInfoVO getRealAuthInfo(Long userId) {
        MiniUser user = coreMiniUserService.selectMiniUserById(userId);
        if (user == null) {
            return null;
        }
        MpUserRealAuthInfoVO vo = new MpUserRealAuthInfoVO();
        vo.setUserId(user.getId());
        vo.setNickName(user.getNickName());
        vo.setAvatarUrl(user.getAvatarUrl());
        vo.setPhone(user.getPhone());
        vo.setRealAuthStatus(user.getRealAuthStatus());
        vo.setRealName(user.getRealName());
        vo.setIdCardNumberMasked(maskIdCard(user.getIdCardNumber()));
        vo.setFaceImageUrl(user.getFaceImageUrl());
        vo.setRealAuthFailReason(user.getRealAuthFailReason());
        return vo;
    }

    @Override
    public void applyRealAuth(Long userId, MpUserRealAuthApplyDTO dto) {
        if (dto == null || !StringUtils.hasText(dto.getRealName()) || !StringUtils.hasText(dto.getIdCardNumber())
                || !StringUtils.hasText(dto.getFaceImageUrl())) {
            throw new IllegalArgumentException("实名信息不完整");
        }
        String realName = dto.getRealName().trim();
        String idCardNumber = dto.getIdCardNumber().trim();
        if (realName.length() < 2 || realName.length() > 50) {
            throw new IllegalArgumentException("真实姓名长度需在2-50之间");
        }
        if (idCardNumber.length() < 15 || idCardNumber.length() > 18) {
            throw new IllegalArgumentException("身份证号码格式不正确");
        }
        MiniUser user = coreMiniUserService.selectMiniUserById(userId);
        if (user == null) {
            throw new IllegalStateException("用户不存在");
        }
        Integer status = user.getRealAuthStatus();
        if (RealAuthStatusEnum.PENDING.getCode() == status) {
            throw new IllegalStateException("审核中，请勿重复提交");
        }
        if (RealAuthStatusEnum.APPROVED.getCode() == status) {
            throw new IllegalStateException("实名认证已通过，无需重复提交");
        }
        MiniUser patch = new MiniUser();
        patch.setId(userId);
        patch.setRealName(realName);
        patch.setIdCardNumber(idCardNumber);
        patch.setFaceImageUrl(dto.getFaceImageUrl());
        patch.setRealAuthStatus(RealAuthStatusEnum.PENDING.getCode());
        patch.setRealAuthFailReason(null);
        patch.setRealAuthReviewBy(null);
        patch.setRealAuthReviewTime(null);
        patch.setUpdateTime(new Date());
        coreMiniUserService.updateMiniUser(patch);
    }

    private String maskIdCard(String idCardNumber) {
        if (!StringUtils.hasText(idCardNumber) || idCardNumber.length() < 8) {
            return null;
        }
        String trimmed = idCardNumber.trim();
        int length = trimmed.length();
        String prefix = trimmed.substring(0, 4);
        String suffix = trimmed.substring(Math.max(length - 4, 4));
        return prefix + "********" + suffix;
    }
}
