package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MpTripChatReadMapper {
    int insertBatchReads(@Param("tripId") Long tripId, @Param("userId") Long userId, @Param("chatIds") List<Long> chatIds);

    int insertReadUpTo(@Param("tripId") Long tripId, @Param("userId") Long userId, @Param("lastChatId") Long lastChatId);
}
