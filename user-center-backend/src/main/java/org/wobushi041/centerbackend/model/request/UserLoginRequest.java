package org.wobushi041.centerbackend.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装用户登录请求参数
 *
 * @author wobushi041
 */
@Data
public class UserLoginRequest implements Serializable {

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户密码
     */
    private String userPassword;

    /// 序列化字段 ///

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 3191241716373120793L;

}
