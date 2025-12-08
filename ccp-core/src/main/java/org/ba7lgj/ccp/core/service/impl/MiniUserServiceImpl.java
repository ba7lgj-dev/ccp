package org.ba7lgj.ccp.core.service.impl;

import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.dto.MiniUserQueryDTO;
import org.ba7lgj.ccp.common.enums.RealAuthStatusEnum;
import org.ba7lgj.ccp.core.mapper.MiniUserMapper;
import org.ba7lgj.ccp.core.service.IMiniUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 小程序用户业务实现。
 */
@Service
public class MiniUserServiceImpl implements IMiniUserService {

    @Resource
    private MiniUserMapper miniUserMapper;

    @Override
    public MiniUser selectMiniUserById(Long id) {
        return miniUserMapper.selectMiniUserById(id);
    }

    @Override
    public MiniUser selectMiniUserByOpenId(String openId) {
        return miniUserMapper.selectMiniUserByOpenId(openId);
    }

    @Override
    public List<MiniUser> selectMiniUserList(MiniUserQueryDTO queryDTO) {
        return miniUserMapper.selectMiniUserList(queryDTO);
    }

    @Override
    public List<MiniUser> selectMiniUserExportList(MiniUserQueryDTO queryDTO) {
        List<MiniUser> list = miniUserMapper.selectMiniUserExportList(queryDTO);
        return CollectionUtils.isEmpty(list) ? List.of() : list;
    }

    @Override
    public MiniUser autoRegisterByOpenId(String openId) {
        MiniUser miniUser = miniUserMapper.selectMiniUserByOpenId(openId);
        if (miniUser != null) {
            return miniUser;
        }
        MiniUser newUser = new MiniUser();
        newUser.setOpenId(openId);
        newUser.setStatus(1);
        newUser.setRealAuthStatus(RealAuthStatusEnum.NOT_AUTH.getCode());
        newUser.setCreateTime(new Date());
        miniUserMapper.insertMiniUser(newUser);
        return newUser;
    }

    @Override
    public int insertMiniUser(MiniUser user) {
        user.setCreateTime(new Date());
        user.setRealAuthStatus(user.getRealAuthStatus() == null ? RealAuthStatusEnum.NOT_AUTH.getCode() : user.getRealAuthStatus());
        user.setStatus(user.getStatus() == null ? 1 : user.getStatus());
        return miniUserMapper.insertMiniUser(user);
    }

    @Override
    public int updateMiniUser(MiniUser user) {
        user.setUpdateTime(new Date());
        return miniUserMapper.updateMiniUser(user);
    }

    @Override
    public int changeStatus(Long id, Integer status) {
        MiniUser user = miniUserMapper.selectMiniUserById(id);
        if (user == null) {
            return 0;
        }
        user.setStatus(status);
        user.setUpdateTime(new Date());
        return miniUserMapper.updateMiniUser(user);
    }

    @Override
    public int reviewUser(Long id, Integer targetRealAuthStatus, String failReason, Long reviewBy, Date reviewTime) {
        if (targetRealAuthStatus == null) {
            throw new IllegalArgumentException("targetRealAuthStatus must be provided");
        }
        boolean approved = RealAuthStatusEnum.APPROVED.getCode() == targetRealAuthStatus;
        boolean rejected = RealAuthStatusEnum.REJECTED.getCode() == targetRealAuthStatus;
        if (!approved && !rejected) {
            throw new IllegalArgumentException("targetRealAuthStatus must be 2 or 3");
        }
        if (rejected && !StringUtils.hasText(failReason)) {
            throw new IllegalArgumentException("realAuthFailReason is required when rejected");
        }
        MiniUser user = miniUserMapper.selectMiniUserById(id);
        if (user == null) {
            return 0;
        }
        user.setRealAuthStatus(targetRealAuthStatus);
        user.setRealAuthFailReason(failReason);
        user.setRealAuthReviewBy(reviewBy);
        user.setRealAuthReviewTime(reviewTime);
        user.setUpdateTime(new Date());
        return miniUserMapper.updateMiniUser(user);
    }

    @Override
    public int deleteMiniUserById(Long id) {
        return miniUserMapper.deleteMiniUserById(id);
    }

    @Override
    public int deleteMiniUserByIds(Long[] ids) {
        if (ids == null || ids.length == 0) {
            return 0;
        }
        return miniUserMapper.deleteMiniUserByIds(ids);
    }
}
