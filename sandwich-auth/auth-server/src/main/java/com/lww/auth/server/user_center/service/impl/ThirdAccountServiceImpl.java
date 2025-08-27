package com.lww.auth.server.user_center.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.user_center.entity.ThirdAccount;
import com.lww.auth.server.user_center.mapper.ThirdAccountMapper;
import com.lww.auth.server.user_center.service.ThirdAccountService;
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
