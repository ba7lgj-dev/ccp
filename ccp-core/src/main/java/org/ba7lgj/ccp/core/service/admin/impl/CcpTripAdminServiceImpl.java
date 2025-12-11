package org.ba7lgj.ccp.core.service.admin.impl;

import org.ba7lgj.ccp.common.dto.trip.CcpTripAdminQuery;
import org.ba7lgj.ccp.common.dto.trip.CcpTripChangeStatusDTO;
import org.ba7lgj.ccp.common.vo.trip.CcpTripAdminDetailVO;
import org.ba7lgj.ccp.common.vo.trip.CcpTripAdminVO;
import org.ba7lgj.ccp.core.service.admin.CcpTripAdminService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 管理端拼车订单服务实现，目前为占位实现，后续可接入实际 Mapper 查询。
 */
@Service
public class CcpTripAdminServiceImpl implements CcpTripAdminService {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<CcpTripAdminVO> selectTripList(CcpTripAdminQuery query) {
        // 占位实现，返回空列表以便前后端联调。
        return new ArrayList<>();
    }

    @Override
    public CcpTripAdminDetailVO selectTripDetailById(Long id) {
        CcpTripAdminDetailVO vo = new CcpTripAdminDetailVO();
        vo.setId(id);
        vo.setSchoolName("示例学校");
        vo.setCampusName("示例校区");
        vo.setOwnerNickName("示例用户");
        vo.setStartAddress("示例起点");
        vo.setEndAddress("示例终点");
        vo.setDepartureTime(LocalDateTime.now().format(FORMATTER));
        vo.setStatus(1);
        vo.setCurrentPeople(1);
        vo.setTotalPeople(4);
        vo.setMemberCount(1);
        vo.setFinishedCount(0);
        vo.setNoShowCount(0);
        return vo;
    }

    @Override
    public int changeStatus(CcpTripChangeStatusDTO dto) {
        // 占位实现，返回成功标识。
        return 1;
    }
}
