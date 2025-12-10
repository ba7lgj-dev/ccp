package org.ba7lgj.ccp.miniprogram.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.ba7lgj.ccp.miniprogram.domain.School;

@Mapper
public interface SchoolMapper {
    List<School> selectEnabledSchools();
}
