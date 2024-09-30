package com.lww.sandwich.controller;

import com.lww.common.Vo.PageDataVo;
import com.lww.common.Vo.PageVo;
import com.lww.sandwich.entity.DictType;
import com.lww.common.web.response.ResponseCode;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.sandwich.service.DictTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典类型表 前端控制器
 * @author lww
 * @since 2022-04-07 16:00:02
 */
@Api(tags = "字典类型表")
@RestController
@RequestMapping("/sandwich/dictType")
public class DictTypeController {

    @Resource
    private DictTypeService dictTypeService;

    @GetMapping("/listAllDictType")
    @ApiOperation(value = "返回所有")
    public ResponseResult<List<DictType>> listAllDictType() {
        List<DictType> data = dictTypeService.list();
        return ResultUtil.success(data);
    }

    @PostMapping("/listDictTypePage")
    @ApiOperation("分页返回")
    public ResponseResult<PageDataVo<List<DictType>>>  listDictTypePage(PageVo pageVo) {
        return dictTypeService.getPage(pageVo);
    }

    @PostMapping("/addDictType")
    @ApiOperation("添加字典类型")
    public ResponseResult<Object> addDictType(DictType dictType) {
        boolean save = dictTypeService.save(dictType);
        return ResultUtil.response(ResponseCode.SUCCESS, "添加成功!");
    }

}
