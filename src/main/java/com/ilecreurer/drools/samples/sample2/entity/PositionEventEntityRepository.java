package com.ilecreurer.drools.samples.sample2.entity;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PositionEventEntityRepository extends
    PagingAndSortingRepository<PositionEventEntity, String> {

    List<PositionEventEntity> findByOrderByTimestamp();
}
