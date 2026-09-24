package org.wobushi041.centerbackend.common;

/**
 * 提供统一接口响应构造方法
 *
 * @author wobushi041
 */
public class ResultUtils {

    /**
     * 构造成功响应
     *
     * @param data 响应数据
     * @param <T>  响应数据类型
     * @return 成功响应对象
     */
    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(200, data, "success", "木有问题嘿嘿");
    }

    /**
     * 根据错误码构造错误响应
     *
     * @param errorCode 错误码
     * @param <T>       响应数据类型
     * @return 错误响应对象
     */
    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode);
    }

    /**
     * 根据错误码和错误详情构造错误响应
     *
     * @param errorCode  错误码
     * @param description 错误详情
     * @return 错误响应对象
     */
    public static BaseResponse error(ErrorCode errorCode, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, errorCode.getMessage(), description);
    }

    /**
     * 根据错误码、错误消息和错误详情构造错误响应
     *
     * @param errorCode   错误码
     * @param message     错误消息
     * @param description 错误详情
     * @return 错误响应对象
     */
    public static BaseResponse error(ErrorCode errorCode, String message, String description) {
        return new BaseResponse<>(errorCode.getCode(), null, message, description);
    }

    /**
     * 根据自定义错误信息构造错误响应
     *
     * @param code        错误状态码
     * @param message     错误消息
     * @param description 错误详情
     * @return 错误响应对象
     */
    public static BaseResponse error(int code, String message, String description) {
        return new BaseResponse<>(code, null, message, description);
    }

}
