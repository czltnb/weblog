package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.ArticleDO;

public interface ArticleDOMapper {

    int insert(ArticleDO article);

    ArticleDO selectArticleById(Long id);

    int delete(Long id);
}
