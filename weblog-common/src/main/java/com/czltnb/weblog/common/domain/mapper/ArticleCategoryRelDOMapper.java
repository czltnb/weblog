package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.ArticleCategoryRelDO;

import java.util.List;

public interface ArticleCategoryRelDOMapper {

    int insert(ArticleCategoryRelDO articleCategoryRelDO);

    int delete(Long articleId);

    ArticleCategoryRelDO selectByArticleId(Long articleId);

    ArticleCategoryRelDO selectOneByCategoryId(Long categoryId);

    List<ArticleCategoryRelDO> batchSelectByArticleIds(List<Long> articleIds);

    List<ArticleCategoryRelDO> batchSelectByCategoryId(Long categoryId);
}
