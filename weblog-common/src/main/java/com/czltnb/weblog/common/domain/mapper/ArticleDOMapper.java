package com.czltnb.weblog.common.domain.mapper;

import com.czltnb.weblog.common.domain.dos.ArticleDO;
import com.czltnb.weblog.common.domain.dos.ArticlePublishCountDO;
import com.czltnb.weblog.common.domain.dos.StatisticsArticlePVDO;

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

    List<ArticleDO> selectArticlePageListByArticleIds(List<Long> articleIds,long offset,long pageSize);

    ArticleDO selectPreArticle(Long articleId);

    ArticleDO selectNextArticle(Long articleId);

    int increaseReadNum(Long articleId);

    /**
     * 查询所有文章的总阅读量
     */
    List<ArticleDO> selectAllReadNum();

    List<ArticlePublishCountDO> selectDateArticlePublishCount(LocalDate startDate, LocalDate endDate);

    /**
     * 获取最近七天的 PV 访问记录
     * @return
     */
    List<StatisticsArticlePVDO> selectLatestWeekRecords();
}
