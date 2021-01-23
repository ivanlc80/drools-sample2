package com.ilecreurer.drools.samples.sample2.controller;

/**
 * Enum ErrorCode.
 * @author ilecreurer.
 */
public enum ErrorCode {

    /**
     * Service error.
     */
    SERVICE_ERROR(10),

    /**
     * Missing position events attribute.
     */
    MISSING_POSITION_EVENTS_ATTR(100),

    /**
     * Missing number items attribute.
     */
    MISSING_NUMBER_ITEMS_ATTR(101),

    /**
     * Number items mismatch attribute.
     */
    NUMBER_ITEMS_MISMATCH(102),

    /**
     * 0 items.
     */
    EMPTY_ITEMS_ATTR(103);

    /**
     * The code.
     */
    private final int code;

    /**
     * Constructor.
     * @param codeParam the code param.
     */
    ErrorCode(final int codeParam) {
        this.code = codeParam;
    }

    /**
     * Getter code.
     * @return the code value.
     */
    public int getCode() {
        return this.code;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    public String toString() {
        return String.valueOf(code);
    }
}
