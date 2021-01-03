package com.ilecreurer.drools.samples.sample2.entity;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
public class PositionEventEntityRepositoryTest {

    /**
     * KieSessionWrapperEntityRepository attribute.
     */
    @Autowired
    private PositionEventEntityRepository positionEventEntityRepository;

    /**
     * Check that the implementation is found.
     */
    @Test
    @Order(1)
    void checkServiceExistsTest() {
        assertThat(positionEventEntityRepository).isNotNull();
    }

    @Test
    @Order(2)
    void checkInsertTest() {
        Date d1 = new Date();
        PositionEventEntity entity1 = new PositionEventEntity();
        entity1.setIdEvent("1");
        entity1.setIdOwner(1);
        entity1.setType("A");
        entity1.setTimestamp(d1);
        entity1.setLatitude(42.2199298);
        entity1.setLongitude(8.7026243);
        entity1 = positionEventEntityRepository.save(entity1);
        assertThat(entity1).isNotNull();
        assertThat(entity1.getIdEvent()).isNotNull();

        Date d2 = new Date(d1.getTime() + 1000);
        PositionEventEntity entity2 = new PositionEventEntity();
        entity2.setIdEvent("2");
        entity2.setIdOwner(2);
        entity2.setType("B");
        entity2.setTimestamp(d2);
        entity2.setLatitude(42.2199299);
        entity2.setLongitude(8.7026244);
        entity2 = positionEventEntityRepository.save(entity2);
    }

    @Test
    @Order(3)
    void listOrderByTimestamps() {
        List<PositionEventEntity> list = positionEventEntityRepository.findByOrderByTimestamp();
        PositionEventEntity entity0 = null;
        Iterator<PositionEventEntity> it = list.iterator();
        while(it.hasNext()) {
            PositionEventEntity entity = it.next();
            if (entity0 != null) {
                assertThat(entity0.getTimestamp()).isBefore(entity.getTimestamp());
            }
            entity0 = entity;
        }
    }

    @Test
    @Order(4)
    void checkFindByIdTest() {
        Optional<PositionEventEntity> result = positionEventEntityRepository.findById("1");
        assertThat(result.isPresent()).isTrue();
    }

    @Test
    @Order(5)
    void deleteAllTest() {
        Iterable<PositionEventEntity> it = positionEventEntityRepository.findAll();
        for (PositionEventEntity entity: it) {
            positionEventEntityRepository.deleteById(entity.getIdEvent());
        }
    }

}
