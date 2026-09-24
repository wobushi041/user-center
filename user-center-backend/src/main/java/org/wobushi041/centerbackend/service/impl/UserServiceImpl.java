package org.wobushi041.centerbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.wobushi041.centerbackend.common.ErrorCode;
import org.wobushi041.centerbackend.constant.UserConstant;
import org.wobushi041.centerbackend.exception.BusinessException;
import org.wobushi041.centerbackend.mapper.UserMapper;
import org.wobushi041.centerbackend.model.enity.User;
import org.wobushi041.centerbackend.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于 MyBatis-Plus 实现用户认证和信息管理
 *
 * @author wobushi041
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    /**
     * 注入用户数据访问依赖
     */
    @Resource
    private UserMapper userMapper;

    /// 用户认证业务 ///

    /**
     * 校验注册信息、使用 MD5 加密密码并通过 MyBatis-Plus 保存用户
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @return 新用户 id
     */
    @Override
    public Long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 检查参数是否为空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        // 检查用户账号长度
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能小于4位");
        }

        // 检查用户密码长度
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码不能小于8位");
        }

        // 检查用户账号是否包含特殊字符
        String validateRegExp = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validateRegExp).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能包含特殊字符");
        }

        // 检查两次密码是否一致
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次密码不一致");
        }

        // 检查账号是否已存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }

        // 使用 MD5 加密密码并保存用户
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "注册失败");
        }
        return user.getId();
    }

    /**
     * 使用加密密码查询用户并将脱敏信息写入 HTTP Session
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request      HTTP 请求对象
     * @return 脱敏后的登录用户
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 检查登录参数
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能小于4位");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码不能小于8位");
        }

        // 检查用户账号是否包含特殊字符
        String validateRegExp = "[`~!@#$%^&*()+=|{}':;',\\\\\\\\[\\\\\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]\"";
        Matcher matcher = Pattern.compile(validateRegExp).matcher(userAccount);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号不能包含特殊字符");
        }

        // 查询账号和加密密码匹配的用户
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);

        // 检查用户是否存在
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }

        // 保存脱敏用户至会话
        User safetyUser = getSafetyUser(user);
        request.getSession().setAttribute(USER_LOGIN_STATE, safetyUser);
        return safetyUser;
    }

    /// 用户信息业务 ///

    /**
     * 根据会话用户 id 查询数据库并返回最新脱敏信息
     *
     * @param currentUser 当前会话用户
     * @return 当前用户的最新脱敏信息
     */
    @Override
    public User getCurrentUser(User currentUser) {
        // 检查当前用户
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NO_AUTHORITY);
        }

        // 查询并脱敏最新用户信息
        long userId = currentUser.getId();
        User user = this.getById(userId);
        return this.getSafetyUser(user);
    }

    /**
     * 从 HTTP Session 中移除用户登录状态
     *
     * @param request HTTP 请求对象
     * @return 注销结果
     */
    @Override
    public int userlogout(HttpServletRequest request) {
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return 1;
    }

    /**
     * 复制允许返回客户端的用户字段以移除密码等敏感信息
     *
     * @param originUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    @Override
    public User getSafetyUser(User originUser) {
        User safetyUser = new User();
        safetyUser.setId(originUser.getId());
        safetyUser.setUsername(originUser.getUsername());
        safetyUser.setUserAccount(originUser.getUserAccount());
        safetyUser.setAvatarUrl(originUser.getAvatarUrl());
        safetyUser.setGender(originUser.getGender());
        safetyUser.setPhone(originUser.getPhone());
        safetyUser.setEmail(originUser.getEmail());
        safetyUser.setUserRole(originUser.getUserRole());
        safetyUser.setUserStatus(originUser.getUserStatus());
        safetyUser.setCreateTime(originUser.getCreateTime());
        return safetyUser;
    }

}
