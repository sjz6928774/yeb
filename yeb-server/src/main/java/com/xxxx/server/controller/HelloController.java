package com.xxxx.server.controller;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ApiModel(value = "测试")
public class HelloController {

    @PostMapping("/hello")
    @ApiOperation(value = "hello")
    public String hello(){
        return "hello";
    }
}
