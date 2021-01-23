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
     * @param nameParam the name.
     * @param timestampParam the timestamp.
     * @param latitudeParam the latitude.
     * @param longitudeParam the longitude.
     */
    public PositionEvent(final String idEventParam,
            final String idOwnerParam,
            final String nameParam,
            final Date timestampParam,
            final double latitudeParam,
            final double longitudeParam) {
        super(latitudeParam, longitudeParam);
        this.idEvent = idEventParam;
        this.idOwner = idOwnerParam;
        this.name = nameParam;
        this.timestamp = timestampParam;
    }

    /**
     * idTransaction.
     */
    private String idEvent;

    /**
     * Owner.
     */
    private String idOwner;

    /**
     * Owner name.
     */
    private String name;

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

}
