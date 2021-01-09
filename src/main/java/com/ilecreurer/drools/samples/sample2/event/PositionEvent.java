package com.ilecreurer.drools.samples.sample2.event;

import java.util.Date;

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
     * @param idEventParam the idEvent.
     * @param idOwnerParam the idOwner.
     * @param typeParam the type.
     * @param timestampParam the timestamp.
     * @param latitudeParam the latitude.
     * @param longitudeParam the longitude.
     */
    public PositionEvent(final String idEventParam,
            final long idOwnerParam,
            final String typeParam,
            final Date timestampParam,
            final double latitudeParam,
            final double longitudeParam) {
        super(latitudeParam, longitudeParam);
        this.idEvent = idEventParam;
        this.idOwner = idOwnerParam;
        this.type = typeParam;
        this.timestamp = timestampParam;
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
     * @param idEventParam the idEvent to set
     */
    public void setIdEvent(final String idEventParam) {
        this.idEvent = idEventParam;
    }

    /**
     * @return the idOwner
     */
    public long getIdOwner() {
        return idOwner;
    }

    /**
     * @param idOwnerParam the idOwner to set
     */
    public void setIdOwner(final long idOwnerParam) {
        this.idOwner = idOwnerParam;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param typeParam the type to set
     */
    public void setType(final String typeParam) {
        this.type = typeParam;
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

}
