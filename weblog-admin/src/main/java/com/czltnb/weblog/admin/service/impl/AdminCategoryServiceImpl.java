package com.czltnb.weblog.admin.service.impl;

import com.czltnb.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.czltnb.weblog.admin.service.AdminCategoryService;
import com.czltnb.weblog.common.domain.dos.CategoryDO;
import com.czltnb.weblog.common.domain.mapper.CategoryDOMapper;
import com.czltnb.weblog.common.enums.ResponseCodeEnum;
import com.czltnb.weblog.common.exception.BizException;
import com.czltnb.weblog.common.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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

}
