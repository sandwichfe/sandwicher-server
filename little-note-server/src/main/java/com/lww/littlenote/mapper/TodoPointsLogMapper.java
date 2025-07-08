package com.lww.littlenote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lww.littlenote.entity.TodoPointsLog;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 积分变动记录表 Mapper 接口
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Mapper
public interface TodoPointsLogMapper extends BaseMapper<TodoPointsLog> {

}