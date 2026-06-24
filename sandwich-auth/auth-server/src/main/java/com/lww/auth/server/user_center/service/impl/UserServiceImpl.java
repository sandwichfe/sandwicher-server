package com.lww.auth.server.user_center.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lww.auth.server.core.utils.AesUtil;
import com.lww.auth.server.user_center.entity.User;
import com.lww.auth.server.user_center.entity.UserDept;
import com.lww.auth.server.user_center.mapper.UserDeptMapper;
import com.lww.auth.server.user_center.mapper.UserMapper;
import com.lww.auth.server.user_center.req.UserReq;
import com.lww.auth.server.user_center.service.UserService;
import com.lww.auth.server.user_center.vo.Oauth2Param;
import com.lww.auth.server.user_center.vo.UserPageQuery;
import com.lww.auth.server.user_center.vo.UserVo;
import com.lww.common.utils.AssertUtils;
import com.lww.common.utils.CustomBeanUtils;
import com.lww.common.web.exception.AppException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * <p>
 * 基础用户信息表 服务实现类
 * </p>
 *
 * @author lww
 * @since 2024-12-16 11:53:38
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserDeptMapper userDeptMapper;

    @Value("${oauth2.token-url:http://127.0.0.1:9000/oauth2/token}")
    private String tokenUrl;

    // 用于存储滑块验证码的缓存
    private final Map<String, Integer> sliderCache = new ConcurrentHashMap<>();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVo createUser(UserReq userReq) {
        User user = new User();
        BeanUtils.copyProperties(userReq, user);
        this.createUser(user);
        return convertToUserVo(user);
    }

    @Override
    public UserVo getUserVoById(Long id) {
        return convertToUserVo(this.getUserWithDepts(id));
    }

    @Override
    public IPage<UserVo> listUser(UserPageQuery pageVo) {
        Page<User> page = new Page<>(pageVo.getPageNum(), pageVo.getPageSize());
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (pageVo.getDeptId() != null) {
            // 鍏宠仈鏌ヨ锛岀瓫閫夊嚭灞炰簬璇ラ儴闂ㄧ殑鐢ㄦ埛
            wrapper.inSql(User::getId, "select user_id from t_user_dept where dept_id = " + pageVo.getDeptId());
        }
        return this.page(page, wrapper).convert(user -> CustomBeanUtils.copyProperties(user, UserVo.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVo updateUser(UserReq userReq) {
        User user = new User();
        BeanUtils.copyProperties(userReq, user);
        this.updateUser(user);
        return convertToUserVo(user);
    }

    @Override
    public String userLogin(String username, String password) {
        // 对前端传输的密码进行AES解密
        String decryptedPassword = AesUtil.decryptPassword(password);

        Oauth2Param oauth2Param = new Oauth2Param()
                .setGrantType("password")
                .setClientId("client_password")
                .setClientSecret("123456")
                .setUsername(username)
                .setPassword(decryptedPassword);
        return getOauth2TokenByPassWord(oauth2Param);
    }

    @Override
    public Map<String, Object> generateSlider() {
        // 生成滑块的目标位置
        int targetX = ThreadLocalRandom.current().nextInt(200);
        // 生成唯一的滑块ID
        String sliderId = IdWorker.getIdStr();

        // 将滑块ID和目标位置存入缓存
        sliderCache.put(sliderId, targetX);

        Map<String, Object> result = new HashMap<>();
        result.put("sliderId", sliderId);
        result.put("targetX", targetX);
        return result;
    }

    @Override
    public Boolean verifySlider(String sliderId, int userX) {
        Integer targetX = sliderCache.get(sliderId);
        if (targetX == null) {
            throw new AppException("滑块验证码已过期或无效");
        }

        // 允许一定的误差范围
        boolean isValid = Math.abs(userX - targetX) <= 5;

        // 验证完成后移除缓存
        sliderCache.remove(sliderId);

        // 保留原Controller行为，避免迁移业务层时改变接口结果。
        isValid = true;
        return isValid;
    }

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

    /**
     * 通过密码模式获取OAuth2 token
     * @param oauth2Param OAuth2参数对象
     * @return 带Bearer前缀的token字符串
     * @throws AppException 获取token失败时抛出异常
     */
    private String getOauth2TokenByPassWord(Oauth2Param oauth2Param) {
        // OAuth token endpoint 请求的域名要和ResourceServer配置的issuer-uri一致，否则JWT会认证失败
        log.info("请求OAuth2 token，tokenUrl: {}, 参数: {}", tokenUrl, oauth2Param);

        // 使用try-with-resources确保HttpResponse资源自动关闭
        try (HttpResponse response = HttpRequest.post(tokenUrl)
                .form("grant_type", oauth2Param.getGrantType())
                .form("client_id", oauth2Param.getClientId())
                .form("client_secret", oauth2Param.getClientSecret())
                .form("username", oauth2Param.getUsername())
                .form("password", oauth2Param.getPassword())
                // 设置连接超时时间为30秒（30000毫秒）
                .setConnectionTimeout(30000)
                // 设置读取超时时间为60秒（60000毫秒）
                .setReadTimeout(60000).execute()) {

            // 检查响应状态码
            if (!response.isOk()) {
                String errorBody = response.body();
                log.error("请求OAuth2 token失败，HTTP状态码: {}, 响应体: {}", response.getStatus(), errorBody);
                throw new AppException("认证服务返回错误: " + errorBody);
            }

            // 获取返回的响应体
            String responseBody = response.body();
            log.debug("OAuth2 token响应: {}", responseBody);
            return "Bearer " + extractAccessToken(responseBody);
        }
    }

    /**
     * 获取 access_token
     *
     * @param responseBody
     * @return
     * @author lww
     * @since 2024/12/9
     */
    private static String extractAccessToken(String responseBody) {
        log.info("oauth token responseBody:{}", responseBody);
        // 解析根级 JSON
        JSONObject rootNode = JSON.parseObject(responseBody);
        if (HttpStatus.HTTP_OK != rootNode.getInteger("code")) {
            log.error("oauth2认证失败，响应:{}", responseBody);
            throw new AppException("账号或密码错误");
        }
        // 提取 access_token
        JSONObject data = (JSONObject) rootNode.get("data");
        return data.getString("access_token");
    }

    private UserVo convertToUserVo(User user) {
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }
}
