package com.lww.auth.server.portal.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.auth.server.portal.config.api.ApiPortalRestController;
import com.lww.auth.server.portal.req.DictItemReq;
import com.lww.auth.server.portal.service.DictItemService;
import com.lww.auth.server.portal.vo.DictItemPageQuery;
import com.lww.auth.server.portal.vo.DictItemVo;
import com.lww.common.web.log.Loggable;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "字典项管理")
@ApiPortalRestController
@RequestMapping("/sys/dict-item")
public class DictItemController {

    @Resource
    private DictItemService dictItemService;

    @Operation(summary = "新增字典项")
    @Loggable(module = "dictItem", type = "create", description = "新增字典项", logResult = false)
    @PostMapping("/create")
    public ResponseResult<DictItemVo> create(@RequestBody DictItemReq req) {
        return ResultUtil.success(dictItemService.createDictItem(req));
    }

    @Operation(summary = "根据ID获取字典项")
    @GetMapping("/get/{id}")
    public ResponseResult<DictItemVo> get(@PathVariable Long id) {
        return ResultUtil.success(dictItemService.getDictItemById(id));
    }

    @Operation(summary = "字典项分页列表")
    @PostMapping("/list")
    public ResponseResult<IPage<DictItemVo>> list(@RequestBody DictItemPageQuery query) {
        return ResultUtil.success(dictItemService.listDictItem(query));
    }

    @Operation(summary = "获取指定类型下全部字典项")
    @GetMapping("/all/{dictTypeId}")
    public ResponseResult<List<DictItemVo>> all(@PathVariable Long dictTypeId) {
        return ResultUtil.success(dictItemService.listAllByDictTypeId(dictTypeId));
    }

    @Operation(summary = "更新字典项")
    @Loggable(module = "dictItem", type = "update", description = "更新字典项", logResult = false)
    @PostMapping("/update")
    public ResponseResult<DictItemVo> update(@RequestBody DictItemReq req) {
        return ResultUtil.success(dictItemService.updateDictItem(req));
    }

    @Operation(summary = "删除字典项")
    @Loggable(module = "dictItem", type = "delete", description = "删除字典项ID: #id", logParams = false)
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> delete(@PathVariable Long id) {
        dictItemService.deleteDictItem(id);
        return ResultUtil.success();
    }
}
