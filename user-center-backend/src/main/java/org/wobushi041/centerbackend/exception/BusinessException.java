package org.wobushi041.centerbackend.exception;

import org.wobushi041.centerbackend.common.ErrorCode;

/**
 * 表示可预期的业务异常
 *
 * @author wobushi041
 */
public class BusinessException extends RuntimeException {

    /**
     * 业务错误状态码
     */
    private final int code;

    /**
     * 业务错误详情
     */
    private final String description;

    /**
     * 使用自定义错误信息构造业务异常
     *
     * @param message     错误消息
     * @param code        错误状态码
     * @param description 错误详情
     */
    public BusinessException(String message, int code, String description) {
        super(message);
        this.code = code;
        this.description = description;
    }

    /**
     * 使用错误码和自定义详情构造业务异常
     *
     * @param errorCode   错误码
     * @param description 错误详情
     */
    public BusinessException(ErrorCode errorCode, String description) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = description;
    }

    /**
     * 根据错误码构造业务异常
     *
     * @param errorCode 错误码
     */
    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
        this.description = errorCode.getDescription();
    }

    /**
     * 获取业务错误状态码
     *
     * @return 业务错误状态码
     */
    public int getCode() {
        return code;
    }

    /**
     * 获取业务错误详情
     *
     * @return 业务错误详情
     */
    public String getDescription() {
        return description;
    }

    /// 序列化字段 ///

    /**
     * 序列化版本号
     */
    private static final long serialVersionUID = 1L;

}
