package org.ba7lgj.ccp.miniprogram.vo;

import java.util.List;

public class MpTripChatUnreadSummaryVO {
    private Long totalUnread;
    private List<MpTripChatUnreadItemVO> items;

    public Long getTotalUnread() { return totalUnread; }
    public void setTotalUnread(Long totalUnread) { this.totalUnread = totalUnread; }

    public List<MpTripChatUnreadItemVO> getItems() { return items; }
    public void setItems(List<MpTripChatUnreadItemVO> items) { this.items = items; }
}
