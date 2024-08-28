package com.lww.sandwich.controller.test;

import com.lww.sandwich.entity.DictType;
import com.lww.sandwich.response.ResponseResult;
import com.lww.sandwich.response.ResultUtil;
import com.lww.sandwich.service.DictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @description:
 * @author lww
 * @since 2022/4/21 14:36
 */

@RestController
@RequestMapping("/test")
@Api("test")
@Slf4j
public class TestController {

    @Resource
    private DictTypeService dictTypeService;

    @GetMapping("/listAllDictType")
    @ApiOperation(value = "返回所有")
    public ResponseResult<List<DictType>> listAllDictType() {
        List<DictType> data = dictTypeService.list();
        return ResultUtil.success(data);
    }


}
