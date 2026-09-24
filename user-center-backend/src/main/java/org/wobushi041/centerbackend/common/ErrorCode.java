package org.wobushi041.centerbackend.common;

import lombok.Getter;

/**
 * 定义统一业务错误码
 *
 * @author wobushi041
 */
@Getter
public enum ErrorCode {

    /**
     * 操作成功
     */
    SUCCESS(0, "success", ""),

    /**
     * 请求参数错误
     */
    PARAMS_ERROR(4000, "请求参数错误", ""),

    /**
     * 请求参数为空
     */
    NULL_ERROR(4001, "请求参数为空", ""),

    /**
     * 用户未登录
     */
    NO_LOGIN(40100, "未登录", ""),

    /**
     * 用户无操作权限
     */
    NO_AUTHORITY(40101, "无权限", ""),

    /**
     * 系统内部异常
     */
    SYSTEM_ERROR(50000, "系统内部异常", ""),

    /**
     * 用户登录状态无效
     */
    NOT_LOGIN(40102, "未登录", "")
    ;

    /**
     * 错误状态码
     */
    private final int code;

    /**
     * 错误消息
     */
    private final String message;

    /**
     * 错误详情
     */
    private final String description;

    /**
     * 构造错误码枚举项
     *
     * @param code        错误状态码
     * @param message     错误消息
     * @param description 错误详情
     */
    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

}
