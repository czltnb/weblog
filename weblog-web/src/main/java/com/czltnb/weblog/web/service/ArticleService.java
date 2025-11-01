package com.czltnb.weblog.web.service;

import com.czltnb.weblog.common.utils.PageResponse;
import com.czltnb.weblog.common.utils.Response;
import com.czltnb.weblog.web.model.vo.article.FindIndexArticlePageListReqVO;

public interface ArticleService {
    /**
     * 获取首页文章分页数据
     * @param findIndexArticlePageListReqVO
     * @return
     */
    PageResponse findArticlePageList(FindIndexArticlePageListReqVO findIndexArticlePageListReqVO);
}
