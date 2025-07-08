package com.lww.littlenote.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lww.littlenote.entity.TodoTask;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 统一任务表 Mapper 接口
 * </p>
 *
 * @author lww
 * @since 2025-01-06
 */
@Mapper
public interface TodoTaskMapper extends BaseMapper<TodoTask> {

}