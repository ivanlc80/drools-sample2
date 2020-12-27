package com.ilecreurer.drools.samples.sample2.service;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CollisionServiceTest2 {
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
     * Check that inserting a non-empty list is allowed.
     */
    @ParameterizedTest
    @CsvFileSource(resources = "/data2.csv", numLinesToSkip = 0)
    void insertMutiplePositionEvents(
            String timestampAsString,
            String idEvent,
            String idOwnerAsString,
            String type,
            String latitudeAsString,
            String longitudeAsString) {
        try {
            List<PositionEvent> positionEvents = new ArrayList<PositionEvent>();
            PositionEvent pe = new PositionEvent(
                    idEvent, Long.parseLong(idOwnerAsString), type, sdf.parse(timestampAsString),
                    Double.parseDouble(latitudeAsString), Double.parseDouble(longitudeAsString)
                    );
            positionEvents.add(pe);

            assertDoesNotThrow(() ->
            {
                collisionService.insertPositionEvents(positionEvents);
            });
        } catch (Exception e) {
            assertThat(true).isFalse();
        }
    }
}
