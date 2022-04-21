package com.lww.sandwich.controller;

import com.lww.sandwich.response.ResponseResult;
import com.lww.sandwich.response.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author lww
 * @since 2022/4/21 14:36
 */

@RestController
@RequestMapping("/base")
@Api("base")
public class BaseController {


    @GetMapping("/getBase")
    public ResponseResult getBase(String param1, String param2) {
        String ret = "a1 is " + param1 + " - a2 is " + param2;
        System.out.println("enterBase...");
        return ResultUtil.success(ret);
    }
}
