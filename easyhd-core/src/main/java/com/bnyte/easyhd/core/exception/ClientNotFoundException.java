package com.bnyte.easyhd.core.exception;

/**
 * @author bnyte
 * @since 1.0.0
 */
public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException() {
        super("未找到合适的Hadoop客户端");
    }

    public ClientNotFoundException(String message) {
        super(message);
    }

    public ClientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientNotFoundException(Throwable cause) {
        super(cause);
    }

    public ClientNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
