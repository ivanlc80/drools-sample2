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

    public CollisionServiceException(final String message) {
        super(message);
    }

    public CollisionServiceException(final Throwable cause) {
        super(cause);
    }

    public CollisionServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public CollisionServiceException(final String message,
            final Throwable cause,
            final boolean enableSuppression,
            final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
