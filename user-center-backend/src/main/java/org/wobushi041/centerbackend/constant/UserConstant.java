package org.wobushi041.centerbackend.constant;

/**
 * 定义用户业务常量
 *
 * @author wobushi041
 */
public interface UserConstant {

    /**
     * 用户登录状态 Session 属性名
     */
    String USER_LOGIN_STATE = "userLoginState";

    /**
     * 默认用户角色
     */
    int DEFAULT_ROLE = 0;

    /**
     * 管理员角色
     */
    int ADMIN_ROLE = 1;

    /**
     * 密码盐值
     */
    String SALT = "041";

}
