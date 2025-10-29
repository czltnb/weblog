package com.czltnb.weblog.admin.controller;

import com.czltnb.weblog.admin.model.vo.user.UpdateAdminUserPasswordReqVO;
import com.czltnb.weblog.admin.service.AdminUserService;
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
@Api(tags = "Admin 用户模块")
public class AdminUserController {

    //注入接口而不是实现类，“依赖倒置原则”（依赖抽象，而非具体），便于扩展和维护，降低耦合度
    @Autowired
    private AdminUserService adminUserService;

    @PostMapping("/password/update")
    @ApiOperation(value = "修改密码")
    @ApiOperationLog(description = "修改密码")
    public Response updatePassword(@RequestBody @Validated UpdateAdminUserPasswordReqVO updateAdminUserPasswordReqVO) {
        return adminUserService.updatePassword(updateAdminUserPasswordReqVO);
    }

    @PostMapping("/user/info")
    @ApiOperation(value = "获取用户信息")
    @ApiOperationLog(description = "获取用户信息")
    public Response findUserInfo(){
        return adminUserService.findUserInfo();
    }

}
