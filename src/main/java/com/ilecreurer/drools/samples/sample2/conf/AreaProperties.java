package com.ilecreurer.drools.samples.sample2.conf;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ilecreurer.drools.samples.sample2.util.Constants;

@Configuration
@ConfigurationProperties("area")
public class AreaProperties {

    /**
     * minLatitude attribute.
     */
    private double minLatitude = Constants.MIN_LAT;

    /**
     * maxLatitude attribute.
     */
    private double maxLatitude = Constants.MAX_LAT;

    /**
     * minLongitude attribute.
     */
    private double minLongitude = Constants.MIN_LONG;

    /**
     * maxLongitude attribute.
     */
    private double maxLongitude = Constants.MAX_LONG;

    /**
     * @return the minLatitude.
     */
    public double getMinLatitude() {
        return minLatitude;
    }

    /**
     * @param minLatitudeParam the minLatitude to set.
     */
    public void setMinLatitude(final double minLatitudeParam) {
        this.minLatitude = minLatitudeParam;
    }

    /**
     * @return the maxLatitude.
     */
    public double getMaxLatitude() {
        return maxLatitude;
    }

    /**
     * @param maxLatitudeParam the maxLatitude to set.
     */
    public void setMaxLatitude(final double maxLatitudeParam) {
        this.maxLatitude = maxLatitudeParam;
    }

    /**
     * @return the minLongitude.
     */
    public double getMinLongitude() {
        return minLongitude;
    }

    /**
     * @param minLongitudeParam the minLongitude to set.
     */
    public void setMinLongitude(final double minLongitudeParam) {
        this.minLongitude = minLongitudeParam;
    }

    /**
     * @return the maxLongitude.
     */
    public double getMaxLongitude() {
        return maxLongitude;
    }

    /**
     * @param maxLongitudeParam the maxLongitude to set.
     */
    public void setMaxLongitude(final double maxLongitudeParam) {
        this.maxLongitude = maxLongitudeParam;
    }

}
