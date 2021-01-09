package com.ilecreurer.drools.samples.sample2.service;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class CollisionServiceTest {
    /**
     * SimpleDateFormat object.
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSZ");

    /**
     * CollisionService object.
     */
    @Autowired
    private CollisionService collisionService;

    /**
     * Check that the implementation is found.
     */
    @Test
    @Order(1)
    void collisionServiceInjection() {
        assertThat(collisionService).isNotNull();
    }

    /**
     * Check that inserting an empty list is not allowed.
     */
    @Test
    @Order(2)
    void insertEmptyPositionEvents() {
        List<PositionEvent> positionEvents = new ArrayList<PositionEvent>();
        assertThrows(IllegalArgumentException.class ,
                () -> collisionService.insertPositionEvents(positionEvents));
    }

    /**
     * Check that inserting a non-empty list is allowed.
     */
    @Test
    @Order(3)
    void insertNoneEmptyPositionEvents() {
        double [][] arPositionsOwner1 = {
                {42.23925810080146, 8.720775789889855},
                {42.23910718303044, 8.722562140994441},
                {42.238920521077496, 8.724520153466433}
        };
        String [] arTimesOwner1 = {
                "2020-12-06 20:21:00.000+0100",
                "2020-12-06 20:21:02.000+0100",
                "2020-12-06 20:22:04.000+0100"

        };

        double [][] arPositionsOwner2 = {
                {42.24035025812356, 8.724954671302685},
                {42.2396790800445, 8.724863476201246},
                {42.238999951763255, 8.724740094593423}
        };
        String [] arTimesOwner2 = {
                "2020-12-06 20:21:01.000+0100",
                "2020-12-06 20:21:03.000+0100",
                "2020-12-06 20:22:05.000+0100"

        };
        assertDoesNotThrow(() ->
        {
            List<PositionEvent> positionEvents = new ArrayList<PositionEvent>();
            long idOwner1 = 1; String type1 = "A"; String uuidp1 = "686dfffb-2478-4bcd-b1ac-a357b183b8a";
            long idOwner2 = 2; String type2 = "B"; String uuidp2 = "686dfffb-2478-4bcd-b1ac-a357b183b8b";

            for (int i = 0;  i < arPositionsOwner1.length; i++) {
                PositionEvent pe1 = new PositionEvent(uuidp1 + i, idOwner1 ,type1,
                        sdf.parse(arTimesOwner1[i]),
                        arPositionsOwner1[i][0], arPositionsOwner1[i][1]);
                positionEvents.add(pe1);

                PositionEvent pe2 = new PositionEvent(uuidp2 + i, idOwner2 ,type2,
                        sdf.parse(arTimesOwner2[i]),
                        arPositionsOwner2[i][0], arPositionsOwner2[i][1]);
                positionEvents.add(pe2);
            }

            collisionService.insertPositionEvents(positionEvents);
        });
    }
}
