package com.ilecreurer.drools.samples.sample2.util;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

import com.ilecreurer.drools.samples.sample2.event.Position;

public class PositionDistanceCalcTest {

    @Test
    void testCalculateDistance() {
        Position p1 = new Position(42.24100554343951, 8.725979275070594);
        Position p2 = new Position(42.240799030012575, 8.726344055476336);

        double dist1 = p1.distanceTo(p2);
        double dist2 = p2.distanceTo(p1);
        assertThat(dist1 < 50).isTrue();
        assertThat(dist1).isEqualTo(dist2);
    }
}
