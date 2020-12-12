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

}
