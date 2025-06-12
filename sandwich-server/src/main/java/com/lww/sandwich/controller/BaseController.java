package com.lww.sandwich.controller;

import cn.hutool.extra.servlet.JakartaServletUtil;
import com.lww.common.web.vo.PageDataVo;
import com.lww.common.web.vo.PageVo;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.sandwich.vo.ViewVO;
import com.lww.sandwich.service.ViewService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

/**
 *  
 * @author lww
 * @since 2022/4/21 14:36
 */

@RestController
@RequestMapping("/base")
@Tag(name = "base")
@Slf4j
@Validated
public class BaseController {

    @Resource
    private ViewService viewService;

    @GetMapping("/getBase")
    public ResponseResult<String> getBase(@NotBlank(message = "p1为空") String param1, @NotBlank(message = "p2为空") String param2) {
        String ret = "a1 is " + param1 + " - a2 is " + param2;
        return ResultUtil.success(ret);
    }

    @GetMapping("/getRequestIp")
    public ResponseResult<String> getRequestIp(HttpServletRequest request) {
        String ipAddress = JakartaServletUtil.getClientIP(request);
        viewService.addViewRecord(ipAddress,request);
        return ResultUtil.success(ipAddress);
    }

    @GetMapping("/getViewList")
    public ResponseResult<PageDataVo<List<ViewVO>>> getViewList(@Valid PageVo pageVo) {
        PageDataVo<List<ViewVO>> pageDataVo = viewService.getViewList(pageVo);
        return ResultUtil.success(pageDataVo);
    }

    @PostMapping("/uploadFile")
    public ResponseResult<String> uploadFile(MultipartFile file) {
        if (file == null) {
            return ResultUtil.error("文件为空！");
        }
        return ResultUtil.success(file.getOriginalFilename());
    }

}
