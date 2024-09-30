package com.lww.security.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.security.entity.LoginUser;
import com.lww.security.mapper.LoginUserMapper;
import com.lww.common.web.response.ResponseResult;
import com.lww.common.web.response.ResultUtil;
import com.lww.security.service.LoginUserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;

/**
 *  
 * @author lww
 * @since 2022/7/20 14:59
 */
@Service
public class LoginUserServiceImpl extends ServiceImpl<LoginUserMapper, LoginUser> implements LoginUserService {

    @Resource
    private LoginUserMapper loginUserMapper;

    @Override
    public LoginUser getUserByUserName(String username) {
        if (!StringUtils.hasLength(username)) {
            return null;
        }
        // 模拟存在数据库里面的密码是加密过后的
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        //if ("aaa".equals(username)) {
        //    return new User("aaa", passwordEncoder.encode("aaa"));
        //}
        // 查询此用户名
        QueryWrapper<LoginUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        LoginUser user = loginUserMapper.selectOne(wrapper);
        return user;
    }

    /**
     * 注册
     * @author lww
     * @since 2023/8/16 13:59
     * @param user
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseResult registerUser(LoginUser user) {
        String username = user.getUsername();
        String password = user.getPassword();
        if (!StringUtils.hasText(username)){
            return ResultUtil.error("用户名不能为空！");
        }
        if (!StringUtils.hasText(password)){
            return ResultUtil.error("密码不能为空！");
        }
        // 此用户名是否已存在
        QueryWrapper<LoginUser> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        boolean existsUserName = loginUserMapper.exists(wrapper);
        if (existsUserName){
            return ResultUtil.error("用户名已存在！");
        }
        LoginUser addUser = new LoginUser();
        addUser.setUsername(username);
        addUser.setPassword(new BCryptPasswordEncoder().encode(password));
        loginUserMapper.insert(addUser);
        return ResultUtil.success("注册成功！");
    }
}
