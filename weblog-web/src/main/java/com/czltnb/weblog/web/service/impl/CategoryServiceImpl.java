package com.czltnb.weblog.web.service.impl;

import com.czltnb.weblog.common.domain.dos.ArticleCategoryRelDO;
import com.czltnb.weblog.common.domain.dos.ArticleDO;
import com.czltnb.weblog.common.domain.dos.CategoryDO;
import com.czltnb.weblog.common.domain.mapper.ArticleCategoryRelDOMapper;
import com.czltnb.weblog.common.domain.mapper.ArticleDOMapper;
import com.czltnb.weblog.common.domain.mapper.CategoryDOMapper;
import com.czltnb.weblog.common.enums.ResponseCodeEnum;
import com.czltnb.weblog.common.exception.BizException;
import com.czltnb.weblog.common.utils.PageResponse;
import com.czltnb.weblog.common.utils.Response;
import com.czltnb.weblog.web.model.vo.category.FindCategoryArticlePageListReqVO;
import com.czltnb.weblog.web.model.vo.category.FindCategoryArticlePageListRspVO;
import com.czltnb.weblog.web.model.vo.category.FindCategoryListRspVO;
import com.czltnb.weblog.web.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryDOMapper categoryDOMapper;
    @Autowired
    private ArticleCategoryRelDOMapper articleCategoryRelDOMapper;
    @Autowired
    private ArticleDOMapper articleDOMapper;

    /**
     * 获取分类列表
     *
     * @return
     */
    @Override
    public Response findCategoryList() {
        // 查询所有分类
        List<CategoryDO> categoryDOS = categoryDOMapper.selectAll();

        // DO 转 VO
        List<FindCategoryListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(categoryDOS)) {
            vos = categoryDOS.stream()
                    .map(categoryDO -> FindCategoryListRspVO.builder()
                            .id(categoryDO.getId())
                            .name(categoryDO.getName())
                            .build())
                    .collect(Collectors.toList());
        }

        return Response.success(vos);
    }

    @Override
    public PageResponse findCategoryArticlePageList(FindCategoryArticlePageListReqVO findCategoryArticlePageListReqVO) {
        long pageNo = findCategoryArticlePageListReqVO.getPageNo();
        long pageSize = findCategoryArticlePageListReqVO.getPageSize();

        long offset = PageResponse.getOffset(pageNo, pageSize);

        Long categoryId = findCategoryArticlePageListReqVO.getId();

        CategoryDO categoryDO = categoryDOMapper.selectById(categoryId);
        // 判断该分类是否存在
        if (Objects.isNull(categoryDO)) {
            log.warn("==> 该分类不存在, categoryId: {}", categoryId);
            throw new BizException(ResponseCodeEnum.CATEGORY_NOT_EXISTED);
        }

        //查询该分类下所有关联的文章ID
        List<ArticleCategoryRelDO> articleCategoryRelDOS = articleCategoryRelDOMapper.batchSelectByCategoryId(categoryId);

        // 若该分类下未发布任何文章
        if (CollectionUtils.isEmpty(articleCategoryRelDOS)) {
            log.info("==> 该分类下还未发布任何文章, categoryId: {}", categoryId);
            return PageResponse.success(null,pageNo,0,pageSize);
        }

        long totalCount = articleCategoryRelDOS.size();

        List<Long> articleIds = articleCategoryRelDOS.stream().map(ArticleCategoryRelDO::getArticleId).collect(Collectors.toList());

        // 根据文章ID批量查询文章分页数据
        List<ArticleDO> articleDOS = articleDOMapper.selectArticlePageListByArticleIds(articleIds,offset,pageSize);

        //DO转 VO
        List<FindCategoryArticlePageListRspVO> vos = null;
        if (!CollectionUtils.isEmpty(articleDOS)) {
            vos = articleDOS.stream()
                    .map(articleDO -> FindCategoryArticlePageListRspVO.builder()
                            .id(articleDO.getId())
                            .title(articleDO.getTitle())
                            .cover(articleDO.getCover())
                            .createDate(articleDO.getCreateTime().toLocalDate())
                            .build()).collect(Collectors.toList());
        }

        return PageResponse.success(vos,pageNo,totalCount,pageSize);
    }

}
