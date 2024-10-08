package com.lww.sandwich.controller;

import com.lww.common.web.vo.PageDataVo;
import com.lww.common.web.vo.PageVo;
import com.lww.sandwich.entity.DictType;
import com.lww.common.web.response.ResponseCode;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.sandwich.service.DictTypeService;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 字典类型表 前端控制器
 * @author lww
 * @since 2022-04-07 16:00:02
 */
@Tag(name = "字典类型表")
@RestController
@RequestMapping("/sandwich/dictType")
public class DictTypeController {

    @Resource
    private DictTypeService dictTypeService;

    @GetMapping("/listAllDictType")
    @Schema(description = "返回所有")
    public ResponseResult<List<DictType>> listAllDictType() {
        List<DictType> data = dictTypeService.list();
        return ResultUtil.success(data);
    }

    @PostMapping("/listDictTypePage")
    @Schema(description = "分页返回")
    public ResponseResult<PageDataVo<List<DictType>>>  listDictTypePage(PageVo pageVo) {
        return dictTypeService.getPage(pageVo);
    }

    @PostMapping("/addDictType")
    @Schema(description = "添加字典类型")
    public ResponseResult<Object> addDictType(DictType dictType) {
        boolean save = dictTypeService.save(dictType);
        return ResultUtil.response(ResponseCode.SUCCESS, "添加成功!");
    }

}
