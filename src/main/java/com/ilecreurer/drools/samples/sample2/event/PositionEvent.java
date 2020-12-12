package com.ilecreurer.drools.samples.sample2.event;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * PositionEvent class.
 * @author ilecreurer
 *
 */
public class PositionEvent extends Position {

    /**
     * Serial.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructor.
     */
    public PositionEvent() {
        super();
    }

    /**
     * Constructor.
     * @param idEvent the idEvent.
     * @param idOwner the idOwner.
     * @param type the type.
     * @param timestamp the timestamp.
     * @param latitude the latitude.
     * @param longitude the longitude.
     */
    public PositionEvent(final String idEvent,
            final long idOwner,
            final String type,
            final Date timestamp,
            final double latitude,
            final double longitude) {
        super(latitude, longitude);
        this.idEvent = idEvent;
        this.idOwner = idOwner;
        this.type = type;
        this.timestamp = timestamp;
    }

    /**
     * idTransaction.
     */
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

}
