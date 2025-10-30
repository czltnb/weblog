package com.czltnb.weblog.admin.controller;

import com.czltnb.weblog.admin.model.vo.category.AddCategoryReqVO;
import com.czltnb.weblog.admin.model.vo.category.DeleteCategoryReqVO;
import com.czltnb.weblog.admin.model.vo.category.FindCategoryPageListReqVO;
import com.czltnb.weblog.admin.service.AdminCategoryService;
import com.czltnb.weblog.common.aspect.ApiOperationLog;
import com.czltnb.weblog.common.utils.PageResponse;
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
@Api(tags = "Admin 文章分类模块")
public class AdminCategoryController {

    @Autowired
    private AdminCategoryService adminCategoryService;

    @PostMapping("/category/add")
    @ApiOperation(value = "添加分类")
    @ApiOperationLog(description = "添加文章分类")
    public Response addCategory(@RequestBody @Validated AddCategoryReqVO addCategoryReqVO){
        return adminCategoryService.addCategory(addCategoryReqVO);
    }

    /**
     * 分页查询接口
     */
    @PostMapping("/category/findPageList")
    @ApiOperation(value = "分类名分页查询")
    @ApiOperationLog(description = "文章分类名分页查询")
    public PageResponse findCategoryPageList(@RequestBody @Validated FindCategoryPageListReqVO findCategoryPageListReqVO){
        return adminCategoryService.findCategoryPageList(findCategoryPageListReqVO);
    }

    /**
     * 逻辑删除分类接口开发
     */
    @PostMapping("/category/delete")
    @ApiOperation(value = "删除分类")
    @ApiOperationLog(description = "删除文章分类")
    public Response deleteCategory(@RequestBody @Validated DeleteCategoryReqVO deleteCategoryReqVO){
        return adminCategoryService.deleteCategory(deleteCategoryReqVO);
    }

    @PostMapping("/category/select/list")
    @ApiOperation(value = "分类 Select 下拉列表数据获取")
    @ApiOperationLog(description = "分类 Select 下拉列表数据获取")
    public Response selectCategoryList(){
        return adminCategoryService.selectCategoryList();
    }
}
