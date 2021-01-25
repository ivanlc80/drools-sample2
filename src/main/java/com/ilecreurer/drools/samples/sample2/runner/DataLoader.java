package com.ilecreurer.drools.samples.sample2.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.ilecreurer.drools.samples.sample2.service.CollisionService;
import com.ilecreurer.drools.samples.sample2.service.CollisionServiceException;

@Component
public class DataLoader implements ApplicationRunner {

    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DataLoader.class);

    /**
     * PositionEventEntityRepository object.
     */
    @Autowired
    private CollisionService collisionService;

    @Override
    public void run(final ApplicationArguments args) throws Exception {
        LOGGER.info("Entering run...");
        try {
            collisionService.preloadSession();
        } catch (CollisionServiceException e) {
            LOGGER.error("Failed to prepload the session", e);
        }
    }

}
