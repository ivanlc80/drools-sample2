package com.ilecreurer.drools.samples.sample2.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.api.time.SessionPseudoClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

/**
 * CollisionServiceImpl class.
 * @author ilecreurer.
 */
@Service
@Scope("singleton")
public class CollisionServiceImpl implements CollisionService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(CollisionServiceImpl.class);

    /**
     * Logger for CEP.
     */
    private static final Logger LOGGER_DRL = (Logger) LoggerFactory.getLogger("com.ilecreurer.drools.samples.sample2.event.Cep");

    /**
     * Current state of the engine.
     */
    private CollisionServiceState state = CollisionServiceState.STOPPED;

    /**
     * SimpleDateFormat object.
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss.SSSZ");

    @Autowired
    private DistanceService distanceService;

    /**
     * KieContainer object.
     */
    @Autowired
    private KieContainer kieContainer;

    /**
     * KieSession object.
     */
    private KieSession kieSession;

    /**
     * EntryPoint into the KieSession.
     */
    private EntryPoint entryPoint;

    /**
     * SessionPseudoClock object.
     */
    private SessionPseudoClock clock;

    @PostConstruct
    void onStart() throws CollisionServiceException {
        try {
            state = CollisionServiceState.STARTING;
            LOGGER.info("CollisionServiceImpl is starting...");

            LOGGER.info("Creating kieSession...");
            this.kieSession = kieContainer.newKieSession("ksessionceprules");

            if (this.kieSession == null) {
                throw new CollisionServiceException("Failed to create kieSession for session name 'ksessionceprules'");
            }
            if (this.distanceService == null) {
                throw new CollisionServiceException("Failed to get distanceService");
            }

            this.kieSession.setGlobal("LOGGER_DRL", LOGGER_DRL);
            this.kieSession.setGlobal("distanceService", distanceService);

            LOGGER.info("Creating entryPoint...");
            this.entryPoint = this.kieSession.getEntryPoint("PositionEventStream");

            LOGGER.info("Getting session clock...");
            this.clock = (SessionPseudoClock)this.kieSession.getSessionClock();

            long currentTime = this.clock.getCurrentTime();
            LOGGER.debug("PseudoClock starting at {}", sdf.format(new Date(currentTime)));

            state = CollisionServiceState.READY;
        } catch (CollisionServiceException e) {
            LOGGER.error("Failed to set up kieSession", e);
            throw e;
        } catch (Exception e) {
            throw new CollisionServiceException("Failed to start", e);
        }
    }

    /**
     * Finalization method.
     */
    @PreDestroy
    void onStop() {
        this.kieSession.dispose();

        state = CollisionServiceState.STOPPED;
        LOGGER.info("The application is stopped...");
    }

    /**
     * Method to insert position events.
     * @param positionEvents a list of PositionEvent objects.
     * @throws CollisionServiceException when the insertion fails.
     * @throws IllegalArgumentException when the positionEvents is invalid.
     */
    @Override
    public void insertPositionEvents(List<PositionEvent> positionEvents)
            throws CollisionServiceException, IllegalArgumentException {
        LOGGER.debug("Entering insertPositionEvents...");
        if (positionEvents == null)
            throw new IllegalArgumentException("positionEvents is null");
        if (positionEvents.isEmpty())
            throw new IllegalArgumentException("positionEvents is empty");

        LOGGER.debug("Check number of facts...");
        long fc = this.kieSession.getFactCount();
        LOGGER.debug("fc: {}", fc);

        for (PositionEvent positionEvent: positionEvents) {
            LOGGER.debug("Inserting transaction...");
            FactHandle factHandle = entryPoint.insert(positionEvent);

            long advanceTime = positionEvent.getTimestamp().getTime() - clock.getCurrentTime();
            if (advanceTime > 0) {
                LOGGER.debug("Advancing time by {}", advanceTime);
                clock.advanceTime(advanceTime, TimeUnit.MILLISECONDS);
                LOGGER.debug("Clock time: {}", sdf.format(clock.getCurrentTime()));
            } else {
                LOGGER.debug("Not advancing time. transaction time: {}, clock time: {}",
                        positionEvent.getTimestamp().getTime(),
                        clock.getCurrentTime());
            }
            LOGGER.debug("Firing rules!");
            kieSession.fireAllRules();
        }

    }

}
