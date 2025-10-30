package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.TagDO;

public interface TagDOMapper {

    TagDO selectByName(String tagName);

    int insertTag(TagDO tag);
}
