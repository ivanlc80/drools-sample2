package com.ilecreurer.drools.samples.sample2.event;

import java.io.Serializable;

import com.ilecreurer.drools.samples.sample2.util.Constants;

/**
 * Position class.
 * @author ilecreurer.
 */
public class Position implements Serializable {

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
     * @throws IllegalArgumentException when the parameters are out of range.
     */
    public Position(final double latitudeParam, final double longitudeParam) throws IllegalArgumentException {
        super();
        if (latitudeParam > Constants.MAX_LAT) {
            throw new IllegalArgumentException("latitude is greater than " + Constants.MAX_LAT);
        }
        if (latitudeParam < Constants.MIN_LAT) {
            throw new IllegalArgumentException("latitude is less than " + Constants.MIN_LAT);
        }
        if (longitudeParam > Constants.MAX_LONG) {
            throw new IllegalArgumentException("longitude is greater than " + Constants.MAX_LONG);
        }
        if (longitudeParam < Constants.MIN_LONG) {
            throw new IllegalArgumentException("longitude is less than " + Constants.MIN_LONG);
        }

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
     * @throws IllegalArgumentException when the parameter is out of range.
     */
    public void setLatitude(final double latitudeParam) {
        if (latitudeParam > Constants.MAX_LAT) {
            throw new IllegalArgumentException("latitude is greater than " + Constants.MAX_LAT);
        }
        if (latitudeParam < Constants.MIN_LAT) {
            throw new IllegalArgumentException("latitude is less than " + Constants.MIN_LAT);
        }

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
     * @throws IllegalArgumentException when the parameter is out of range.
     */
    public void setLongitude(final double longitudeParam) {
        if (longitudeParam > Constants.MAX_LONG) {
            throw new IllegalArgumentException("longitude is greater than " + Constants.MAX_LONG);
        }
        if (longitudeParam < Constants.MIN_LONG) {
            throw new IllegalArgumentException("longitude is less than " + Constants.MIN_LONG);
        }

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

        return Math.abs(this.getLongitude() - p2.getLongitude()) % Constants.MAX_LONG;
    }

    /**
     * Method to get the distance between two points on the sphere.
     * @param p2 the second point.
     * @return The distance in metres.
     * @throws IllegalArgumentException when the positions have incorrect data.
     */
    public double distanceTo(final Position p2) throws IllegalArgumentException {
        if (p2 == null) throw new IllegalArgumentException("p2 is null");

        // cos(c) = cos(a)*cos(b) + sin(a)*sin(b)*cos(C)
        double a = toRadians(Constants.MAX_LAT - this.getLatitude());
        double b = toRadians(Constants.MAX_LAT - p2.getLatitude());
        double angleC = toRadians(Math.abs(this.getLongitude() - p2.getLongitude()) % Constants.MAX_LONG);
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
        return (angleInDegrees / Constants.PI_IN_DEGREES) * Math.PI;
    }

    public static double toDegrees(final double angleInRadians) {
        return (angleInRadians / Math.PI) * Constants.PI_IN_DEGREES;
    }

    public static double convertDegreesToMetres(final double angleInDegrees) {
        return angleInDegrees * Constants.MINUTES_IN_DEGREE * Constants.METRES_PER_NAUTICAL_MILE;
    }
}
