package com.gapache.uid.exception;

/**
 * UidGenerateException
 *
 * @author HuSen
 * create on 2020/1/9 16:38
 */
public class UidGenerateException extends RuntimeException {
    private static final long serialVersionUID = 7450085737650622981L;

    /**
     * Default constructor
     */
    public UidGenerateException() {
        super();
    }

    /**
     * Constructor with message & cause
     *
     * @param message message
     * @param cause   cause
     */
    public UidGenerateException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructor with message
     *
     * @param message message
     */
    public UidGenerateException(String message) {
        super(message);
    }

    /**
     * Constructor with message format
     *
     * @param msgFormat msgFormat
     * @param args      args
     */
    public UidGenerateException(String msgFormat, Object... args) {
        super(String.format(msgFormat, args));
    }

    /**
     * Constructor with cause
     *
     * @param cause cause
     */
    public UidGenerateException(Throwable cause) {
        super(cause);
    }
}
