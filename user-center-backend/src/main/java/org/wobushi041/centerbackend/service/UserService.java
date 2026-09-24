package org.wobushi041.centerbackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.wobushi041.centerbackend.model.enity.User;

import javax.servlet.http.HttpServletRequest;

/**
 * 定义用户认证和信息管理业务契约
 *
 * @author wobushi041
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录状态 Session 属性名
     */
    String USER_LOGIN_STATE = "userLoginState";

    /// 用户认证业务 ///

    /**
     * 注册用户账号
     *
     * @param userAccount   用户账号
     * @param userPassword  用户密码
     * @param checkPassword 确认密码
     * @return 新用户 id
     */
    Long userRegister(String userAccount, String userPassword, String checkPassword);

    /**
     * 校验账号凭据并登录用户
     *
     * @param userAccount  用户账号
     * @param userPassword 用户密码
     * @param request      HTTP 请求对象
     * @return 脱敏后的登录用户
     */
    User userLogin(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 注销当前用户会话
     *
     * @param request HTTP 请求对象
     * @return 注销结果
     */
    int userlogout(HttpServletRequest request);

    /// 用户信息业务 ///

    /**
     * 获取用户脱敏信息
     *
     * @param originUser 原始用户信息
     * @return 脱敏后的用户信息
     */
    User getSafetyUser(User originUser);

    /**
     * 获取当前登录用户信息
     *
     * @param currentUser 当前会话用户
     * @return 当前用户的最新脱敏信息
     */
    User getCurrentUser(User currentUser);

}
