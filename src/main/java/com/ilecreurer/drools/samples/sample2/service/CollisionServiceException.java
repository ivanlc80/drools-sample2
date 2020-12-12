package com.ilecreurer.drools.samples.sample2.service;

/**
 * CollisionServiceException class.
 * @author ilecreurer.
 */
public class CollisionServiceException extends Exception {

    /**
     * Serial.
     */
    private static final long serialVersionUID = 1L;

    public CollisionServiceException() {
    }

    public CollisionServiceException(String message) {
        super(message);
    }

    public CollisionServiceException(Throwable cause) {
        super(cause);
    }

    public CollisionServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CollisionServiceException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
