package com.ilecreurer.drools.samples.sample2.event;

import java.io.Serializable;

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
     * @param latitude the latitude.
     * @param longitude the longitude.
     */
    public Position(final double latitude, final double longitude) {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(final double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(final double longitude) {
        this.longitude = longitude;
    }

    /**
     * Method to get the distance between two points on the sphere.
     * @param p2 the second point.
     * @return The distance in metres.
     * @throws IllegalArgumentException when the positions have incorrect data.
     */
    public double distanceTo(Position p2) throws IllegalArgumentException {
        if (this.getLatitude() > 90) throw new IllegalArgumentException("p1.latitude is greater than 90");
        if (this.getLatitude() < -90) throw new IllegalArgumentException("p1.latitude is less than 90");
        if (p2.getLatitude() > 90) throw new IllegalArgumentException("p2.latitude is greater than 90");
        if (p2.getLatitude() < -90) throw new IllegalArgumentException("p2.latitude is less than 90");

        // cos(c) = cos(a)*cos(b) + sin(a)*sin(b)*cos(C)
        double a = toRadians(90 - this.getLatitude());
        double b = toRadians(90 - p2.getLatitude());
        double C = toRadians(this.getLongitude() - p2.getLongitude());
        double cosc =
                (
                    Math.cos(a) *
                    Math.cos(b)
                ) +
                (
                    Math.sin(a) *
                    Math.sin(b) *
                    Math.cos(C)
                )
                ;
        double c = toDegrees(Math.acos(cosc));
        double dist = convertDegreesToMetres(c);
        return dist;
    }

    public static double toRadians(double angleInDegrees) {
        return (angleInDegrees / 180) * Math.PI;
    }

    public static double toDegrees(double angleInRadians) {
        return (angleInRadians / Math.PI) * 180;
    }

    public static double convertDegreesToMetres(double angleInDegrees) {
        return angleInDegrees * 60 * 1852;
    }
}
