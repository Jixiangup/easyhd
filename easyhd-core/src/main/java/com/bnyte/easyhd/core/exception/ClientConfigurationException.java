package com.bnyte.easyhd.core.exception;

/**
 * @author bnyte
 * @since 2022/5/8 15:29
 */
public class ClientConfigurationException extends RuntimeException {
    public ClientConfigurationException() {
    }

    public ClientConfigurationException(String message) {
        super(message);
    }

    public ClientConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientConfigurationException(Throwable cause) {
        super(cause);
    }

    public ClientConfigurationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
