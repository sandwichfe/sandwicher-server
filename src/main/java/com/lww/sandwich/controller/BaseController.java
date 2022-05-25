package com.lww.sandwich.controller;

import com.lww.sandwich.response.ResponseResult;
import com.lww.sandwich.response.ResultUtil;
import com.lww.sandwich.service.ViewService;
import com.lww.sandwich.utils.IpUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @description:
 * @author lww
 * @since 2022/4/21 14:36
 */

@RestController
@RequestMapping("/base")
@Api("base")
@Slf4j
public class BaseController {

    @Resource
    private ViewService viewService;

    @GetMapping("/getBase")
    public ResponseResult getBase(String param1, String param2) {
        String ret = "a1 is " + param1 + " - a2 is " + param2;
        System.out.println("enterBase...");
        return ResultUtil.success(ret);
    }

    @GetMapping("/getRequestIp")
    public ResponseResult getRequestIp(HttpServletRequest request) {
        log.info("ip:-----------------");
        String ipAddress = IpUtils.getIpAddress(request);
        viewService.addViewRecord(ipAddress);
        return ResultUtil.success(ipAddress);
    }


}
