package com.ilecreurer.drools.samples.sample2.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

/**
 * Entity wrapper class PositionEventEntity.
 * @author ilecreurer.
 */
@Entity
public class PositionEventEntity implements Serializable {

    /**
     * Serial.
     */
    private static final long serialVersionUID = 1L;

    /**
     * idTransaction.
     */
    @Id
    private String idEvent;

    /**
     * Owner.
     */
    private long idOwner;

    /**
     * Owner type.
     */
    private String type;

    /**
     * Timestamp.
     */
    private Date timestamp;

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
    public PositionEventEntity() {
        super();
    }

    /**
     * Constructor.
     * @param idEvent the idEvent param.
     * @param idOwner the idOwner param.
     * @param type the type param.
     * @param timestamp the timestamp param.
     * @param latitude the latitude param.
     * @param longitude the longitude param.
     */
    public PositionEventEntity(final String idEvent,
            final long idOwner,
            final String type,
            final Date timestamp,
            final double latitude,
            final double longitude) {
        this.idEvent = idEvent;
        this.idOwner = idOwner;
        this.type = type;
        this.timestamp = timestamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * @return the idEvent
     */
    public String getIdEvent() {
        return idEvent;
    }

    /**
     * @param idEvent the idEvent to set
     */
    public void setIdEvent(String idEvent) {
        this.idEvent = idEvent;
    }

    /**
     * @return the idOwner
     */
    public long getIdOwner() {
        return idOwner;
    }

    /**
     * @param idOwner the idOwner to set
     */
    public void setIdOwner(long idOwner) {
        this.idOwner = idOwner;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
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
    public void setLatitude(double latitude) {
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
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }





}
