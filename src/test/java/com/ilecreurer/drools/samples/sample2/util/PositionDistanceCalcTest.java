package com.ilecreurer.drools.samples.sample2.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.ilecreurer.drools.samples.sample2.event.Position;

public class PositionDistanceCalcTest {

    @Test
    void testCalculateDistance1() {
        Position p1 = new Position(42.24305872668545, 8.726016825947589);
        Position p2 = new Position(42.24019934293979, 8.731188124640742);

        double dist = PositionDistanceCalc.getDistance(p1, p2);
        assertThat(dist < 540).isTrue();
    }

    @Test
    void testCalculateDistance2() {
        Position p1 = new Position(42.24100554343951, 8.725979275070594);
        Position p2 = new Position(42.240799030012575, 8.726344055476336);

        double dist = PositionDistanceCalc.getDistance(p1, p2);
        assertThat(dist < 50).isTrue();
    }
}
