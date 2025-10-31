package com.czltnb.weblog.admin.service;

import com.czltnb.weblog.admin.model.vo.article.PublishArticleReqVO;
import com.czltnb.weblog.common.utils.Response;

public interface AdminArticleService {

    /**
     * 发布文章
     */
    Response publishArticle(PublishArticleReqVO publishArticleReqVO);
}
