package com.ilecreurer.drools.samples.sample2.service;

import org.springframework.stereotype.Service;

import com.ilecreurer.drools.samples.sample2.event.Position;

/**
 * DistanceService interface.
 * @author ilecreurer.
 */
public interface DistanceService {
    /**
     * Method to calculate the distance between two points.
     * @param p1 the first point.
     * @param p2 the second point.
     * @return The distance in meters.
     * @throws IllegalArgumentException when one of the arguments is invalid.
     */
    public double getDistance(Position p1, Position p2) throws IllegalArgumentException;
}
