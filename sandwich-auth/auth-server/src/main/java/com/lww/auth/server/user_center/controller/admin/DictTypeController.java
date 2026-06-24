package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.auth.server.user_center.req.DictTypeReq;
import com.lww.auth.server.user_center.service.DictTypeService;
import com.lww.auth.server.user_center.vo.DictTypePageQuery;
import com.lww.auth.server.user_center.vo.DictTypeVo;
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
        return ResultUtil.success(dictTypeService.createDictType(req));
    }

    @Operation(summary = "根据ID获取字典类型")
    @GetMapping("/get/{id}")
    public ResponseResult<DictTypeVo> get(@PathVariable Long id) {
        return ResultUtil.success(dictTypeService.getDictTypeById(id));
    }

    @Operation(summary = "字典类型分页列表")
    @PostMapping("/list")
    public ResponseResult<IPage<DictTypeVo>> list(@RequestBody DictTypePageQuery query) {
        return ResultUtil.success(dictTypeService.listDictType(query));
    }

    @Operation(summary = "获取全部字典类型")
    @GetMapping("/all")
    public ResponseResult<List<DictTypeVo>> all() {
        return ResultUtil.success(dictTypeService.listAllDictType());
    }

    @Operation(summary = "更新字典类型")
    @Loggable(module = "dictType", type = "update", description = "更新字典类型", logResult = false)
    @PostMapping("/update")
    public ResponseResult<DictTypeVo> update(@RequestBody DictTypeReq req) {
        return ResultUtil.success(dictTypeService.updateDictType(req));
    }

    @Operation(summary = "删除字典类型（同时删除其字典项）")
    @Loggable(module = "dictType", type = "delete", description = "删除字典类型ID: #id", logParams = false)
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> delete(@PathVariable Long id) {
        dictTypeService.deleteType(id);
        return ResultUtil.success();
    }
}
