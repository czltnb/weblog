package com.czltnb.weblog.admin.service.impl;

import com.czltnb.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.czltnb.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.czltnb.weblog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.czltnb.weblog.admin.model.vo.category.FindCategoryPageListRspVO;
import com.czltnb.weblog.admin.service.AdminCategoryService;
import com.czltnb.weblog.common.domain.dos.CategoryDO;
import com.czltnb.weblog.common.domain.mapper.CategoryDOMapper;
import com.czltnb.weblog.common.enums.ResponseCodeEnum;
import com.czltnb.weblog.common.exception.BizException;
import com.czltnb.weblog.common.utils.PageResponse;
import com.czltnb.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AdminCategoryServiceImpl implements AdminCategoryService {

    @Autowired
    CategoryDOMapper categoryDOMapper;

    @Override
    public Response addCategory(AddCategoryReqVO addCategoryReqVO) {
        String categoryName = addCategoryReqVO.getName();

        CategoryDO categoryDO = categoryDOMapper.selectByName(categoryName);

        //重复创建
        if (Objects.nonNull(categoryDO)) {
            log.warn("分类名称: {}，此分类已经存在，无法重复创建", categoryName);
            throw new BizException(ResponseCodeEnum.CATEGORY_NAME_IS_EXISTED);
        }

        CategoryDO insertCategoryDO = CategoryDO.builder()
                        .name(categoryName)
                        .build();

        categoryDOMapper.insertCategory(insertCategoryDO);

        return Response.success();
    }

    @Override
    public PageResponse findCategoryPageList(FindCategoryPageListReqVO findCategoryPageListReqVO) {
        String categoryName = findCategoryPageListReqVO.getName();
        long pageNo = findCategoryPageListReqVO.getPageNo(); //当前页码

        long pageSize = 4;//定义每页数据量为 4

        //如果传入的name名字为空，则模糊查询条件为"%%"就返回所有数据
        //不为空，则正常模糊查询
        //模糊查询数据库，求出总数据量 totalCount
        long totalCount =categoryDOMapper.selectCountByCategoryName(categoryName);

        //
        if(totalCount == 0){
            return PageResponse.success(null,pageNo,totalCount,pageSize);
        }

        long offset = PageResponse.getOffset(pageNo, pageSize);

        //模糊分页查询
        List<CategoryDO> categoryDOS = categoryDOMapper.selectPageListByCategoryName(categoryName, offset,pageSize);


        //再判定一次，因为可能有"分类总数不为空，但是指定查询的页码大于最大页码，查到空数据"的情况
        if(CollectionUtils.isEmpty(categoryDOS)){
            return PageResponse.success(null,pageNo,totalCount,pageSize);
        }

        //DO转VO
        List<FindCategoryPageListRspVO> findCategoryPageListRspVOS = null;
        findCategoryPageListRspVOS = categoryDOS.stream()
                .map(categoryDO -> FindCategoryPageListRspVO.builder()
                        .id(categoryDO.getId())
                        .name(categoryDO.getName())
                        .createTime(categoryDO.getCreateTime())
                        .build())
                .collect(Collectors.toList());

        return PageResponse.success(findCategoryPageListRspVOS,pageNo,totalCount,pageSize);
    }

    @Override
    public Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO) {
        Long categoryId = deleteCategoryReqVO.getId();

        //先查询是否有该分类
        CategoryDO categoryDO = categoryDOMapper.selectById(categoryId);
        if(Objects.isNull(categoryDO)){
            throw new BizException(ResponseCodeEnum.CATEGORY_ID_IS_NOT_EXISTED);
        }

        //逻辑删除该分类
        categoryDOMapper.deleteCategoryById(categoryId);

        return Response.success();
    }

}
