package com.ilecreurer.drools.samples.sample2.controller;

import java.io.Serializable;

/**
 * ResponseMessage class.
 * @author ilecreurer.
 */
public class ResponseMessage implements Serializable {

    /**
     * Serial.
     */
    private static final long serialVersionUID = 1L;
    private final String message;
    private final int errorCode;
    /**
     * Constructor.
     * @param message the message.
     */
    public ResponseMessage(final String message, final int errorCode) {
        super();
        this.message = message;
        this.errorCode = errorCode;
    }
    /**
     * Get message.
     * @return message.
     */
    public String getMessage() {
        return message;
    }
    /**
     * Get errorCode.
     * @return the errorCode.
     */
    public int getErrorCode() {
        return errorCode;
    }



}
