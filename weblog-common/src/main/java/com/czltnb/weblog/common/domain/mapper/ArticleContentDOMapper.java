package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.ArticleContentDO;

public interface ArticleContentDOMapper {

    int insert(ArticleContentDO articleContentDO);

    int delete(Long articleId);

    ArticleContentDO selectByArticleId(Long articleId);
}
