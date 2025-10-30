package com.czltnb.weblog.admin.service;

import com.czltnb.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.czltnb.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.czltnb.weblog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.czltnb.weblog.common.utils.PageResponse;
import com.czltnb.weblog.common.utils.Response;

public interface AdminCategoryService {

    /**
     * 添加文章分类
     */
    Response addCategory(AddCategoryReqVO addCategoryReqVO);

    /**
     * 按照分类名，分页查询文章
     */
    PageResponse findCategoryPageList(FindCategoryPageListReqVO findCategoryPageListReqVO);

    /**
     * 删除文章分类
     */
    Response deleteCategory(DeleteCategoryReqVO deleteCategoryReqVO);
}
