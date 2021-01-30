package com.ilecreurer.drools.samples.sample2.util;

/**
 * Constants class.
 * @author ilecreurer.
 */
public final class Constants {

    /**
     * Constructor (hidden as utility class).
     */
    private Constants() { }

    /**
     * The minimum distance before considering a potential collision.
     */
    public static final Double MIN_DIST = new Double(50.0);

    /**
     * Insert into blocks of 100 registers.
     */
    public static final int MAX_POSITION_EVENTS_SIZE = 100;

    /**
     * 1000 millis in one second.
     */
    public static final int MILLIS_IN_SECOND = 1000;

    /**
     * Log every 1000 inserted registers.
     */
    public static final int REGISTERS_LOG_BLOCK_SIZE = 1000;
}
