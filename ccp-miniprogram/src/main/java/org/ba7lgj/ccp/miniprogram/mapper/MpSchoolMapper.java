package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.ba7lgj.ccp.miniprogram.domain.MpSchool;

@Mapper
public interface MpSchoolMapper {
    List<MpSchool> selectEnabledSchools();
}
