package com.czltnb.weblog.web.model;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.validation.constraints.*;

@Data
@ApiModel(value = "用户实体类")
public class User {

    @NotBlank(message = "用户名不能为空")
    private String username;

    @NotNull(message = "需要填写性别性别")
    private Integer sex;

    @NotNull(message = "年龄不能为空")
    @Min(value = 18,message = "年龄必须大于18周岁")
    @Max(value = 100,message = "年龄必须小于100周岁")
    private Integer age;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;
}
