package com.czltnb.weblog.admin.service;

import com.czltnb.weblog.admin.model.vo.article.DeleteArticleReqVO;
import com.czltnb.weblog.admin.model.vo.article.FindArticlePageListReqVO;
import com.czltnb.weblog.admin.model.vo.article.PublishArticleReqVO;
import com.czltnb.weblog.common.utils.PageResponse;
import com.czltnb.weblog.common.utils.Response;

public interface AdminArticleService {

    /**
     * 发布文章
     */
    Response publishArticle(PublishArticleReqVO publishArticleReqVO);

    /**
     * 删除文章
     */
    Response deleteArticle(DeleteArticleReqVO deleteArticleReqVO);

    /**
     * 文章分页查询
     */
    PageResponse findArticlePageList(FindArticlePageListReqVO findArticlePageListReqVO);
}
