package com.gevlet.coop.exceptions;

/**
 * ClassName:
 * Description:<p></p>
 * Create by daishenglei@bestpay.com.cn on 2018-05-08 18:09
 */
public class ServerInitException extends RuntimeException{


    public ServerInitException() {
        super();
    }

    public ServerInitException(String message) {
        super(message);
    }

    public ServerInitException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerInitException(Throwable cause) {
        super(cause);
    }

    protected ServerInitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
