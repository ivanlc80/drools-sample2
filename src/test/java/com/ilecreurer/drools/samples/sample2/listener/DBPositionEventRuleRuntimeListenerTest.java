package com.ilecreurer.drools.samples.sample2.listener;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ilecreurer.drools.samples.sample2.entity.PositionEventEntity;
import com.ilecreurer.drools.samples.sample2.entity.PositionEventEntityRepository;
import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class DBPositionEventRuleRuntimeListenerTest {

    /**
     * PositionEventEntityRepository object.
     */
    @Autowired
    private PositionEventEntityRepository positionEventEntityRepository;

    /**
     * Check that the implementation is found.
     */
    @Test
    @Order(1)
    void collisionServiceInjection() {
        assertDoesNotThrow(() -> {
            DBPositionEventRuleRuntimeListener listener =
                new DBPositionEventRuleRuntimeListener(
                        positionEventEntityRepository, true);
        });
    }
}
