package com.czltnb.weblog.web.service.impl;

import com.czltnb.weblog.admin.event.ReadArticleEvent;
import com.czltnb.weblog.admin.model.vo.article.FindArticlePageListReqVO;
import com.czltnb.weblog.admin.model.vo.article.FindArticlePageListRspVO;
import com.czltnb.weblog.common.domain.dos.*;
import com.czltnb.weblog.common.domain.mapper.*;
import com.czltnb.weblog.common.enums.ResponseCodeEnum;
import com.czltnb.weblog.common.exception.BizException;
import com.czltnb.weblog.common.utils.PageResponse;
import com.czltnb.weblog.common.utils.Response;
import com.czltnb.weblog.web.model.vo.article.*;
import com.czltnb.weblog.web.model.vo.category.FindCategoryListRspVO;
import com.czltnb.weblog.web.model.vo.tag.FindTagListRspVO;
import com.czltnb.weblog.web.service.ArticleService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDOMapper articleDOMapper;
    @Autowired
    private ArticleContentDOMapper articleContentDOMapper;
    @Autowired
    private ArticleCategoryRelDOMapper articleCategoryRelDOMapper;
    @Autowired
    private CategoryDOMapper categoryDOMapper;
    @Autowired
    private TagDOMapper tagDOMapper;
    @Autowired
    private ArticleTagRelDOMapper articleTagRelDOMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public PageResponse findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO){
        long pageNo = findIndexArticlePageListReqVO.getPageNo();

        long pageSize = 10;

        long totalCount = articleDOMapper.selectCount();

        if(totalCount == 0){
            return PageResponse.success(null,pageNo,totalCount,pageSize);
        }

        long offset = PageResponse.getOffset(pageNo,pageSize);

        //分页查询
        List<ArticleDO> articleDOS = articleDOMapper.selectArticlePageList(offset,pageSize);

        //再判定一次，因为可能有"分类总数不为空，但是指定查询的页码大于最大页码，查到空数据"的情况
        if(CollectionUtils.isEmpty(articleDOS)){
            return PageResponse.success(null,pageNo,totalCount,pageSize);
        }

        //DO转VO
        List<FindIndexArticlePageListRspVO> findIndexArticlePageListRspVOS = null;

        //拿到文章所有 ID 集合
        List<Long> articleIds = articleDOS.stream().map(ArticleDO::getId).collect(Collectors.toList());

        //设置文章所属分类
        //查询所有分类
        List<CategoryDO> categoryDOS = categoryDOMapper.selectAll();
        // 转 Map
        Map<Long,String> categoryIdNameMap = categoryDOS.stream().collect(Collectors.toMap(CategoryDO::getId, CategoryDO::getName));

        //根据文章ID批量查询所有关联记录
        List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelDOMapper.batchSelectByArticleIds(articleIds);


        //1.创建返参VO集合
        findIndexArticlePageListRspVOS = articleDOS.stream()
                .map(articleDO -> FindIndexArticlePageListRspVO.builder()
                        .id(articleDO.getId())
                        .title(articleDO.getTitle())
                        .cover(articleDO.getCover())
                        .summary(articleDO.getSummary())
                        .createTime(articleDO.getCreateTime())
                        .build())
                .collect(Collectors.toList());

        //2.设置集合中的每一个元素设置categoryId
        findIndexArticlePageListRspVOS.forEach(vo -> {
            Long currArticleId = vo.getId();
            // 过滤出当前文章对应的关联数据
            Optional<ArticleCategoryRelDO> optional = articleCategoryRelDOS.stream().filter(rel -> Objects.equals(rel.getArticleId(), currArticleId)).findAny();

            // 若不为空
            if (optional.isPresent()) {
                ArticleCategoryRelDO articleCategoryRelDO = optional.get();
                Long categoryId = articleCategoryRelDO.getCategoryId();
                // 通过分类 ID 从 map 中拿到对应的分类名称
                String categoryName = categoryIdNameMap.get(categoryId);

                FindCategoryListRspVO findCategoryListRspVO = FindCategoryListRspVO.builder()
                        .id(categoryId)
                        .name(categoryName)
                        .build();
                // 设置到当前 vo 类中
                vo.setCategory(findCategoryListRspVO);
            }
        });

        //3.为集合中的每一个元素设置tags，标签集合
        // 查询所有标签
        List<TagDO> tagDOS = tagDOMapper.selectAll();
        // 转 Map, 方便后续根据标签 ID 拿到对应的标签名称
        Map<Long, String> mapIdNameMap = tagDOS.stream().collect(Collectors.toMap(TagDO::getId, TagDO::getName));

        // 拿到所有文章的标签关联记录
        /**
         * 这个区分一下batchSelectByArticleId方法，那个方法是返回某个文章的标签集合
         * 这个是返回，所有文章的所有标签集合
         * 这个方法入参是articleIds，是一个List，那个是单独的 articleId
         * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!重点
         */
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelDOMapper.batchSelectByArticleIds(articleIds);

        findIndexArticlePageListRspVOS.forEach(vo -> {
            Long currArticleId = vo.getId();
            // 过滤出当前文章的标签关联记录
            List<ArticleTagRelDO> articleTagRelDOList = articleTagRelDOS.stream().filter(rel -> Objects.equals(rel.getArticleId(), currArticleId)).collect(Collectors.toList());

            List<FindTagListRspVO> findTagListRspVOS = Lists.newArrayList();
            // 将关联记录 DO 转 VO, 并设置对应的标签名称
            articleTagRelDOList.forEach(articleTagRelDO -> {
                Long tagId = articleTagRelDO.getTagId();
                String tagName = mapIdNameMap.get(tagId);

                FindTagListRspVO findTagListRspVO = FindTagListRspVO.builder()
                        .id(tagId)
                        .name(tagName)
                        .build();
                findTagListRspVOS.add(findTagListRspVO);
            });
            // 设置转换后的标签数据
            vo.setTags(findTagListRspVOS);
        });



        return PageResponse.success(findIndexArticlePageListRspVOS,pageNo,totalCount,pageSize);
    }

    /**
     * 获取文章详情
     *
     * @param findArticleDetailReqVO
     * @return
     */
    @Override
    public Response findArticleDetail(FindArticleDetailReqVO findArticleDetailReqVO) {
        Long articleId = findArticleDetailReqVO.getArticleId();

        ArticleDO articleDO = articleDOMapper.selectArticleById(articleId);

        //判断文章是否存在
        if(Objects.isNull(articleDO)){
            log.warn("==> 该文章不存在, articleId: {}", articleId);
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_EXISTED);
        }

        //查询正文
        ArticleContentDO articleContentDO = articleContentDOMapper.selectByArticleId(articleId);

        //DO转VO
        FindArticleDetailRspVO vo = FindArticleDetailRspVO.builder()
                .title(articleDO.getTitle())
                .createTime(articleDO.getCreateTime())
                .content(articleContentDO.getContent())
                .readNum(articleDO.getReadNum())
                .build();

        //查询所属分类
        ArticleCategoryRelDO articleCategoryRelDO = articleCategoryRelDOMapper.selectByArticleId(articleId);
        CategoryDO categoryDO = categoryDOMapper.selectById(articleCategoryRelDO.getCategoryId());
        vo.setCategoryId(categoryDO.getId());
        vo.setCategoryName(categoryDO.getName());

        //查询标签
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelDOMapper.batchSelectByArticleId(articleId);
        List<Long> tagIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getTagId).collect(Collectors.toList());
        if(!tagIds.isEmpty()) {
            List<TagDO> tagDOS = tagDOMapper.selectByIds(tagIds);

            //标签DO转VO
            List<FindTagListRspVO> tagListRspVOS = tagDOS.stream()
                    .map(tagDO -> FindTagListRspVO.builder().id(tagDO.getId()).name(tagDO.getName()).build())
                    .collect(Collectors.toList());
            vo.setTags(tagListRspVOS);
        }


        //上一篇
        ArticleDO preArticleDO = articleDOMapper.selectPreArticle(articleId);
        if(Objects.nonNull(preArticleDO)){
            FindPreNextArticleRspVO preArticleVO = FindPreNextArticleRspVO.builder()
                    .articleId(preArticleDO.getId())
                    .articleTitle(preArticleDO.getTitle())
                    .build();
            vo.setPreArticle(preArticleVO);
        }

        //下一篇
        ArticleDO nextArticleDO = articleDOMapper.selectNextArticle(articleId);
        if(Objects.nonNull(nextArticleDO)){
            FindPreNextArticleRspVO nextArticleRspVO = FindPreNextArticleRspVO.builder()
                    .articleId(nextArticleDO.getId())
                    .articleTitle(nextArticleDO.getTitle())
                    .build();
            vo.setNextArticle(nextArticleRspVO);
        }

        // 发布文章阅读事件
        eventPublisher.publishEvent(new ReadArticleEvent(this, articleId));

        return Response.success(vo);
    }


}
