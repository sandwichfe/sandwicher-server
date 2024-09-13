package com.lww.sandwich.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lww.Vo.PageDataVo;
import com.lww.Vo.PageVo;
import com.lww.sandwich.Vo.ViewVO;
import com.lww.sandwich.entity.View;
import com.lww.response.ResponseResult;
import com.lww.response.ResultUtil;
import com.lww.sandwich.service.ViewService;
import com.lww.sandwich.utils.IpUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

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
        return ResultUtil.success(ret);
    }

    @GetMapping("/getRequestIp")
    public ResponseResult getRequestIp(HttpServletRequest request) {
        String ipAddress = IpUtils.getIpAddress(request);
        viewService.addViewRecord(ipAddress,request);
        return ResultUtil.success(ipAddress);
    }

    @GetMapping("/getViewList")
    public ResponseResult<PageDataVo<List<ViewVO>>> getViewList(PageVo pageVo) {
        PageDataVo<List<ViewVO>> pageDataVo = viewService.getViewList(pageVo);
        return ResultUtil.success(pageDataVo);
    }

    @PostMapping("/uploadFile")
    public ResponseResult uploadFile(MultipartFile file) {
        if (file == null) {
            return ResultUtil.error("文件为空！");
        }
        return ResultUtil.success(file.getOriginalFilename());
    }

}
