package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.CategoryDO;
import org.apache.ibatis.annotations.Param;

public interface CategoryDOMapper {

    CategoryDO selectByName(@Param("name") String categoryName);

    int insertCategory(CategoryDO category);
}
