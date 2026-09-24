package org.wobushi041.centerbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.wobushi041.centerbackend.common.BaseResponse;
import org.wobushi041.centerbackend.common.ErrorCode;
import org.wobushi041.centerbackend.common.ResultUtils;
import org.wobushi041.centerbackend.exception.BusinessException;
import org.wobushi041.centerbackend.model.enity.User;
import org.wobushi041.centerbackend.model.request.UserLoginRequest;
import org.wobushi041.centerbackend.model.request.UserRegisterRequest;
import org.wobushi041.centerbackend.service.UserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static org.wobushi041.centerbackend.constant.UserConstant.ADMIN_ROLE;
import static org.wobushi041.centerbackend.service.UserService.USER_LOGIN_STATE;

/**
 * 提供用户注册、登录、注销和管理接口
 *
 * @author wobushi041
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 注入用户服务依赖
     */
    @Resource
    private UserService userService;

    /// 认证相关接口 ///

    /**
     * 注册用户账号
     *
     * @param userRegisterRequest 用户注册请求
     * @return 新用户 id 响应
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 检查请求参数
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 提取注册信息
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();

        // 检查必要参数
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }

        // 执行用户注册
        Long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 登录用户账号并建立会话
     *
     * @param userLoginRequest 用户登录请求
     * @param request          HTTP 请求对象
     * @return 登录用户信息响应
     */
    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 检查请求参数
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 提取登录信息
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();

        // 检查必要参数
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }

        // 执行用户登录
        User user = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(user);
    }

    /**
     * 注销当前用户会话
     *
     * @param request HTTP 请求对象
     * @return 注销结果响应
     */
    @PostMapping("/logout")
    public BaseResponse<Integer> userLogout(HttpServletRequest request) {
        return ResultUtils.success(userService.userlogout(request));
    }

    /// 用户管理接口 ///

    /**
     * 查询符合用户名条件的用户
     *
     * @param username 用户名查询条件
     * @param request  HTTP 请求对象
     * @return 用户列表
     */
    @GetMapping("/search")
    public List<User> searchUser(String username, HttpServletRequest request) {
        // 校验管理员权限
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObject == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User user = (User) userObject;
        if (user.getUserRole() != ADMIN_ROLE) {
            throw new BusinessException(ErrorCode.NO_AUTHORITY, "你不是管理员，无权限");
        }

        // 构建用户查询条件
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like("username", username);
        }
        List<User> searchlist = userService.list(queryWrapper);
        return searchlist;
    }

    /**
     * 删除指定用户
     *
     * @param id      用户 id
     * @param request HTTP 请求对象
     * @return 是否删除成功
     */
    @GetMapping("/delete")
    public boolean deleteUser(@RequestParam long id, HttpServletRequest request) {
        // 校验管理员权限
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObject == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }
        User user = (User) userObject;
        if (user.getUserRole() != ADMIN_ROLE) {
            throw new BusinessException(ErrorCode.NO_AUTHORITY, "你不是管理员，无权限");
        }

        // 校验用户 id
        if (id <= 0) {
            return false;
        }
        return userService.removeById(id);
    }

    /**
     * 获取当前登录用户信息
     *
     * @param request HTTP 请求对象
     * @return 当前登录用户信息响应
     */
    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        // 检查请求对象
        if (request == null) {
            throw new BusinessException(ErrorCode.NULL_ERROR);
        }

        // 读取并返回当前用户
        Object userObject = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObject;
        User safetyUser = userService.getCurrentUser(currentUser);
        return ResultUtils.success(safetyUser);
    }

}
