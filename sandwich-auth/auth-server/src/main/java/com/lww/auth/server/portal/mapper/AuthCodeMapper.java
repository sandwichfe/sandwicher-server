package com.lww.auth.server.portal.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lww.auth.server.portal.entity.AuthCode;
import org.apache.ibatis.annotations.Mapper;

/**
 * 授权码Mapper接口
 *
 * @author lww
 * @since 2026/06/30
 */
@Mapper
public interface AuthCodeMapper extends BaseMapper<AuthCode> {
}
