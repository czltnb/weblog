package com.czltnb.weblog.admin.service;

import com.czltnb.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.czltnb.weblog.common.utils.Response;

public interface AdminCategoryService {

    /**
     * 添加文章分类
     */
    Response addCategory(AddCategoryReqVO addCategoryReqVO);
}
