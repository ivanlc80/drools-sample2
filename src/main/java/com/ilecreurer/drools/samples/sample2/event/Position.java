package com.ilecreurer.drools.samples.sample2.event;

import java.io.Serializable;

/**
 * Position class.
 * @author ilecreurer.
 */
public class Position implements Serializable {
    /**
     * Max latitude.
     */
    private static final int MAX_LAT = 90;

    /**
     * 180 degrees.
     */
    private static final int PI_IN_DEGREES = 180;

    /**
     * 60 minutes in one degree.
     */
    private static final int MINUTES_IN_DEGREE = 60;

    /**
     * Number of metres in one NM which is one minute of a degree.
     */
    private static final int METRES_PER_NAUTICAL_MILE = 1852;

    /**
     * Serial.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Latitude.
     */
    private double latitude;

    /**
     * Longitude.
     */
    private double longitude;

    /**
     * Constructor.
     */
    public Position() {
        super();
    }

    /**
     * Constructor.
     * @param latitudeParam the latitude.
     * @param longitudeParam the longitude.
     */
    public Position(final double latitudeParam, final double longitudeParam) {
        super();
        this.latitude = latitudeParam;
        this.longitude = longitudeParam;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitudeParam the latitude to set.
     */
    public void setLatitude(final double latitudeParam) {
        this.latitude = latitudeParam;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitudeParam the longitude to set.
     */
    public void setLongitude(final double longitudeParam) {
        this.longitude = longitudeParam;
    }

    /**
     * Method to get the difference in latitude between two points on the sphere.
     * @param p2 the second point.
     * @return The absolute value of the difference between the latitude.
     * @throws IllegalArgumentException when the positions have incorrect data.
     */
    public double diffLat(final Position p2) throws IllegalArgumentException {
        if (p2 == null) throw new IllegalArgumentException("p2 is null");

        return Math.abs(this.getLatitude() - p2.getLatitude());
    }

    /**
     * Method to get the difference in longitude between two points on the sphere.
     * @param p2 the second point.
     * @return The absolute value of the difference between the longitudes.
     * @throws IllegalArgumentException when the positions have incorrect data.
     */
    public double diffLong(final Position p2) throws IllegalArgumentException {
        if (p2 == null) throw new IllegalArgumentException("p2 is null");

        return Math.abs(this.getLongitude() - p2.getLongitude());
    }

    /**
     * Method to get the distance between two points on the sphere.
     * @param p2 the second point.
     * @return The distance in metres.
     * @throws IllegalArgumentException when the positions have incorrect data.
     */
    public double distanceTo(final Position p2) throws IllegalArgumentException {
        if (p2 == null) throw new IllegalArgumentException("p2 is null");
        if (this.getLatitude() > MAX_LAT) throw new IllegalArgumentException("p1.latitude is greater than 90");
        if (this.getLatitude() < -MAX_LAT) throw new IllegalArgumentException("p1.latitude is less than 90");
        if (p2.getLatitude() > MAX_LAT) throw new IllegalArgumentException("p2.latitude is greater than 90");
        if (p2.getLatitude() < -MAX_LAT) throw new IllegalArgumentException("p2.latitude is less than 90");

        // cos(c) = cos(a)*cos(b) + sin(a)*sin(b)*cos(C)
        double a = toRadians(MAX_LAT - this.getLatitude());
        double b = toRadians(MAX_LAT - p2.getLatitude());
        double angleC = toRadians(this.getLongitude() - p2.getLongitude());
        double cosc =
                (
                    Math.cos(a) * Math.cos(b)
                )
                + (
                    Math.sin(a) * Math.sin(b) * Math.cos(angleC)
                );
        double c = toDegrees(Math.acos(cosc));
        double dist = convertDegreesToMetres(c);
        return dist;
    }

    public static double toRadians(final double angleInDegrees) {
        return (angleInDegrees / PI_IN_DEGREES) * Math.PI;
    }

    public static double toDegrees(final double angleInRadians) {
        return (angleInRadians / Math.PI) * PI_IN_DEGREES;
    }

    public static double convertDegreesToMetres(final double angleInDegrees) {
        return angleInDegrees * MINUTES_IN_DEGREE * METRES_PER_NAUTICAL_MILE;
    }
}
