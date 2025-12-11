package org.ba7lgj.ccp.miniprogram.domain;

import java.util.Date;

public class MpTripChatRead {
    private Long id;
    private Long chatId;
    private Long tripId;
    private Long userId;
    private Date readTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Date getReadTime() { return readTime; }
    public void setReadTime(Date readTime) { this.readTime = readTime; }
}
