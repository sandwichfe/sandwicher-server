package com.lww.auth.server.user_center.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.core.utils.AesUtil;
import com.lww.auth.server.user_center.entity.User;
import com.lww.auth.server.user_center.entity.UserDept;
import com.lww.auth.server.user_center.mapper.UserDeptMapper;
import com.lww.auth.server.user_center.mapper.UserMapper;
import com.lww.auth.server.user_center.service.UserService;
import com.lww.common.utils.AssertUtils;
import jakarta.annotation.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 基础用户信息表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:38
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserDeptMapper userDeptMapper;

    @Override
    public User getUserByUserName(String username) {
        // 根据账号查询用户
        return baseMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getUsername, username));
    }

    @Override
    public void registerUser(User user) {
        String username = user.getUsername();
        String password = user.getPassword();

        AssertUtils.assertTrue(!StringUtils.hasText(username), "用户名不能为空");
        AssertUtils.assertTrue(!StringUtils.hasText(password), "密码不能为空");

        // 此用户名是否已存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username",username);
        boolean existsUserName = baseMapper.exists(wrapper);
        AssertUtils.assertTrue(existsUserName, "用户名已存在");

        // add
        User addUser = new User();
        addUser.setUsername(username);
        addUser.setPassword(new BCryptPasswordEncoder().encode(password));
        baseMapper.insert(addUser);

    }

    @Override
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        // 获取用户信息
        User user = baseMapper.selectById(userId);
        AssertUtils.assertTrue(user == null, "用户不存在");

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // 解密前端传来的密码
        String decryptedOldPassword = AesUtil.decryptPassword(oldPassword);
        String decryptedNewPassword = AesUtil.decryptPassword(newPassword);
        
        // 验证原密码
        boolean matches = passwordEncoder.matches(decryptedOldPassword, user.getPassword());
        AssertUtils.assertTrue(!matches, "原密码错误");
        
        // 更新密码
        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setPassword(passwordEncoder.encode(decryptedNewPassword));
        
        return baseMapper.updateById(updateUser) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createUser(User user) {
        if (StringUtils.hasText(user.getPassword())) {
            user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }
        this.save(user);
        saveUserDepts(user.getId(), user.getDeptIds());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(User user) {
        this.updateById(user);
        // 删除原有部门关联
        userDeptMapper.delete(new LambdaQueryWrapper<UserDept>().eq(UserDept::getUserId, user.getId()));
        // 保存新的部门关联
        saveUserDepts(user.getId(), user.getDeptIds());
    }

    private void saveUserDepts(Long userId, List<Long> deptIds) {
        if (deptIds != null && !deptIds.isEmpty()) {
            for (Long deptId : deptIds) {
                UserDept userDept = new UserDept();
                userDept.setUserId(userId);
                userDept.setDeptId(deptId);
                userDeptMapper.insert(userDept);
            }
        }
    }

    @Override
    public User getUserWithDepts(Long id) {
        User user = this.getById(id);
        if (user != null) {
            List<UserDept> userDepts = userDeptMapper.selectList(new LambdaQueryWrapper<UserDept>().eq(UserDept::getUserId, id));
            user.setDeptIds(userDepts.stream().map(UserDept::getDeptId).collect(Collectors.toList()));
        }
        return user;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        this.removeById(id);
        userDeptMapper.delete(new LambdaQueryWrapper<UserDept>().eq(UserDept::getUserId, id));
    }
}
