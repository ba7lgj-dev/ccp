package org.ba7lgj.ccp.miniprogram.service;

import java.util.List;
import org.ba7lgj.ccp.miniprogram.vo.MpTripChatListVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripChatMessageVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripChatUnreadSummaryVO;

public interface MpTripChatService {
    MpTripChatListVO listMessages(Long userId, Long tripId, Long lastId, int pageSize);

    MpTripChatMessageVO sendMessage(Long userId, Long tripId, String content);

    void markReadUpTo(Long userId, Long tripId, Long lastChatId);

    MpTripChatUnreadSummaryVO getUnreadSummary(Long userId);
}
