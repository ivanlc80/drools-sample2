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
     * Insert into blocks of 1000 registers.
     */
    public static final int MAX_POSITION_EVENTS_SIZE = 1000;

    /**
     * 1000 millis in one second.
     */
    public static final int MILLIS_IN_SECOND = 1000;

    /**
     * Max latitude.
     */
    public static final double MAX_LAT = 90;

    /**
     * Min latitude.
     */
    public static final double MIN_LAT = -90;

    /**
     * Max longitude.
     */
    public static final double MAX_LONG = 180;

    /**
     * Min longitude.
     */
    public static final double MIN_LONG = -180;

    /**
     * 180 degrees.
     */
    public static final int PI_IN_DEGREES = 180;

    /**
     * 60 minutes in one degree.
     */
    public static final int MINUTES_IN_DEGREE = 60;

    /**
     * Number of metres in one NM which is one minute of a degree.
     */
    public static final int METRES_PER_NAUTICAL_MILE = 1852;
}
