package org.wobushi041.centerbackend.common;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * 封装统一接口响应数据
 *
 * @author wobushi041
 */
@Data
@Slf4j
public class BaseResponse<T> implements Serializable {

    /**
     * 响应状态码
     */
    private int code;

    /**
     * 响应数据
     */
    private T data;

    /**
     * 响应消息
     */
    private String message;

    /**
     * 响应详情
     */
    private String description;

    /**
     * 使用完整响应信息构造响应对象
     *
     * @param code        响应状态码
     * @param data        响应数据
     * @param message     响应消息
     * @param description 响应详情
     */
    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    /**
     * 使用状态码、数据和消息构造响应对象
     *
     * @param code    响应状态码
     * @param data    响应数据
     * @param message 响应消息
     */
    public BaseResponse(int code, T data, String message) {
        this(code, data, message, "");
    }

    /**
     * 使用状态码和数据构造响应对象
     *
     * @param code 响应状态码
     * @param data 响应数据
     */
    public BaseResponse(int code, T data) {
        this(code, data, "", "");
    }

    /**
     * 根据错误码构造响应对象
     *
     * @param errorCode 错误码
     */
    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage(), errorCode.getDescription());
    }

    /// 序列化字段 ///

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 1L;

}
