package com.ilecreurer.drools.samples.sample2.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ilecreurer.drools.samples.sample2.event.Position;
import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class DistanceServiceTest {

    /**
     * CollisionService object.
     */
    @Autowired
    private DistanceService distanceService;

    /**
     * Check that the implementation is found.
     */
    @Test
    void collisionServiceInjection() {
        assertThat(distanceService).isNotNull();
    }

    @Test
    void distanceCalc() {
        double [][] arPositionsOwner1 = {
                {42.23925810080146, 8.720775789889855},
                {42.23910718303044, 8.722562140994441},
                {42.238920521077496, 8.724520153466433}
        };

        double [][] arPositionsOwner2 = {
                {42.24035025812356, 8.724954671302685},
                {42.2396790800445, 8.724863476201246},
                {42.238999951763255, 8.724740094593423}
        };

        for (int i = 0; i < arPositionsOwner1.length; i++) {
            Position p1 = new Position(arPositionsOwner1[i][0], arPositionsOwner1[i][1]);
            Position p2 = new Position(arPositionsOwner2[i][0], arPositionsOwner1[i][1]);
            System.out.println(distanceService.getDistance(p1, p2));
        }
        assertThat(distanceService).isNotNull();
    }
}
