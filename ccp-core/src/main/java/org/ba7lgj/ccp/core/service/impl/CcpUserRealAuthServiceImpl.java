package org.ba7lgj.ccp.core.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;
import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.dto.CcpUserRealAuthQueryDTO;
import org.ba7lgj.ccp.core.domain.vo.CcpUserRealAuthDetailVO;
import org.ba7lgj.ccp.core.domain.vo.CcpUserRealAuthListVO;
import org.ba7lgj.ccp.core.mapper.CcpMiniUserMapper;
import org.ba7lgj.ccp.core.service.CcpUserRealAuthService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class CcpUserRealAuthServiceImpl implements CcpUserRealAuthService {

    @Resource
    private CcpMiniUserMapper ccpMiniUserMapper;

    @Override
    public List<CcpUserRealAuthListVO> selectUserRealAuthList(CcpUserRealAuthQueryDTO queryDTO) {
        List<CcpUserRealAuthListVO> list = ccpMiniUserMapper.selectUserRealAuthList(queryDTO);
        if (list != null) {
            list.forEach(this::maskIdCardNumber);
        }
        return list;
    }

    @Override
    public CcpUserRealAuthDetailVO selectUserRealAuthById(Long userId) {
        return ccpMiniUserMapper.selectUserRealAuthDetail(userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int approveUserRealAuth(Long userId, Long reviewerUserId, String remark) {
        MiniUser dbUser = ccpMiniUserMapper.selectCcpMiniUserById(userId);
        if (dbUser == null) {
            throw new ServiceException("用户不存在");
        }
        if (!Integer.valueOf(1).equals(dbUser.getRealAuthStatus())) {
            throw new ServiceException("当前状态不允许审批");
        }
        MiniUser update = new MiniUser();
        update.setId(userId);
        update.setRealAuthStatus(2);
        update.setRealAuthFailReason(null);
        update.setRealAuthReviewBy(reviewerUserId);
        update.setRealAuthReviewTime(new Date());
        update.setUpdateTime(new Date());
        update.setAdminRemark(StringUtils.isNotBlank(remark) ? remark : null);
        return ccpMiniUserMapper.updateRealAuthStatus(update);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int rejectUserRealAuth(Long userId, Long reviewerUserId, String failReason, String remark) {
        MiniUser dbUser = ccpMiniUserMapper.selectCcpMiniUserById(userId);
        if (dbUser == null) {
            throw new ServiceException("用户不存在");
        }
        if (!Integer.valueOf(1).equals(dbUser.getRealAuthStatus())) {
            throw new ServiceException("当前状态不允许审批");
        }
        if (StringUtils.isBlank(failReason)) {
            throw new ServiceException("拒绝原因不能为空");
        }
        MiniUser update = new MiniUser();
        update.setId(userId);
        update.setRealAuthStatus(3);
        update.setRealAuthFailReason(failReason);
        update.setRealAuthReviewBy(reviewerUserId);
        update.setRealAuthReviewTime(new Date());
        update.setUpdateTime(new Date());
        update.setAdminRemark(StringUtils.isNotBlank(remark) ? remark : null);
        return ccpMiniUserMapper.updateRealAuthStatus(update);
    }

    private void maskIdCardNumber(CcpUserRealAuthListVO vo) {
        if (vo == null) {
            return;
        }
        String idCard = vo.getIdCardNumber();
        if (StringUtils.isBlank(idCard)) {
            vo.setIdCardMasked(null);
            vo.setIdCardNumber(null);
            return;
        }
        if (idCard.length() <= 8) {
            vo.setIdCardMasked(idCard);
            vo.setIdCardNumber(null);
            return;
        }
        String masked = idCard.substring(0, 3) + StringUtils.repeat("*", idCard.length() - 7) + idCard.substring(idCard.length() - 4);
        vo.setIdCardMasked(masked);
        vo.setIdCardNumber(null);
    }
}
