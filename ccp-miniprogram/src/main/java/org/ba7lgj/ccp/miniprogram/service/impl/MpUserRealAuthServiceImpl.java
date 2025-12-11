package org.ba7lgj.ccp.miniprogram.service.impl;

import com.ruoyi.framework.config.ServerConfig;
import java.util.Objects;
import javax.annotation.Resource;
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

    @Resource
    private CoreMiniUserService coreMiniUserService;

    private final ServerConfig serverConfig;

    public MpUserRealAuthServiceImpl(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    @Override
    public MpUserRealAuthInfoVO getCurrentUserRealAuthInfo(Long userId) {
        if (userId == null) {
            return null;
        }
        MiniUser user = coreMiniUserService.selectMiniUserById(userId);
        if (user == null) {
            return null;
        }
        MpUserRealAuthInfoVO vo = new MpUserRealAuthInfoVO();
        vo.setRealName(user.getRealName());
        vo.setIdCardMasked(maskIdCard(user.getIdCardNumber()));
        vo.setFaceImageUrl(buildFullUrl(user.getFaceImageUrl()));
        vo.setRealAuthStatus(user.getRealAuthStatus() == null ? RealAuthStatusEnum.NOT_AUTH.getCode() : user.getRealAuthStatus());
        vo.setRealAuthFailReason(StringUtils.hasText(user.getRealAuthFailReason()) ? user.getRealAuthFailReason() : "");
        return vo;
    }

    @Override
    public void applyRealAuth(Long userId, MpUserRealAuthApplyDTO dto) {
        if (userId == null) {
            throw new IllegalStateException("未登录或token无效");
        }
        MiniUser current = coreMiniUserService.selectMiniUserById(userId);
        if (current == null) {
            throw new IllegalStateException("用户不存在");
        }
        RealAuthStatusEnum status = RealAuthStatusEnum.of(current.getRealAuthStatus());
        if (status == RealAuthStatusEnum.PENDING) {
            throw new IllegalStateException("实名信息审核中，请勿重复提交");
        }
        if (status == RealAuthStatusEnum.APPROVED) {
            throw new IllegalStateException("已通过实名认证，如需修改请联系管理员");
        }
        String realName = dto == null ? null : dto.getRealName();
        String idCardNumber = dto == null ? null : dto.getIdCardNumber();
        String faceImageUrl = dto == null ? null : dto.getFaceImageUrl();
        if (!StringUtils.hasText(realName) || realName.trim().length() < 2 || realName.trim().length() > 30) {
            throw new IllegalArgumentException("真实姓名长度需在2到30个字符之间");
        }
        String trimmedIdCard = idCardNumber == null ? null : idCardNumber.trim();
        if (!StringUtils.hasText(trimmedIdCard) || !isValidIdCardLength(trimmedIdCard)) {
            throw new IllegalArgumentException("身份证号格式不正确");
        }
        MiniUser patch = new MiniUser();
        patch.setId(userId);
        patch.setRealName(realName.trim());
        patch.setIdCardNumber(trimmedIdCard);
        patch.setFaceImageUrl(StringUtils.hasText(faceImageUrl) ? faceImageUrl.trim() : null);
        patch.setRealAuthStatus(RealAuthStatusEnum.PENDING.getCode());
        patch.setRealAuthFailReason(null);
        patch.setRealAuthReviewBy(null);
        patch.setRealAuthReviewTime(null);
        coreMiniUserService.updateMiniUser(patch);
    }

    @Override
    public void ensureApproved(Long userId) {
        if (userId == null) {
            throw new IllegalStateException("未登录或token无效");
        }
        MiniUser user = coreMiniUserService.selectMiniUserById(userId);
        if (user == null) {
            throw new IllegalStateException("未登录或token无效");
        }
        RealAuthStatusEnum status = RealAuthStatusEnum.of(user.getRealAuthStatus());
        if (!Objects.equals(status, RealAuthStatusEnum.APPROVED)) {
            throw new IllegalStateException("请先完成实名认证后再使用拼车");
        }
    }

    private String maskIdCard(String idCardNumber) {
        if (!StringUtils.hasText(idCardNumber)) {
            return null;
        }
        String trimmed = idCardNumber.trim();
        int len = trimmed.length();
        if (len <= 4) {
            return "**" + trimmed.charAt(len - 1);
        }
        int prefix = Math.min(3, len - 2);
        int suffix = Math.min(4, len - prefix);
        String start = trimmed.substring(0, prefix);
        String end = trimmed.substring(len - suffix);
        StringBuilder sb = new StringBuilder();
        sb.append(start);
        for (int i = 0; i < len - prefix - suffix; i++) {
            sb.append('*');
        }
        sb.append(end);
        return sb.toString();
    }

    private boolean isValidIdCardLength(String idCardNumber) {
        int len = idCardNumber.length();
        return len == 15 || len == 18;
    }

    private String buildFullUrl(String path) {
        if (!StringUtils.hasText(path)) {
            return path;
        }
        if (path.startsWith("http://") || path.startsWith("https://")) {
            return path;
        }
        String domain = serverConfig.getUrl();
        if (!StringUtils.hasText(domain)) {
            return path;
        }
        if (!path.startsWith("/")) {
            return domain + "/" + path;
        }
        return domain + path;
    }
}
