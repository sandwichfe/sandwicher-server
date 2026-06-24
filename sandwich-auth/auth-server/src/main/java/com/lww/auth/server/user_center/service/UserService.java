package com.lww.auth.server.user_center.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lww.auth.server.user_center.entity.User;
import com.lww.auth.server.user_center.req.UserReq;
import com.lww.auth.server.user_center.vo.UserPageQuery;
import com.lww.auth.server.user_center.vo.UserVo;

import java.util.Map;

/**
 * <p>
 * 基础用户信息表 服务类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:38
 */
public interface UserService extends IService<User> {

    UserVo createUser(UserReq userReq);

    UserVo getUserVoById(Long id);

    IPage<UserVo> listUser(UserPageQuery pageVo);

    UserVo updateUser(UserReq userReq);

    /**
     * 用户登录
     * @param username 用户名
     * @param password 前端加密后的密码
     * @return 带Bearer前缀的token字符串
     */
    String userLogin(String username, String password);

    /**
     * 生成滑块验证码
     * @return 滑块验证码信息
     */
    Map<String, Object> generateSlider();

    /**
     * 验证滑块位置
     * @param sliderId 滑块ID
     * @param userX 用户拖动位置
     * @return 是否验证通过
     */
    Boolean verifySlider(String sliderId, int userX);

    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getUserByUserName(String username);

    /**
     * 注册
     * @author lww
     * @since 2023/8/16 13:59
     * @param user 用户信息
     */
    void registerUser(User user);

    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    /**
     * 修改密码
     * @param userId 用户ID
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 是否修改成功
     */
    boolean changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 创建用户（包含部门关联）
     * @param user 用户信息
     */
    void createUser(User user);

    /**
     * 更新用户（包含部门关联）
     * @param user 用户信息
     */
    void updateUser(User user);

    /**
     * 获取用户详情（包含部门关联）
     * @param id 用户ID
     * @return 用户信息
     */
    User getUserWithDepts(Long id);

    /**
     * 删除用户（包含部门关联）
     * @param id 用户ID
     */
    void deleteUser(Long id);
}
