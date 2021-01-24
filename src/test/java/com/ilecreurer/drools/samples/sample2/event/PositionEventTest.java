package com.ilecreurer.drools.samples.sample2.event;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

/**
 * PositionEventTest class.
 * @author ilecreurer.
 *
 */
public class PositionEventTest {

    /**
     * Order test on IdOwner.
     */
    @Test
    void eventPositionTest() {
        PositionEvent pe1 = new PositionEvent("ID1", "ABC", "ABC name", new Date(), 0.0d ,0.0d);
        PositionEvent pe2 = new PositionEvent("ID2", "XYZ", "XYZ name", new Date(), 0.0d ,0.0d);

        assertThat(pe1.getIdOwner().compareTo(pe2.getIdOwner()) < 0);
    }
}
