package com.lww.auth.server.user_center.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.user_center.entity.Dept;
import com.lww.auth.server.user_center.vo.DeptTreeVO;

import java.util.List;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author lww
 * @since 2024-12-16
 */
public interface DeptService extends IService<Dept> {

    /**
     * 获取部门树
     * @return List<DeptTreeVO>
     */
    List<DeptTreeVO> getDeptTree();
}
