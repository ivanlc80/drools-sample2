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
     * @param messageParam the message param.
     * @param errorCodeParam the errorCode param.
     */
    public ResponseMessage(final String messageParam, final int errorCodeParam) {
        super();
        this.message = messageParam;
        this.errorCode = errorCodeParam;
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
