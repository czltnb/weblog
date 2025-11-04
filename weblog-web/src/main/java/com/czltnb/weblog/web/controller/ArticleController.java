package com.czltnb.weblog.web.controller;

import com.czltnb.weblog.common.aspect.ApiOperationLog;
import com.czltnb.weblog.common.utils.PageResponse;
import com.czltnb.weblog.common.utils.Response;
import com.czltnb.weblog.web.model.vo.article.FindArticleDetailReqVO;
import com.czltnb.weblog.web.model.vo.article.FindIndexArticlePageListReqVO;
import com.czltnb.weblog.web.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "文章")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/article/list")
    @ApiOperation(value = "获取首页文章分页数据")
    @ApiOperationLog(description = "获取首页文章分页数据")
    public PageResponse findArticlePageList(@RequestBody @Validated FindIndexArticlePageListReqVO findIndexArticlePageListReqVO){
        return articleService.findArticlePageList(findIndexArticlePageListReqVO);
    }

    @PostMapping("/article/detail")
    @ApiOperation(value = "获取文章详情")
    @ApiOperationLog(description = "获取文章详情")
    public Response findArticleDetail(@RequestBody FindArticleDetailReqVO findArticleDetailReqVO) {
        return articleService.findArticleDetail(findArticleDetailReqVO);
    }
}
