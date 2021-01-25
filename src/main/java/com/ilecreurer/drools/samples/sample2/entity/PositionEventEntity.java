package com.ilecreurer.drools.samples.sample2.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity wrapper class PositionEventEntity.
 * @author ilecreurer.
 */
@Entity
@Table(name = "POSITION_EVENT")
public class PositionEventEntity implements Serializable {

    /**
     * Serial.
     */
    private static final long serialVersionUID = 1L;

    /**
     * idTransaction.
     */
    @Id
    @Column(name = "ID_EVENT")
    private String idEvent;

    /**
     * Owner.
     */
    @Column(name = "ID_OWNER")
    private String idOwner;

    /**
     * Owner name.
     */
    @Column(name = "NAME")
    private String name;

    /**
     * Timestamp.
     */
    @Column(name = "EVENT_TIMESTAMP")
    private Date timestamp;

    /**
     * Latitude.
     */
    @Column(name = "LATITUDE")
    private double latitude;

    /**
     * Longitude.
     */
    @Column(name = "LONGITUDE")
    private double longitude;

    /**
     * Constructor.
     */
    public PositionEventEntity() {
        super();
    }

    /**
     * Constructor.
     * @param idEventParam the idEvent.
     * @param idOwnerParam the idOwner.
     * @param nameParam the name.
     * @param timestampParam the timestamp.
     * @param latitudeParam the latitude.
     * @param longitudeParam the longitude.
     */
    public PositionEventEntity(final String idEventParam,
            final String idOwnerParam,
            final String nameParam,
            final Date timestampParam,
            final double latitudeParam,
            final double longitudeParam) {
        this.idEvent = idEventParam;
        this.idOwner = idOwnerParam;
        this.name = nameParam;
        this.timestamp = timestampParam;
        this.latitude = latitudeParam;
        this.longitude = longitudeParam;
    }

    /**
     * @return the idEvent
     */
    public String getIdEvent() {
        return idEvent;
    }

    /**
     * @param idEventParam the idEvent to set
     */
    public void setIdEvent(final String idEventParam) {
        this.idEvent = idEventParam;
    }

    /**
     * @return the idOwner
     */
    public String getIdOwner() {
        return idOwner;
    }

    /**
     * @param idOwnerParam the idOwner to set
     */
    public void setIdOwner(final String idOwnerParam) {
        this.idOwner = idOwnerParam;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param nameParam the name to set
     */
    public void setName(final String nameParam) {
        this.name = nameParam;
    }

    /**
     * @return the timestamp
     */
    public Date getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestampParam the timestamp to set
     */
    public void setTimestamp(final Date timestampParam) {
        this.timestamp = timestampParam;
    }

    /**
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @param latitudeParam the latitude to set
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
     * @param longitudeParam the longitude to set
     */
    public void setLongitude(final double longitudeParam) {
        this.longitude = longitudeParam;
    }





}
