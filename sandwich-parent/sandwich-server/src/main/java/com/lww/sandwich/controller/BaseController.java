package com.lww.sandwich.controller;

import com.lww.common.web.Vo.PageDataVo;
import com.lww.common.web.Vo.PageVo;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.sandwich.Vo.ViewVO;
import com.lww.sandwich.service.ViewService;
import com.lww.sandwich.utils.IpUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseResult getBase(@NotBlank(message = "p1为空") String param1, @NotBlank(message = "p2为空") String param2) {
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
    public ResponseResult<PageDataVo<List<ViewVO>>> getViewList(@Valid PageVo pageVo) {
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
