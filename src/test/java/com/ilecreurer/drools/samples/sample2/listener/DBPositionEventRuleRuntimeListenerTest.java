package com.ilecreurer.drools.samples.sample2.listener;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ilecreurer.drools.samples.sample2.entity.PositionEventEntityRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
            listener.setActive(false);
            listener.setActive(true);
        });
    }
}
