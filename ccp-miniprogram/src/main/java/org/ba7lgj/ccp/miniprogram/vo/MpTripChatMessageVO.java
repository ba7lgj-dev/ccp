package org.ba7lgj.ccp.miniprogram.vo;

public class MpTripChatMessageVO {
    private Long id;
    private Long tripId;
    private Long senderUserId;
    private String senderNickName;
    private String senderAvatarUrl;
    private Integer contentType;
    private Integer messageType;
    private String content;
    private String sendTime;
    private boolean mine;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    public Long getSenderUserId() { return senderUserId; }
    public void setSenderUserId(Long senderUserId) { this.senderUserId = senderUserId; }

    public String getSenderNickName() { return senderNickName; }
    public void setSenderNickName(String senderNickName) { this.senderNickName = senderNickName; }

    public String getSenderAvatarUrl() { return senderAvatarUrl; }
    public void setSenderAvatarUrl(String senderAvatarUrl) { this.senderAvatarUrl = senderAvatarUrl; }

    public Integer getContentType() { return contentType; }
    public void setContentType(Integer contentType) { this.contentType = contentType; }

    public Integer getMessageType() { return messageType; }
    public void setMessageType(Integer messageType) { this.messageType = messageType; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getSendTime() { return sendTime; }
    public void setSendTime(String sendTime) { this.sendTime = sendTime; }

    public boolean isMine() { return mine; }
    public void setMine(boolean mine) { this.mine = mine; }
}
