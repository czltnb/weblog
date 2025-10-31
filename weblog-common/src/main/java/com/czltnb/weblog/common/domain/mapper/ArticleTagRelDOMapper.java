package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.ArticleTagRelDO;

import java.util.List;

public interface ArticleTagRelDOMapper {

    int insert(ArticleTagRelDO articleTagRelDO);

    int batchInsert(List<ArticleTagRelDO> articleTagRelDOList);

    int delete(Long articleId);

    List<ArticleTagRelDO> batchSelectByArticleId(Long articleId);
}
