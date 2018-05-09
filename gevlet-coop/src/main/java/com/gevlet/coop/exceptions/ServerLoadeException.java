package com.gevlet.coop.exceptions;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 18:09
 */
public class ServerLoadeException extends RuntimeException{


    public ServerLoadeException() {
        super();
    }

    public ServerLoadeException(String message) {
        super(message);
    }

    public ServerLoadeException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerLoadeException(Throwable cause) {
        super(cause);
    }

    protected ServerLoadeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
