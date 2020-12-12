package com.ilecreurer.drools.samples.sample2.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class CollisionServiceTest3 {
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
    @Test
    void insertMutiplePositionEvents(
            ) {
        try {
            InputStream is = this.getClass().getClassLoader()
                    .getResourceAsStream("data2.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            List<PositionEvent> positionEvents = new ArrayList<PositionEvent>();

            String line;
            while((line = br.readLine()) != null) {
                String [] ar = line.split(",");

                String timestampAsString = ar[0];
                String idEvent = ar[1];
                String idOwnerAsString = ar[2];
                String type = ar[3];
                String latitudeAsString = ar[4];
                String longitudeAsString = ar[5];

                PositionEvent pe = new PositionEvent(
                        idEvent, Long.parseLong(idOwnerAsString), type, sdf.parse(timestampAsString),
                        Double.parseDouble(latitudeAsString), Double.parseDouble(longitudeAsString)
                        );
                positionEvents.add(pe);
            }

            assertDoesNotThrow(() ->
            {
                collisionService.insertPositionEvents(positionEvents);
            });
        } catch (Exception e) {
            assertThat(true).isFalse();
        }
    }
}
