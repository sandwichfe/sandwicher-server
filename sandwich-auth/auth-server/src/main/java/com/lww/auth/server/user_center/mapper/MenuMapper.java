package com.lww.auth.server.user_center.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lww.auth.server.user_center.entity.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 系统菜单表 Mapper 接口
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    List<Menu> selectMenusByUserId(@Param("userId") Long userId);
}
