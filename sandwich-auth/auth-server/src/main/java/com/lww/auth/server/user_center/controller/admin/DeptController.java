package com.lww.auth.server.user_center.controller.admin;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lww.auth.server.user_center.entity.Dept;
import com.lww.auth.server.user_center.req.DeptReq;
import com.lww.auth.server.user_center.service.DeptService;
import com.lww.auth.server.user_center.vo.DeptTreeVO;
import com.lww.auth.server.user_center.vo.DeptVo;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.common.web.vo.PageVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    @PostMapping("/create")
    public ResponseResult<DeptVo> createDept(@RequestBody DeptReq deptReq) {
        Dept dept = new Dept();
        BeanUtils.copyProperties(deptReq, dept);
        dept.setParentId(Optional.ofNullable(dept.getParentId()).orElse(0L));
        deptService.save(dept);
        DeptVo deptVo = new DeptVo();
        BeanUtils.copyProperties(dept, deptVo);
        return ResultUtil.success(deptVo);
    }

    @Operation(summary = "根据ID获取部门")
    @GetMapping("/get/{id}")
    public ResponseResult<DeptVo> getDeptById(@PathVariable Long id) {
        Dept dept = deptService.getById(id);
        DeptVo deptVo = new DeptVo();
        BeanUtils.copyProperties(dept, deptVo);
        return ResultUtil.success(deptVo);
    }

    @Operation(summary = "获取所有部门")
    @PostMapping("/list")
    public ResponseResult<IPage<DeptVo>> listDept(@RequestBody PageVo pageVo) {
        Page<Dept> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        Page<Dept> depts = deptService.page(page);
        IPage<DeptVo> deptVos = depts.convert(dept -> CustomBeanUtils.copyProperties(dept, DeptVo.class));
        return ResultUtil.success(deptVos);
    }

    @Operation(summary = "更新部门")
    @PostMapping("/update")
    public ResponseResult<DeptVo> updateDept(@RequestBody DeptReq deptReq) {
        Dept dept = new Dept();
        BeanUtils.copyProperties(deptReq, dept);
        deptService.updateById(dept);
        DeptVo deptVo = new DeptVo();
        BeanUtils.copyProperties(dept, deptVo);
        return ResultUtil.success(deptVo);
    }

    @Operation(summary = "删除部门")
    @DeleteMapping("/delete/{id}")
    public ResponseResult<Void> deleteDept(@PathVariable Long id) {
        deptService.removeById(id);
        return ResultUtil.success();
    }

    @Operation(summary = "获取部门树")
    @GetMapping("/tree")
    public ResponseResult<List<DeptTreeVO>> getDeptTree() {
        return ResultUtil.success(deptService.getDeptTree());
    }
}
