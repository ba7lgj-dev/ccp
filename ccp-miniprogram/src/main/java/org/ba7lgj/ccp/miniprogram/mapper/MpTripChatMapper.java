package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.ba7lgj.ccp.miniprogram.domain.MpTripChat;
import org.ba7lgj.ccp.miniprogram.vo.MpTripUnreadCountDTO;

@Mapper
public interface MpTripChatMapper {
    List<MpTripChat> selectByTripWithPaging(@Param("tripId") Long tripId, @Param("lastId") Long lastId, @Param("pageSize") int pageSize);

    int insertChat(MpTripChat chat);

    int countOlderMessages(@Param("tripId") Long tripId, @Param("id") Long id);

    List<MpTripChat> selectLatestByTripIds(@Param("tripIds") List<Long> tripIds);

    List<MpTripUnreadCountDTO> countUnreadByTripIds(@Param("tripIds") List<Long> tripIds, @Param("userId") Long userId);
}
