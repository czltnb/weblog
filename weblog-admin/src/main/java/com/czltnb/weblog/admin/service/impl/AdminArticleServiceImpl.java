package com.czltnb.weblog.admin.service.impl;

import com.czltnb.weblog.admin.model.vo.article.PublishArticleReqVO;
import com.czltnb.weblog.admin.service.AdminArticleService;
import com.czltnb.weblog.common.domain.dos.ArticleCategoryRelDO;
import com.czltnb.weblog.common.domain.dos.ArticleContentDO;
import com.czltnb.weblog.common.domain.dos.ArticleDO;
import com.czltnb.weblog.common.domain.dos.CategoryDO;
import com.czltnb.weblog.common.domain.mapper.ArticleCategoryRelDOMapper;
import com.czltnb.weblog.common.domain.mapper.ArticleContentDOMapper;
import com.czltnb.weblog.common.domain.mapper.ArticleDOMapper;
import com.czltnb.weblog.common.domain.mapper.CategoryDOMapper;
import com.czltnb.weblog.common.enums.ResponseCodeEnum;
import com.czltnb.weblog.common.exception.BizException;
import com.czltnb.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AdminArticleServiceImpl implements AdminArticleService {

    @Autowired
    private ArticleDOMapper articleDOMapper;
    @Autowired
    private ArticleContentDOMapper articleContentDOMapper;
    @Autowired
    private ArticleCategoryRelDOMapper articleCategoryRelDOMapper;
    @Autowired
    private CategoryDOMapper categoryDOMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response publishArticle(PublishArticleReqVO publishArticleReqVO) {
        //1.VO转ArticleDO
        ArticleDO articleDO = ArticleDO.builder()
                .title(publishArticleReqVO.getTitle())
                .cover(publishArticleReqVO.getCover())
                .summary(publishArticleReqVO.getSummary())
                .createTime(LocalDateTime.now())
                .updateTime(LocalDateTime.now())
                .build();
        articleDOMapper.insert(articleDO);

        //拿到文章的主键ID
        Long articleId = articleDO.getId();

        //2.VO 转 ArticleContentDO
        ArticleContentDO articleContentDO = ArticleContentDO.builder()
                .articleId(articleId)
                .content(publishArticleReqVO.getContent())
                .build();
        articleContentDOMapper.insert(articleContentDO);

        //3.处理文章关联的分类
        Long categoryId = publishArticleReqVO.getCategoryId();

        //检验该分类是否存在
        CategoryDO categoryDO = categoryDOMapper.selectById(categoryId);
        if (Objects.isNull(categoryDO)) {
            log.warn("==> 该分类不存在，categoryId: {}",categoryId);
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXISTED);
        }

        ArticleCategoryRelDO articleCategoryRelDO = ArticleCategoryRelDO.builder()
                .articleId(articleId)
                .categoryId(categoryId)
                .build();
        articleCategoryRelDOMapper.insert(articleCategoryRelDO);

        //4.处理文章关联的标签集合
        List<String> publishTags = publishArticleReqVO.getTags();
        insertTags(publishTags);

        return Response.success();
    }

    /**
     * 保存标签
     * @param tags
     */
    private static void insertTags(List<String> tags) {
        //TODO
    }
}
