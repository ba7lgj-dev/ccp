package org.ba7lgj.ccp.core.service.impl;

import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import org.ba7lgj.ccp.core.domain.CcpUserSchoolAuth;
import org.ba7lgj.ccp.core.mapper.CcpUserSchoolAuthMapper;
import org.ba7lgj.ccp.core.service.ICcpUserSchoolAuthService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 学校学生认证服务实现。
 */
@Service
public class CcpUserSchoolAuthServiceImpl implements ICcpUserSchoolAuthService {

    @Resource
    private CcpUserSchoolAuthMapper ccpUserSchoolAuthMapper;

    @Override
    public List<CcpUserSchoolAuth> selectCcpUserSchoolAuthList(CcpUserSchoolAuth query, Long managerUserId, boolean isAdmin) {
        if (!isAdmin) {
            query.setManagerUserId(managerUserId);
        } else {
            query.setManagerUserId(null);
        }
        return ccpUserSchoolAuthMapper.selectCcpUserSchoolAuthList(query);
    }

    @Override
    public CcpUserSchoolAuth selectCcpUserSchoolAuthById(Long id, Long managerUserId, boolean isAdmin) {
        Long filterManagerId = isAdmin ? null : managerUserId;
        return ccpUserSchoolAuthMapper.selectCcpUserSchoolAuthById(id, filterManagerId);
    }

    @Override
    public int approve(Long id, Long reviewUserId) {
        CcpUserSchoolAuth auth = loadWithPermission(id, reviewUserId);
        if (auth.getStatus() == null || (auth.getStatus() != 1 && auth.getStatus() != 3)) {
            throw new ServiceException("状态不允许审核通过");
        }
        Date now = new Date();
        auth.setStatus(2);
        auth.setFailReason(null);
        auth.setReviewBy(reviewUserId);
        auth.setReviewTime(now);
        auth.setUpdateBy(SecurityUtils.getUsername());
        auth.setUpdateTime(now);
        return ccpUserSchoolAuthMapper.updateCcpUserSchoolAuth(auth);
    }

    @Override
    public int reject(Long id, String failReason, Long reviewUserId) {
        if (!StringUtils.hasText(failReason)) {
            throw new ServiceException("拒绝原因不能为空");
        }
        CcpUserSchoolAuth auth = loadWithPermission(id, reviewUserId);
        if (auth.getStatus() == null || (auth.getStatus() != 1 && auth.getStatus() != 3)) {
            throw new ServiceException("状态不允许审核拒绝");
        }
        Date now = new Date();
        auth.setStatus(3);
        auth.setFailReason(failReason);
        auth.setReviewBy(reviewUserId);
        auth.setReviewTime(now);
        auth.setUpdateBy(SecurityUtils.getUsername());
        auth.setUpdateTime(now);
        return ccpUserSchoolAuthMapper.updateCcpUserSchoolAuth(auth);
    }

    private CcpUserSchoolAuth loadWithPermission(Long id, Long reviewUserId) {
        Long managerFilter = SecurityUtils.isAdmin(reviewUserId) ? null : reviewUserId;
        CcpUserSchoolAuth auth = ccpUserSchoolAuthMapper.selectCcpUserSchoolAuthById(id, managerFilter);
        if (auth == null) {
            throw new ServiceException("无权审核或记录不存在");
        }
        return auth;
    }
}
