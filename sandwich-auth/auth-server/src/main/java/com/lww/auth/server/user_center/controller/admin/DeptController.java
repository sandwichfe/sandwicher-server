package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lww.auth.server.user_center.req.DeptReq;
import com.lww.auth.server.user_center.service.DeptService;
import com.lww.auth.server.user_center.vo.DeptTreeVO;
import com.lww.auth.server.user_center.vo.DeptVo;
import com.lww.common.web.log.Loggable;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.web.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author lww
 */
@Tag(name = "部门管理")
@RestController
@RequestMapping("/api/sys/dept")
public class DeptController {

    @Resource
    private DeptService deptService;

    @Operation(summary = "新增部门")
    @Loggable(module = "dept", type = "create", description = "新增部门", logResult = false)
    @PostMapping("/create")
    public ResponseResult<DeptVo> createDept(@RequestBody DeptReq deptReq) {
        return ResultUtil.success(deptService.createDept(deptReq));
    }

    @Operation(summary = "根据ID获取部门")
    @GetMapping("/get/{id}")
    public ResponseResult<DeptVo> getDeptById(@PathVariable Long id) {
        return ResultUtil.success(deptService.getDeptById(id));
    }

    @Operation(summary = "获取所有部门")
    @PostMapping("/list")
    public ResponseResult<IPage<DeptVo>> listDept(@RequestBody PageVo pageVo) {
        return ResultUtil.success(deptService.listDept(pageVo));
    }

    @Operation(summary = "更新部门")
    @Loggable(module = "dept", type = "update", description = "更新部门", logResult = false)
    @PostMapping("/update")
    public ResponseResult<DeptVo> updateDept(@RequestBody DeptReq deptReq) {
        return ResultUtil.success(deptService.updateDept(deptReq));
    }

    @Operation(summary = "删除部门")
    @Loggable(module = "dept", type = "delete", description = "删除部门ID: #id", logParams = false)
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> deleteDept(@PathVariable Long id) {
        deptService.deleteDept(id);
        return ResultUtil.success();
    }

    @Operation(summary = "获取部门树")
    @GetMapping("/tree")
    public ResponseResult<List<DeptTreeVO>> getDeptTree() {
        return ResultUtil.success(deptService.getDeptTree());
    }
}
