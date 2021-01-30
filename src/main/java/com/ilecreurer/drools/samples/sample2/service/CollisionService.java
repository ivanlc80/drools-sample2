package com.ilecreurer.drools.samples.sample2.service;

import java.util.List;

import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

/**
 * CollisionService interface.
 * @author ilecreurer.
 */
public interface CollisionService {

    /**
     * Method to preload the session with events.
     * @throws CollisionServiceException when the insertion fails.
     */
    void preloadSession()
            throws CollisionServiceException;

    /**
     * Method to insert position events.
     * @param positionEvents a list of PositionEvent objects.
     * @return number of events inserted into the ksession.
     * @throws CollisionServiceException when the insertion fails.
     * @throws IllegalArgumentException when the list of position events is invalid.
     */
    int insertPositionEvents(List<PositionEvent> positionEvents)
            throws CollisionServiceException, IllegalArgumentException;
}
