package com.ilecreurer.drools.samples.sample2.service;

import org.springframework.stereotype.Service;

import com.ilecreurer.drools.samples.sample2.event.Position;
import com.ilecreurer.drools.samples.sample2.util.PositionDistanceCalc;

/**
 * DistanceServiceImpl class.
 * @author ilecreurer.
 */
@Service
public class DistanceServiceImpl implements DistanceService {

    /**
     * Method to calculate the distance.
     * @param p1 the first position.
     * @param p2 the second position.
     * @return the distance in meters.
     * @throws IllegalArgumentException when one of the parameters is invalid.
     */
    @Override
    public double getDistance(Position p1, Position p2) throws IllegalArgumentException {
        return PositionDistanceCalc.getDistance(p1, p2);
    }

}
