package com.czltnb.weblog.admin.service.impl;

import com.czltnb.weblog.admin.model.vo.article.DeleteArticleReqVO;
import com.czltnb.weblog.admin.model.vo.article.FindArticlePageListReqVO;
import com.czltnb.weblog.admin.model.vo.article.FindArticlePageListRspVO;
import com.czltnb.weblog.admin.model.vo.article.PublishArticleReqVO;
import com.czltnb.weblog.admin.service.AdminArticleService;
import com.czltnb.weblog.common.domain.dos.*;
import com.czltnb.weblog.common.domain.mapper.*;
import com.czltnb.weblog.common.enums.ResponseCodeEnum;
import com.czltnb.weblog.common.exception.BizException;
import com.czltnb.weblog.common.utils.PageResponse;
import com.czltnb.weblog.common.utils.Response;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    @Autowired
    private TagDOMapper tagDOMapper;
    @Autowired
    private ArticleTagRelDOMapper articleTagRelDOMapper;

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
        insertTags(articleId,publishTags);

        return Response.success();
    }

    /**
     * 保存标签
     * @param publishTags
     */
    private void insertTags(Long articleId,List<String> publishTags) {
        //筛选提交的标签中，数据库中不存在的标签
        List<String> notExistTags = null;
        //筛选提交的标签中，数据库中已经存在的标签
        List<String> existedTags = null;

        //批量查询出所有标签
        List<TagDO> tagDOS = tagDOMapper.selectAll();

        //如果数据库中没有任何标签
        if(CollectionUtils.isEmpty(tagDOS)) {
            notExistTags = publishTags;
        } else {
            //数据库中存在的tags
            List<String> tagNames = tagDOS.stream().map(tagDO -> tagDO.getName()).collect(Collectors.toList());

            //注意filter的使用，别和map()方法搞混了
            //从用户输入的标签中，提炼出数据库中已经存在的
            existedTags = publishTags.stream().filter(tagName -> tagNames.contains(tagName)).collect(Collectors.toList());
            //提炼出数据库中不存在的
            notExistTags = publishTags.stream().filter(tagName -> !tagNames.contains(tagName)).collect(Collectors.toList());

            // 补充逻辑：
            // 还有一种可能：按字符串名称提交上来的标签，也有可能是表中已存在的，比如数据库表中已经有了 Java 标签，用户提交了个 java 小写的标签

            /**
             * Map的key统一规定为小写，且来自数据库中存在的tag
             */
            Map<String,Long> tagNameIdMap = tagDOS.stream().collect(Collectors.toMap(tagDO -> tagDO.getName().toLowerCase(),TagDO::getId));

            //上边提炼 existedTags 和 notExistTags 的时候，有误判
            //比如数据库表中已经有了 Java 标签，用户提交了个 java 小写的标签，java标签误入notExistTags
            Iterator<String> iterator = notExistTags.iterator();
            while (iterator.hasNext()) {
                String notExistTag = iterator.next();
                String notExistTagLower = notExistTag.toLowerCase(); // 转小写，统一比对标准

                // 转小写, 若 Map 中相同的 key，则表示该新标签是重复标签
                if(tagNameIdMap.containsKey(notExistTagLower)) {
                    // 从notExistTags集合中清除
                    iterator.remove();
                    // 并将对应的 Name 添加到已存在的标签集合existedTags
                    existedTags.add(notExistTagLower);
                }
            }

            //I.对于数据库中存在的标签，只需存入文章-标签关联表
            if(!CollectionUtils.isEmpty(existedTags)) {
                List<ArticleTagRelDO> articleTagRelDOS = Lists.newArrayList();
                existedTags.forEach(tagName -> {
                    /**
                     * 先把tagName转化为小写，因为tagNameIdMap的键，都是小写的key
                     * 这一步很重要！！！！
                     */
                    String tagNameLover = tagName.toLowerCase();
                    ArticleTagRelDO articleTagRelDO = ArticleTagRelDO.builder()
                            .articleId(articleId)
                            .tagId(tagNameIdMap.get(tagNameLover))
                            .build();
                    articleTagRelDOS.add(articleTagRelDO);
                });
                //批量插入
                articleTagRelDOMapper.batchInsert(articleTagRelDOS);
            }

            //II.对于数据库中不存在的标签，先存入标签表、再存入文章-标签表
            if(!CollectionUtils.isEmpty(notExistTags)) {
                List<ArticleTagRelDO> articleTagRelDOS = Lists.newArrayList();
                notExistTags.forEach(tagName -> {
                    TagDO tagDO = TagDO.builder()
                            .name(tagName)
                            .createTime(LocalDateTime.now())
                            .updateTime(LocalDateTime.now())
                            .build();
                    tagDOMapper.insertTag(tagDO);

                    //拿到保存的标签ID
                    Long tagId = tagDO.getId();

                    //文章-标签关联入库
                    ArticleTagRelDO articleTagRelDO = ArticleTagRelDO.builder()
                            .articleId(articleId)
                            .tagId(tagId)
                            .build();
                    articleTagRelDOS.add(articleTagRelDO);
                });
                //批量插入
                articleTagRelDOMapper.batchInsert(articleTagRelDOS);
            }

        }

    }

    /**
     * 彻底删除文章(非逻辑删除)
     * @param deleteArticleReqVO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Response deleteArticle(DeleteArticleReqVO deleteArticleReqVO) {
        Long articleId = deleteArticleReqVO.getId();

        //先判断要删除的文章是否存在
        ArticleDO articleDO = articleDOMapper.selectArticleById(articleId);

        if(Objects.isNull(articleDO)) {
            log.warn("==> 要删除的文章不存在,文章id为: {}",articleId);
            throw new BizException(ResponseCodeEnum.ARTICLE_NOT_EXISTED);
        }

        //1.逻辑删除文章表内容
        articleDOMapper.delete(articleId);

        //2.删除文章内容表内容
        articleContentDOMapper.delete(articleId);
        //3.删除文章-分类表内容
        articleCategoryRelDOMapper.delete(articleId);
        //4.删除文章-标签表内容
        articleTagRelDOMapper.delete(articleId);

        return Response.success();
    }

    @Override
    public PageResponse findArticlePageList(FindArticlePageListReqVO findArticlePageListReqVO) {

        String title = findArticlePageListReqVO.getTitle();
        LocalDate startDate = findArticlePageListReqVO.getStartDate();
        LocalDate endDate = findArticlePageListReqVO.getEndDate();

        long pageNo = findArticlePageListReqVO.getPageNo();

        long pageSize = 10;

        long totalCount = articleDOMapper.selectCountByTitleInTargetTime(title,startDate,endDate);

        if(totalCount == 0){
            return PageResponse.success(null,pageNo,totalCount,pageSize);
        }

        long offset = PageResponse.getOffset(pageNo,pageSize);

        //模糊分页查询
        List<ArticleDO> articleDOS = articleDOMapper.selectArticleByTitleInTargetTime(title,startDate,endDate,offset,pageSize);

        //再判定一次，因为可能有"分类总数不为空，但是指定查询的页码大于最大页码，查到空数据"的情况
        if(CollectionUtils.isEmpty(articleDOS)){
            return PageResponse.success(null,pageNo,totalCount,pageSize);
        }

        //DO转VO
        List<FindArticlePageListRspVO> findArticlePageListRspVOS = null;
        findArticlePageListRspVOS = articleDOS.stream()
                .map(articleDO -> FindArticlePageListRspVO.builder()
                        .id(articleDO.getId())
                        .title(articleDO.getTitle())
                        .cover(articleDO.getCover())
                        .createTime(articleDO.getCreateTime())
                        .build())
                .collect(Collectors.toList());

        return PageResponse.success(findArticlePageListRspVOS,pageNo,totalCount,pageSize);
    }
}
