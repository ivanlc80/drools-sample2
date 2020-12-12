package com.ilecreurer.drools.samples.sample2.util;

import com.ilecreurer.drools.samples.sample2.event.Position;

/**
 * PositionDistanceCalc class.
 * @author ilecreurer
 *
 */
public class PositionDistanceCalc {
    /**
     * Method to get the distance between two points on the sphere.
     * @param p1 the first point.
     * @param p2 the second point.
     * @return The distance in metres.
     * @throws IllegalArgumentException
     */
    public static double getDistance(Position p1, Position p2)
            throws IllegalArgumentException {
        if (p1 == null) throw new IllegalArgumentException("p1 is null");
        if (p2 == null) throw new IllegalArgumentException("p2 is null");
        if (p1.getLatitude() > 90) throw new IllegalArgumentException("p1.latitude is greater than 90");
        if (p1.getLatitude() < -90) throw new IllegalArgumentException("p1.latitude is less than 90");
        if (p2.getLatitude() > 90) throw new IllegalArgumentException("p2.latitude is greater than 90");
        if (p2.getLatitude() < -90) throw new IllegalArgumentException("p2.latitude is less than 90");

        // cos(c) = cos(a)*cos(b) + sin(a)*sin(b)*cos(C)
        double a = toRadians(90 - p1.getLatitude());
        double b = toRadians(90 - p2.getLatitude());
        double C = toRadians(p1.getLongitude() - p2.getLongitude());
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
