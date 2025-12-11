package org.ba7lgj.ccp.miniprogram.controller;

import org.ba7lgj.ccp.miniprogram.context.MpUserContextHolder;
import org.ba7lgj.ccp.miniprogram.exception.MpServiceException;
import org.ba7lgj.ccp.miniprogram.service.MpTripChatService;
import org.ba7lgj.ccp.miniprogram.vo.MpResult;
import org.ba7lgj.ccp.miniprogram.vo.MpTripChatListVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripChatMessageVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripChatUnreadSummaryVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mp/trip/chat")
public class MpTripChatController {
    @Autowired
    private MpTripChatService tripChatService;

    @GetMapping("/list")
    public MpResult<MpTripChatListVO> list(@RequestParam("tripId") Long tripId,
                                           @RequestParam(value = "lastId", required = false) String lastId,
                                           @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        int size = pageSize == null ? 20 : pageSize;
        Long lastIdVal = null;
        if (StringUtils.isNotBlank(lastId) && !"null".equalsIgnoreCase(lastId)) {
            try {
                lastIdVal = Long.valueOf(lastId);
            } catch (NumberFormatException e) {
                return MpResult.error(400, "lastId参数格式错误");
            }
        }
        try {
            MpTripChatListVO vo = tripChatService.listMessages(userId, tripId, lastIdVal, size);
            return MpResult.ok(vo);
        } catch (MpServiceException e) {
            return MpResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return MpResult.error(500, "获取聊天记录失败");
        }
    }

    @PostMapping("/send")
    public MpResult<MpTripChatMessageVO> send(@RequestBody MpTripChatMessageVO payload) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        if (payload == null || payload.getTripId() == null) {
            return MpResult.error(400, "参数错误");
        }
        try {
            MpTripChatMessageVO vo = tripChatService.sendMessage(userId, payload.getTripId(), payload.getContent());
            return MpResult.ok(vo);
        } catch (MpServiceException e) {
            return MpResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return MpResult.error(500, "发送消息失败");
        }
    }

    @PostMapping("/markRead")
    public MpResult<Void> markRead(@RequestBody MarkReadRequest payload) {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        if (payload == null || payload.getTripId() == null || payload.getLastChatId() == null) {
            return MpResult.error(400, "参数错误");
        }
        try {
            tripChatService.markReadUpTo(userId, payload.getTripId(), payload.getLastChatId());
            return MpResult.ok();
        } catch (MpServiceException e) {
            return MpResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return MpResult.error(500, "标记已读失败");
        }
    }

    @GetMapping("/unreadSummary")
    public MpResult<MpTripChatUnreadSummaryVO> unreadSummary() {
        Long userId = MpUserContextHolder.getUserId();
        if (userId == null) {
            return MpResult.error(4001, "未登录或token无效");
        }
        try {
            MpTripChatUnreadSummaryVO vo = tripChatService.getUnreadSummary(userId);
            return MpResult.ok(vo);
        } catch (MpServiceException e) {
            return MpResult.error(e.getCode(), e.getMessage());
        } catch (Exception e) {
            return MpResult.error(500, "获取未读信息失败");
        }
    }

    public static class MarkReadRequest {
        private Long tripId;
        private Long lastChatId;

        public Long getTripId() {
            return tripId;
        }

        public void setTripId(Long tripId) {
            this.tripId = tripId;
        }

        public Long getLastChatId() {
            return lastChatId;
        }

        public void setLastChatId(Long lastChatId) {
            this.lastChatId = lastChatId;
        }
    }
}
