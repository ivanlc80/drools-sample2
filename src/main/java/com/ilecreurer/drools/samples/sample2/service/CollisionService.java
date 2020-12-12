package com.ilecreurer.drools.samples.sample2.service;

import java.util.List;

import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

/**
 * CollisionService interface.
 * @author ilecreurer.
 */
public interface CollisionService {
    /**
     * Method to insert position events.
     * @param positionEvents a list of PositionEvent objects.
     * @throws CollisionServiceException when the insertion fails.
     */
    void insertPositionEvents(List<PositionEvent> positionEvents)
            throws CollisionServiceException;
}
