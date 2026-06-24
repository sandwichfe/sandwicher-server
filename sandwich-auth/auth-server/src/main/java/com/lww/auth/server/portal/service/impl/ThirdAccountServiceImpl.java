package com.lww.auth.server.portal.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.portal.entity.ThirdAccount;
import com.lww.auth.server.portal.mapper.ThirdAccountMapper;
import com.lww.auth.server.portal.service.ThirdAccountService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 三方登录账户信息表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:37
 */
@Service
public class ThirdAccountServiceImpl extends ServiceImpl<ThirdAccountMapper, ThirdAccount> implements ThirdAccountService {

}
