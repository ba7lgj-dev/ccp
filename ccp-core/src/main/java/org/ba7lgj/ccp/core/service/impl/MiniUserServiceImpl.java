package org.ba7lgj.ccp.core.service.impl;

import org.ba7lgj.ccp.common.domain.MiniUser;
import org.ba7lgj.ccp.common.dto.MiniUserQueryDTO;
import org.ba7lgj.ccp.common.enums.AuthStatusEnum;
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
        newUser.setAuthStatus(AuthStatusEnum.NOT_AUTH.getCode());
        newUser.setCreateTime(new Date());
        miniUserMapper.insertMiniUser(newUser);
        return newUser;
    }

    @Override
    public int insertMiniUser(MiniUser user) {
        user.setCreateTime(new Date());
        user.setAuthStatus(user.getAuthStatus() == null ? AuthStatusEnum.NOT_AUTH.getCode() : user.getAuthStatus());
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
    public int reviewUser(Long id, Integer targetAuthStatus, String failReason, Long reviewBy, Date reviewTime) {
        if (!AuthStatusEnum.APPROVED.getCode().equals(targetAuthStatus)
                && !AuthStatusEnum.REJECTED.getCode().equals(targetAuthStatus)) {
            throw new IllegalArgumentException("targetAuthStatus must be 2 or 3");
        }
        if (AuthStatusEnum.REJECTED.getCode().equals(targetAuthStatus) && !StringUtils.hasText(failReason)) {
            throw new IllegalArgumentException("authFailReason is required when rejected");
        }
        MiniUser user = miniUserMapper.selectMiniUserById(id);
        if (user == null) {
            return 0;
        }
        user.setAuthStatus(targetAuthStatus);
        user.setAuthFailReason(failReason);
        user.setReviewBy(reviewBy);
        user.setReviewTime(reviewTime);
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
