package com.lww.sandwich.controller;

import com.lww.sandwich.entity.View;
import com.lww.sandwich.pojo.Vo.PageDataVo;
import com.lww.sandwich.pojo.Vo.PageVo;
import com.lww.sandwich.response.ResponseResult;
import com.lww.sandwich.response.ResultUtil;
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
        viewService.addViewRecord(ipAddress);
        return ResultUtil.success(ipAddress);
    }

    @GetMapping("/getViewList")
    public ResponseResult<PageDataVo<List<View>>> getViewList(PageVo pageVo) {
        PageDataVo<List<View>> pageDataVo = viewService.getViewList(pageVo);
        return ResultUtil.success(pageDataVo);
    }

    @PostMapping("/uploadFile")
    public ResponseResult uploadFile(MultipartFile file) {
        if (file == null) {
            return ResultUtil.error("文件为空！");
        }
        return ResultUtil.success(file.getOriginalFilename());
    }

    @Test
    public void test() {

        List<String> sentences = Arrays.asList("hello world", "Jia Gou Wu Dao");
        // 单独peek操作不会执行
        sentences.stream().peek(sentence -> System.out.println(sentences));

        sentences.stream().forEach(s -> System.out.println(s));

        System.out.println("peek 加终止操作 .count");
        long l = sentences.stream().peek(sentence -> System.out.println(sentence)).count();
        System.out.println(l);

    }

    @Test
    public void test1() {
        // stream
        // 从员工集合中筛选出salary大于8000的员工，并放置到新的集合里。


    }


}
