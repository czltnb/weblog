package com.czltnb.weblog.admin.controller;

import com.czltnb.weblog.admin.model.vo.tag.AddTagReqVO;
import com.czltnb.weblog.admin.service.AdminTagService;
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
@RequestMapping("/admin")
@Api(tags = "Admin 文章标签模块")
public class AdminTagController {

    @Autowired
    private AdminTagService adminTagService;

    @PostMapping("/tag/add")
    @ApiOperation(value = "添加标签")
    @ApiOperationLog(description = "添加标签")
    public Response addTag(@RequestBody @Validated AddTagReqVO addTagReqVO) {
        return adminTagService.addTag(addTagReqVO);
    }

}
