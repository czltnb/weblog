package com.czltnb.weblog.web.service.impl;

import com.czltnb.weblog.common.domain.dos.ArticleDO;
import com.czltnb.weblog.common.domain.dos.ArticleTagRelDO;
import com.czltnb.weblog.common.domain.dos.TagDO;
import com.czltnb.weblog.common.domain.mapper.ArticleDOMapper;
import com.czltnb.weblog.common.domain.mapper.ArticleTagRelDOMapper;
import com.czltnb.weblog.common.domain.mapper.TagDOMapper;
import com.czltnb.weblog.common.enums.ResponseCodeEnum;
import com.czltnb.weblog.common.exception.BizException;
import com.czltnb.weblog.common.utils.PageResponse;
import com.czltnb.weblog.common.utils.Response;
import com.czltnb.weblog.web.model.vo.category.FindCategoryArticlePageListRspVO;
import com.czltnb.weblog.web.model.vo.tag.FindTagArticlePageListReqVO;
import com.czltnb.weblog.web.model.vo.tag.FindTagArticlePageListRspVO;
import com.czltnb.weblog.web.model.vo.tag.FindTagListRspVO;
import com.czltnb.weblog.web.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TagServiceImpl implements TagService {

    @Autowired
    private TagDOMapper tagDOMapper;
    @Autowired
    private ArticleTagRelDOMapper articleTagRelDOMapper;
    @Autowired
    private ArticleDOMapper articleDOMapper;

    /**
     * 获取标签列表
     *
     * @return
     */
    @Override
    public Response findTagList() {
        // 查询所有标签
        List<TagDO> tagDOS = tagDOMapper.selectAll();

        // DO 转 VO
        List<FindTagListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(tagDOS)) {
            vos = tagDOS.stream()
                    .map(tagDO -> FindTagListRspVO.builder()
                            .id(tagDO.getId())
                            .name(tagDO.getName())
                            .build())
                    .collect(Collectors.toList());
        }

        return Response.success(vos);
    }

    /**
     * 获取标签下文章分页列表
     *
     * @param findTagArticlePageListReqVO
     * @return
     */
    @Override
    public Response findTagPageList(FindTagArticlePageListReqVO findTagArticlePageListReqVO) {
        long pageNo = findTagArticlePageListReqVO.getPageNo();
        long pageSize = findTagArticlePageListReqVO.getPageSize();

        long offset = PageResponse.getOffset(pageNo, pageSize);

        Long tagId = findTagArticlePageListReqVO.getId();;

        TagDO tagDO = tagDOMapper.selectById(tagId);
        //判断该标签是否存在
        if (Objects.isNull(tagDO)) {
            log.warn("==> 该标签不存在, categoryId: {}", tagId);
            throw new BizException(ResponseCodeEnum.TAG_ID_IS_NOT_EXISTED);
        }

        //查询该标签下所有关联的文章ID
        List<ArticleTagRelDO> articleTagRelDOS = articleTagRelDOMapper.batchSelectByTagId(tagId);

        // 若该标签下未发布任何文章
        if (CollectionUtils.isEmpty(articleTagRelDOS)) {
            log.info("==> 该标签下还未发布任何文章, tagId: {}", tagId);
            return PageResponse.success(null, pageNo,0,pageSize);
        }

        long totalCount = articleTagRelDOS.size();

        // 提取所有文章 ID
        List<Long> articleIds = articleTagRelDOS.stream().map(ArticleTagRelDO::getArticleId).collect(Collectors.toList());

        // 根据文章ID批量查询文章分页数据
        List<ArticleDO> articleDOS = articleDOMapper.selectArticlePageListByArticleIds(articleIds,offset,pageSize);

        //DO转VO
        List<FindTagArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            vos = articleDOS.stream()
                    .map(articleDO -> FindTagArticlePageListRspVO.builder()
                            .id(articleDO.getId())
                            .title(articleDO.getTitle())
                            .cover(articleDO.getCover())
                            .createDate(articleDO.getCreateTime().toLocalDate())
                            .build()).collect(Collectors.toList());
        }

        return PageResponse.success(vos,pageNo,totalCount,pageSize);
    }
}
