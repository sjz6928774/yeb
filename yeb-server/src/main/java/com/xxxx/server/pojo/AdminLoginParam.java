package com.xxxx.server.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 用户登录实体类
 */
@Getter
@Setter
// 可以使用链式set
@Accessors(chain = true)
@ApiModel(value = "AdminLogin对象", description = "")
public class AdminLoginParam {
    @ApiModelProperty(value = "用户名", required = true)
    private String username;
    @ApiModelProperty(value = "密码", required = true)
    private String password;
    @ApiModelProperty(value = "验证码", required = true)
    private String code;

}
