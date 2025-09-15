package com.czltnb.weblog.web.controller;

import com.czltnb.weblog.common.aspect.ApiOperationLog;
import com.czltnb.weblog.common.enums.ResponseCodeEnum;
import com.czltnb.weblog.common.exception.BizException;
import com.czltnb.weblog.common.utils.JsonUtil;
import com.czltnb.weblog.common.utils.Response;
import com.czltnb.weblog.web.model.User;
import lombok.extern.slf4j.Slf4j;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@Slf4j
public class TestController {

    @PostMapping("/admin/test")
    @ApiOperationLog(description = "测试接口")
    public Response test(@RequestBody @Validated User user) {
//        log.info(JsonUtil.toJsonString(user));
        return Response.success();
    }



}

