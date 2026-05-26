package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.server.user_center.entity.DictItem;
import com.lww.auth.server.user_center.req.DictItemReq;
import com.lww.auth.server.user_center.service.DictItemService;
import com.lww.auth.server.user_center.vo.DictItemPageQuery;
import com.lww.auth.server.user_center.vo.DictItemVo;
import com.lww.common.utils.AssertUtils;
import com.lww.common.utils.CustomBeanUtils;
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

@Tag(name = "字典项管理")
@RestController
@RequestMapping("/api/sys/dict-item")
public class DictItemController {

    @Resource
    private DictItemService dictItemService;

    @Operation(summary = "新增字典项")
    @PostMapping("/create")
    public ResponseResult<DictItemVo> create(@RequestBody DictItemReq req) {
        AssertUtils.assertTrue(req.getDictTypeId() == null, "字典类型ID不能为空");
        AssertUtils.assertTrue(!StringUtils.hasText(req.getLabel()), "标签不能为空");
        AssertUtils.assertTrue(!StringUtils.hasText(req.getValue()), "值不能为空");

        boolean exists = dictItemService.count(new LambdaQueryWrapper<DictItem>()
                .eq(DictItem::getDictTypeId, req.getDictTypeId())
                .eq(DictItem::getValue, req.getValue())) > 0;
        AssertUtils.assertTrue(exists, "同一类型下字典值已存在");

        DictItem dictItem = new DictItem();
        BeanUtils.copyProperties(req, dictItem);
        dictItemService.save(dictItem);
        return ResultUtil.success(CustomBeanUtils.copyProperties(dictItem, DictItemVo.class));
    }

    @Operation(summary = "根据ID获取字典项")
    @GetMapping("/get/{id}")
    public ResponseResult<DictItemVo> get(@PathVariable Long id) {
        DictItem dictItem = dictItemService.getById(id);
        return ResultUtil.success(CustomBeanUtils.copyProperties(dictItem, DictItemVo.class));
    }

    @Operation(summary = "字典项分页列表")
    @PostMapping("/list")
    public ResponseResult<IPage<DictItemVo>> list(@RequestBody DictItemPageQuery query) {
        AssertUtils.assertTrue(query.getDictTypeId() == null, "字典类型ID不能为空");
        Page<DictItem> page = new Page<>(query.getPageNum(), query.getPageSize());
        LambdaQueryWrapper<DictItem> wrapper = new LambdaQueryWrapper<DictItem>()
                .eq(DictItem::getDictTypeId, query.getDictTypeId())
                .orderByAsc(DictItem::getSort)
                .orderByDesc(DictItem::getId);
        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(DictItem::getLabel, query.getKeyword())
                    .or()
                    .like(DictItem::getValue, query.getKeyword()));
        }
        IPage<DictItemVo> result = dictItemService.page(page, wrapper)
                .convert(e -> CustomBeanUtils.copyProperties(e, DictItemVo.class));
        return ResultUtil.success(result);
    }

    @Operation(summary = "获取指定类型下全部字典项")
    @GetMapping("/all/{dictTypeId}")
    public ResponseResult<List<DictItemVo>> all(@PathVariable Long dictTypeId) {
        List<DictItemVo> list = dictItemService.list(new LambdaQueryWrapper<DictItem>()
                        .eq(DictItem::getDictTypeId, dictTypeId)
                        .orderByAsc(DictItem::getSort)
                        .orderByDesc(DictItem::getId))
                .stream()
                .map(e -> CustomBeanUtils.copyProperties(e, DictItemVo.class))
                .toList();
        return ResultUtil.success(list);
    }

    @Operation(summary = "更新字典项")
    @PostMapping("/update")
    public ResponseResult<DictItemVo> update(@RequestBody DictItemReq req) {
        AssertUtils.assertTrue(req.getId() == null, "ID不能为空");
        AssertUtils.assertTrue(req.getDictTypeId() == null, "字典类型ID不能为空");
        AssertUtils.assertTrue(!StringUtils.hasText(req.getLabel()), "标签不能为空");
        AssertUtils.assertTrue(!StringUtils.hasText(req.getValue()), "值不能为空");

        boolean exists = dictItemService.count(new LambdaQueryWrapper<DictItem>()
                .eq(DictItem::getDictTypeId, req.getDictTypeId())
                .eq(DictItem::getValue, req.getValue())
                .ne(DictItem::getId, req.getId())) > 0;
        AssertUtils.assertTrue(exists, "同一类型下字典值已存在");

        DictItem dictItem = new DictItem();
        BeanUtils.copyProperties(req, dictItem);
        dictItemService.updateById(dictItem);
        DictItem updated = dictItemService.getById(req.getId());
        return ResultUtil.success(CustomBeanUtils.copyProperties(updated, DictItemVo.class));
    }

    @Operation(summary = "删除字典项")
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> delete(@PathVariable Long id) {
        dictItemService.removeById(id);
        return ResultUtil.success();
    }
}

