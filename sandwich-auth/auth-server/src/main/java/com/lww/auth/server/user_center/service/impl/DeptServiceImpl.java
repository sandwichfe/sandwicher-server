package com.lww.auth.server.user_center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.user_center.entity.Dept;
import com.lww.auth.server.user_center.mapper.DeptMapper;
import com.lww.auth.server.user_center.service.DeptService;
import com.lww.auth.server.user_center.vo.DeptTreeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 部门表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-12-16
 */
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements DeptService {

    @Override
    public List<DeptTreeVO> getDeptTree() {
        // Get all depts
        List<Dept> depts = this.list(new LambdaQueryWrapper<Dept>()
                .eq(Dept::getDeleted, false)
                .orderByAsc(Dept::getSort));

        // Convert to VO
        List<DeptTreeVO> allVos = depts.stream().map(this::convertToVo).collect(Collectors.toList());

        // Build tree
        return buildTree(allVos);
    }

    private DeptTreeVO convertToVo(Dept dept) {
        DeptTreeVO vo = new DeptTreeVO();
        BeanUtils.copyProperties(dept, vo);
        if (dept.getCreateTime() != null) {
            vo.setCreateTime(dept.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        }
        return vo;
    }

    private List<DeptTreeVO> buildTree(List<DeptTreeVO> allVos) {
        List<DeptTreeVO> roots = new ArrayList<>();
        for (DeptTreeVO vo : allVos) {
            if (vo.getParentId() == null || vo.getParentId() == 0) {
                roots.add(vo);
            }
            for (DeptTreeVO child : allVos) {
                if (child.getParentId() != null && child.getParentId().equals(vo.getId())) {
                    if (vo.getChildren() == null) {
                        vo.setChildren(new ArrayList<>());
                    }
                    vo.getChildren().add(child);
                }
            }
        }
        return roots;
    }
}
