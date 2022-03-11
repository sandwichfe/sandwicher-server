package com.lww.sandwich.controller;

import com.lww.sandwich.entity.Simple;
import com.lww.sandwich.response.ResponseResult;
import com.lww.sandwich.response.ResultUtil;
import com.lww.sandwich.service.SimpleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 例子 前端控制器
 *
 * @author lww
 * @since 2022-03-11 16:18:29
 */
@Api(tags = "例子")
@RestController
@RequestMapping("/sandwich/simple")
public class SimpleController {

    @Resource
    private SimpleService simpleService;

    @ApiOperation("返回所有")
    @GetMapping("/listAll")
    public ResponseResult<Object> list() {
        List<Simple> list = simpleService.list();
        return ResultUtil.success(list);
    }

    @ApiOperation("根据id获取")
    @GetMapping("/getOne/{id}")
    public ResponseResult<Object> getOne(@PathVariable String id) {
        return ResultUtil.success(simpleService.getById(id));
    }

    @ApiOperation("添加")
    @PostMapping("addSimple")
    public ResponseResult<Object> addSimple(Simple simple) {
        return ResultUtil.success(simpleService.save(simple));
    }

    @ApiOperation("编辑")
    @PostMapping("editSimple")
    public ResponseResult<Object> editSimple(Simple simple) {
        return ResultUtil.success(simpleService.updateById(simple));
    }

    @ApiOperation("删除")
    @DeleteMapping("/deleteSimple/{id}")
    public ResponseResult<Object> deleteSimple(@PathVariable String id) {
        return ResultUtil.success(simpleService.removeById(id));
    }

}
