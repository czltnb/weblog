package com.czltnb.weblog.admin.service;

import com.czltnb.weblog.admin.model.vo.tag.AddTagReqVO;
import com.czltnb.weblog.admin.model.vo.tag.FindTagPageListReqVO;
import com.czltnb.weblog.common.utils.PageResponse;
import com.czltnb.weblog.common.utils.Response;

public interface AdminTagService {

    /**
     * 添加标签接口
     */
    Response addTag(AddTagReqVO addTagReqVO);

    /**
     * 分页查询标签
     */
    PageResponse findTagPageList(FindTagPageListReqVO findTagPageListReqVO);
}
