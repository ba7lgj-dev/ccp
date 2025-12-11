package org.ba7lgj.ccp.miniprogram.vo;

import java.util.List;

public class MpTripChatListVO {
    private List<MpTripChatMessageVO> items;
    private boolean hasMore;

    public List<MpTripChatMessageVO> getItems() { return items; }
    public void setItems(List<MpTripChatMessageVO> items) { this.items = items; }

    public boolean isHasMore() { return hasMore; }
    public void setHasMore(boolean hasMore) { this.hasMore = hasMore; }
}
