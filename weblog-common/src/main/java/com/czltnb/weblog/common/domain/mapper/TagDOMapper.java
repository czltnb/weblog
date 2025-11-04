package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.CategoryDO;
import com.czltnb.weblog.common.domain.dos.TagDO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

public interface TagDOMapper {

    TagDO selectByName(String tagName);

    TagDO selectById(Long id);

    List<TagDO> selectByIds(List<Long> ids);

    List<TagDO> selectAll();

    int insertTag(TagDO tag);


    int selectCountByTagName(String tagName);



    List<TagDO> selectPageListByTagName(
            @Param("name") String tagName,
            @Param("offset") long offset,
            @Param("pageSize")long pageSize
    );

    List<TagDO> selectPageListByTagNameInTargetTime(
            @Param("name") String tagName,
            @Param("offset") long offset,
            @Param("pageSize")long pageSize,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    //逻辑删除，修改分类的 is_deleted字段
    int deleteTagById(@Param("id") Long id);

    int selectCount();

    List<TagDO> selectByTagsName(List<String> tagsName);

    int insertTags(List<TagDO> tags);
}
