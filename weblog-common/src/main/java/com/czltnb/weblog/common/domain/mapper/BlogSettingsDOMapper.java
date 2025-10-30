package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.BlogSettingsDO;
import org.apache.ibatis.annotations.Param;

public interface BlogSettingsDOMapper {

    BlogSettingsDO selectById(@Param("id") Long id);

    int insert(BlogSettingsDO blogSettingsDO);

    int update(BlogSettingsDO blogSettingsDO);
}
