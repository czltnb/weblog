package com.czltnb.weblog.admin.controller;

import com.czltnb.weblog.admin.model.vo.blogsettings.UpdateBlogSettingsReqVO;
import com.czltnb.weblog.admin.service.AdminBlogSettingsService;
import com.czltnb.weblog.common.aspect.ApiOperationLog;
import com.czltnb.weblog.common.utils.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/blog/settings")
@Api(tags = "Admin 博客设置模块")
public class AdminBlogSettingsController {

    @Autowired
    private AdminBlogSettingsService blogSettingsService;

    @PostMapping("/update")
    @ApiOperation(value = "博客基础信息修改")
    @ApiOperationLog(description = "博客基础信息修改")
    public Response updateBlogSettings(@RequestBody @Validated UpdateBlogSettingsReqVO updateBlogSettingsReqVO) {
        return blogSettingsService.updateBlogSettings(updateBlogSettingsReqVO);
    }

    @PostMapping("/detail")
    @ApiOperation(value = "博客基础信息详情查询")
    @ApiOperationLog(description = "博客基础信息详情查询")
    public Response findBlogSettingsDetail(){
        return blogSettingsService.findBlogSettingsDetail();
    }

}
