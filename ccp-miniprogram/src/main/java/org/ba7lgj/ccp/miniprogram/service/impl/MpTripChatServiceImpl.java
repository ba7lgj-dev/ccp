package org.ba7lgj.ccp.miniprogram.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.ba7lgj.ccp.miniprogram.domain.MpTrip;
import org.ba7lgj.ccp.miniprogram.domain.MpTripChat;
import org.ba7lgj.ccp.miniprogram.domain.MpTripMember;
import org.ba7lgj.ccp.miniprogram.domain.MpUser;
import org.ba7lgj.ccp.miniprogram.exception.MpServiceException;
import org.ba7lgj.ccp.miniprogram.mapper.MpTripChatMapper;
import org.ba7lgj.ccp.miniprogram.mapper.MpTripChatReadMapper;
import org.ba7lgj.ccp.miniprogram.mapper.MpTripMapper;
import org.ba7lgj.ccp.miniprogram.mapper.MpTripMemberMapper;
import org.ba7lgj.ccp.miniprogram.mapper.MpUserMapper;
import org.ba7lgj.ccp.miniprogram.service.MpTripChatService;
import org.ba7lgj.ccp.miniprogram.vo.MpTripChatListVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripChatMessageVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripChatUnreadItemVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripChatUnreadSummaryVO;
import org.ba7lgj.ccp.miniprogram.vo.MpTripUnreadCountDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service("mpTripChatService")
public class MpTripChatServiceImpl implements MpTripChatService {
    private static final int DEFAULT_PAGE_SIZE = 20;
    private static final int MAX_PAGE_SIZE = 50;
    private static final int MAX_CONTENT_LENGTH = 500;
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private MpTripMapper tripMapper;

    @Autowired
    private MpTripMemberMapper tripMemberMapper;

    @Autowired
    private MpTripChatMapper tripChatMapper;

    @Autowired
    private MpTripChatReadMapper tripChatReadMapper;

    @Autowired
    private MpUserMapper userMapper;

    @Override
    public MpTripChatListVO listMessages(Long userId, Long tripId, Long lastId, Long lastMessageId, int pageSize) {
        MpTrip trip = ensureTripExists(tripId);
        ensureMember(userId, tripId);
        int size = pageSize <= 0 ? DEFAULT_PAGE_SIZE : Math.min(pageSize, MAX_PAGE_SIZE);
        List<MpTripChat> chats;
        boolean incremental = lastMessageId != null;
        if (incremental) {
            chats = tripChatMapper.selectNewerThan(tripId, lastMessageId, size);
        } else {
            chats = tripChatMapper.selectByTripWithPaging(tripId, lastId, size);
        }
        if (CollectionUtils.isEmpty(chats)) {
            MpTripChatListVO vo = new MpTripChatListVO();
            vo.setItems(Collections.emptyList());
            vo.setHasMore(false);
            return vo;
        }
        if (!incremental) {
            Collections.reverse(chats);
        }
        Map<Long, MpUser> userMap = loadUsers(chats);
        List<MpTripChatMessageVO> items = chats.stream().map(chat -> toMessageVO(chat, userId, userMap)).collect(Collectors.toList());
        Long smallestId = chats.get(0).getId();
        boolean hasMore = !incremental && chats.size() == size && tripChatMapper.countOlderMessages(tripId, smallestId) > 0;
        markAsRead(userId, tripId, chats);
        MpTripChatListVO vo = new MpTripChatListVO();
        vo.setItems(items);
        vo.setHasMore(hasMore);
        return vo;
    }

    @Override
    public MpTripChatMessageVO sendMessage(Long userId, Long tripId, String content) {
        MpTrip trip = ensureTripExists(tripId);
        if (trip.getStatus() != null && (trip.getStatus() == 4 || trip.getStatus() == 5)) {
            throw new MpServiceException(4005, "该拼单已结束，无法继续聊天");
        }
        ensureMember(userId, tripId);
        if (!StringUtils.hasText(content)) {
            throw new MpServiceException(400, "消息内容不能为空");
        }
        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new MpServiceException(400, "消息内容过长");
        }
        Date now = new Date();
        MpTripChat chat = new MpTripChat();
        chat.setTripId(tripId);
        chat.setSenderUserId(userId);
        chat.setContentType(1);
        chat.setMessageType(0);
        chat.setContent(content);
        chat.setIsDeleted(0);
        chat.setSendTime(now);
        chat.setCreateTime(now);
        chat.setUpdateTime(now);
        tripChatMapper.insertChat(chat);
        if (chat.getId() != null) {
            List<Long> selfChatIds = new ArrayList<>();
            selfChatIds.add(chat.getId());
            tripChatReadMapper.insertBatchReads(tripId, userId, selfChatIds);
        }
        Map<Long, MpUser> userMap = loadUsers(Collections.singletonList(chat));
        return toMessageVO(chat, userId, userMap);
    }

    @Override
    public void markReadUpTo(Long userId, Long tripId, Long lastChatId) {
        ensureTripExists(tripId);
        ensureMember(userId, tripId);
        if (lastChatId == null || lastChatId <= 0) {
            throw new MpServiceException(400, "参数错误");
        }
        tripChatReadMapper.insertReadUpTo(tripId, userId, lastChatId);
    }

    @Override
    public MpTripChatUnreadSummaryVO getUnreadSummary(Long userId) {
        List<Integer> statuses = new ArrayList<>();
        statuses.add(1);
        statuses.add(4);
        List<Long> tripIds = tripMemberMapper.selectTripIdsByUserAndStatus(userId, statuses);
        MpTripChatUnreadSummaryVO vo = new MpTripChatUnreadSummaryVO();
        if (CollectionUtils.isEmpty(tripIds)) {
            vo.setTotalUnread(0L);
            vo.setItems(Collections.emptyList());
            return vo;
        }
        List<MpTripUnreadCountDTO> counts = tripChatMapper.countUnreadByTripIds(tripIds, userId);
        Map<Long, Long> countMap = new HashMap<>();
        long total = 0L;
        if (!CollectionUtils.isEmpty(counts)) {
            for (MpTripUnreadCountDTO dto : counts) {
                countMap.put(dto.getTripId(), dto.getUnreadCount());
                total += dto.getUnreadCount() == null ? 0L : dto.getUnreadCount();
            }
        }
        List<MpTripChat> latestList = tripChatMapper.selectLatestByTripIds(tripIds);
        Map<Long, MpTripChat> latestMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(latestList)) {
            for (MpTripChat chat : latestList) {
                latestMap.put(chat.getTripId(), chat);
            }
        }
        List<MpTripChatUnreadItemVO> items = new ArrayList<>();
        for (Long tripId : tripIds) {
            MpTripChatUnreadItemVO item = new MpTripChatUnreadItemVO();
            item.setTripId(tripId);
            Long unreadCount = countMap.getOrDefault(tripId, 0L);
            item.setUnreadCount(unreadCount);
            MpTripChat latest = latestMap.get(tripId);
            if (latest != null) {
                String preview = latest.getContent();
                if (preview != null && preview.length() > 30) {
                    preview = preview.substring(0, 30);
                }
                item.setLastMessagePreview(preview);
                item.setLastMessageTime(formatTime(latest.getSendTime()));
            }
            items.add(item);
        }
        vo.setTotalUnread(total);
        vo.setItems(items);
        return vo;
    }

    private MpTrip ensureTripExists(Long tripId) {
        if (tripId == null) {
            throw new MpServiceException(400, "参数错误");
        }
        MpTrip trip = tripMapper.selectById(tripId);
        if (trip == null) {
            throw new MpServiceException(404, "拼单不存在");
        }
        return trip;
    }

    private void ensureMember(Long userId, Long tripId) {
        MpTripMember member = tripMemberMapper.selectByTripIdAndUserId(tripId, userId);
        if (member == null || member.getStatus() == null || member.getStatus() == 2 || member.getStatus() == 3) {
            throw new MpServiceException(4004, "非本拼单成员，无法查看聊天记录");
        }
    }

    private Map<Long, MpUser> loadUsers(List<MpTripChat> chats) {
        Set<Long> userIds = new HashSet<>();
        for (MpTripChat chat : chats) {
            if (chat.getSenderUserId() != null) {
                userIds.add(chat.getSenderUserId());
            }
        }
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyMap();
        }
        List<MpUser> users = userMapper.selectByIds(new ArrayList<>(userIds));
        if (CollectionUtils.isEmpty(users)) {
            return Collections.emptyMap();
        }
        Map<Long, MpUser> map = new HashMap<>();
        for (MpUser user : users) {
            map.put(user.getId(), user);
        }
        return map;
    }

    private MpTripChatMessageVO toMessageVO(MpTripChat chat, Long currentUserId, Map<Long, MpUser> userMap) {
        MpTripChatMessageVO vo = new MpTripChatMessageVO();
        vo.setId(chat.getId());
        vo.setTripId(chat.getTripId());
        vo.setSenderUserId(chat.getSenderUserId());
        MpUser sender = userMap.get(chat.getSenderUserId());
        if (sender != null) {
            vo.setSenderNickName(sender.getNickName());
            vo.setSenderAvatarUrl(sender.getAvatarUrl());
        }
        vo.setContentType(chat.getContentType());
        vo.setMessageType(chat.getMessageType());
        vo.setContent(chat.getContent());
        vo.setSendTime(formatTime(chat.getSendTime()));
        vo.setMine(Objects.equals(currentUserId, chat.getSenderUserId()));
        return vo;
    }

    private String formatTime(Date date) {
        if (date == null) {
            return null;
        }
        synchronized (DATE_FORMAT) {
            return DATE_FORMAT.format(date);
        }
    }

    private void markAsRead(Long userId, Long tripId, List<MpTripChat> chats) {
        if (CollectionUtils.isEmpty(chats)) {
            return;
        }
        List<Long> chatIds = chats.stream()
            .map(MpTripChat::getId)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(chatIds)) {
            return;
        }
        tripChatReadMapper.insertBatchReads(tripId, userId, chatIds);
    }
}
