package com.ilecreurer.drools.samples.sample2.controller;

import java.io.Serializable;
import java.util.List;

import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

/**
 * MultipleTransactionsPayload class.
 * @author ilecreurer.
 */
public class MultiplePositionEventsPayload implements Serializable {

    /**
     * Serial.
     */
    private static final long serialVersionUID = 1L;

    /**
     * NumberItems.
     */
    private Integer numberItems;

    /**
     * Transactions.
     */
    private List<PositionEvent> positionEvents;

    /**
     * Constructor.
     */
    public MultiplePositionEventsPayload() {
        super();
    }

    /**
     * @return the numberItems
     */
    public Integer getNumberItems() {
        return numberItems;
    }

    /**
     * @param numberItemsParams the numberItems to set
     */
    public void setNumberItems(final Integer numberItemsParams) {
        this.numberItems = numberItemsParams;
    }

    /**
     * @return the positionEvents
     */
    public List<PositionEvent> getPositionEvents() {
        return positionEvents;
    }

    /**
     * @param positionEventsParam the positionEvents to set
     */
    public void setPositionEvents(final List<PositionEvent> positionEventsParam) {
        this.positionEvents = positionEventsParam;
    }

}
