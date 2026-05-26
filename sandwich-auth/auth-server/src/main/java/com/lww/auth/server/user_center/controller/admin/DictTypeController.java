package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.server.user_center.entity.DictType;
import com.lww.auth.server.user_center.req.DictTypeReq;
import com.lww.auth.server.user_center.service.DictTypeService;
import com.lww.auth.server.user_center.vo.DictTypePageQuery;
import com.lww.auth.server.user_center.vo.DictTypeVo;
import com.lww.common.utils.AssertUtils;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.log.Loggable;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "字典类型管理")
@RestController
@RequestMapping("/api/sys/dict-type")
public class DictTypeController {

    @Resource
    private DictTypeService dictTypeService;

    @Operation(summary = "新增字典类型")
    @Loggable(module = "dictType", type = "create", description = "新增字典类型", logResult = false)
    @PostMapping("/create")
    public ResponseResult<DictTypeVo> create(@RequestBody DictTypeReq req) {
        AssertUtils.assertTrue(!StringUtils.hasText(req.getTypeCode()), "类型编码不能为空");
        AssertUtils.assertTrue(!StringUtils.hasText(req.getTypeName()), "类型名称不能为空");

        boolean exists = dictTypeService.count(new LambdaQueryWrapper<DictType>()
                .eq(DictType::getTypeCode, req.getTypeCode())) > 0;
        AssertUtils.assertTrue(exists, "类型编码已存在");

        DictType dictType = new DictType();
        BeanUtils.copyProperties(req, dictType);
        dictTypeService.save(dictType);
        return ResultUtil.success(CustomBeanUtils.copyProperties(dictType, DictTypeVo.class));
    }

    @Operation(summary = "根据ID获取字典类型")
    @GetMapping("/get/{id}")
    public ResponseResult<DictTypeVo> get(@PathVariable Long id) {
        DictType dictType = dictTypeService.getById(id);
        return ResultUtil.success(CustomBeanUtils.copyProperties(dictType, DictTypeVo.class));
    }

    @Operation(summary = "字典类型分页列表")
    @PostMapping("/list")
    public ResponseResult<IPage<DictTypeVo>> list(@RequestBody DictTypePageQuery query) {
        Page<DictType> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<DictType> wrapper = new LambdaQueryWrapper<DictType>()
                .orderByAsc(DictType::getSort)
                .orderByDesc(DictType::getId);
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(DictType::getTypeCode, query.getKeyword())
                    .or()
                    .like(DictType::getTypeName, query.getKeyword()));
        }
        IPage<DictTypeVo> result = dictTypeService.page(page, wrapper)
                .convert(e -> CustomBeanUtils.copyProperties(e, DictTypeVo.class));
        return ResultUtil.success(result);
    }

    @Operation(summary = "获取全部字典类型")
    @GetMapping("/all")
    public ResponseResult<List<DictTypeVo>> all() {
        List<DictTypeVo> list = dictTypeService.list(new LambdaQueryWrapper<DictType>()
                        .orderByAsc(DictType::getSort)
                        .orderByDesc(DictType::getId))
                .stream()
                .map(e -> CustomBeanUtils.copyProperties(e, DictTypeVo.class))
                .toList();
        return ResultUtil.success(list);
    }

    @Operation(summary = "更新字典类型")
    @Loggable(module = "dictType", type = "update", description = "更新字典类型", logResult = false)
    @PostMapping("/update")
    public ResponseResult<DictTypeVo> update(@RequestBody DictTypeReq req) {
        AssertUtils.assertTrue(req.getId() == null, "ID不能为空");
        AssertUtils.assertTrue(!StringUtils.hasText(req.getTypeCode()), "类型编码不能为空");
        AssertUtils.assertTrue(!StringUtils.hasText(req.getTypeName()), "类型名称不能为空");

        boolean exists = dictTypeService.count(new LambdaQueryWrapper<DictType>()
                .eq(DictType::getTypeCode, req.getTypeCode())
                .ne(DictType::getId, req.getId())) > 0;
        AssertUtils.assertTrue(exists, "类型编码已存在");

        DictType dictType = new DictType();
        BeanUtils.copyProperties(req, dictType);
        dictTypeService.updateById(dictType);
        DictType updated = dictTypeService.getById(req.getId());
        return ResultUtil.success(CustomBeanUtils.copyProperties(updated, DictTypeVo.class));
    }

    @Operation(summary = "删除字典类型（同时删除其字典项）")
    @Loggable(module = "dictType", type = "delete", description = "删除字典类型ID: #id", logParams = false)
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> delete(@PathVariable Long id) {
        dictTypeService.deleteType(id);
        return ResultUtil.success();
    }
}
