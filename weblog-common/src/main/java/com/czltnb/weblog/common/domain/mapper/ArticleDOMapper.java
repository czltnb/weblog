package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.ArticleDO;

import java.time.LocalDate;
import java.util.List;

public interface ArticleDOMapper {

    int insert(ArticleDO article);

    ArticleDO selectArticleById(Long id);

    int delete(Long id);

    int updateArticleById(ArticleDO article);

    int selectCountByTitleInTargetTime(String title, LocalDate startDate, LocalDate endDate);

    List<ArticleDO> selectArticleByTitleInTargetTime(String title, LocalDate startDate, LocalDate endDate,long offset,long pageSize);

    int selectCount();

    List<ArticleDO> selectArticlePageList(long offset,long pageSize);
}
