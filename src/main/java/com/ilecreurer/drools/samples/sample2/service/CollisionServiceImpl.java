package com.ilecreurer.drools.samples.sample2.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.api.time.SessionPseudoClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ilecreurer.drools.samples.sample2.conf.AreaProperties;
import com.ilecreurer.drools.samples.sample2.entity.PositionEventEntity;
import com.ilecreurer.drools.samples.sample2.entity.PositionEventEntityRepository;
import com.ilecreurer.drools.samples.sample2.event.PositionEvent;
import com.ilecreurer.drools.samples.sample2.listener.DBPositionEventRuleRuntimeListener;
import com.ilecreurer.drools.samples.sample2.util.Constants;

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
    private static final Logger LOGGER_DRL =
            (Logger) LoggerFactory.getLogger("com.ilecreurer.drools.samples.sample2.event.Cep");

    /**
     * Current state of the engine.
     */
    private CollisionServiceState state = CollisionServiceState.STOPPED;

    /**
     * SimpleDateFormat object.
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss.SSSZ");

    @Autowired
    private AreaProperties areaProperties;

    /**
     * PositionEventEntityRepository object.
     */
    @Autowired
    private PositionEventEntityRepository positionEventEntityRepository;

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

    /**
     * DBPositionEventRuleRuntimeListener object.
     */
    private DBPositionEventRuleRuntimeListener dbPositionEventRuleRuntimeListener;

    /**
     * Thread object for ksession.
     */
    private Thread threadSession;

    @PostConstruct
    void onStart() throws CollisionServiceException {
        try {
            state = CollisionServiceState.STARTING;
            LOGGER.info("CollisionServiceImpl is starting...");

            LOGGER.info("min latitude: {}", areaProperties.getMinLatitude());
            LOGGER.info("max latitude: {}", areaProperties.getMaxLatitude());
            LOGGER.info("min longitude: {}", areaProperties.getMinLongitude());
            LOGGER.info("max longitude: {}", areaProperties.getMaxLongitude());

            LOGGER.info("Creating kieSession...");
            this.kieSession = kieContainer.newKieSession("ksessionceprules");
            this.kieSession.setGlobal("LOGGER_DRL", LOGGER_DRL);
            this.kieSession.setGlobal("MIN_DIST", Constants.MIN_DIST);

            LOGGER.info("Creating entryPoint...");
            this.entryPoint = this.kieSession.getEntryPoint("PositionEventStream");

            LOGGER.info("Getting session clock...");
            this.clock = (SessionPseudoClock) this.kieSession.getSessionClock();

            long currentTime = this.clock.getCurrentTime();
            LOGGER.debug("PseudoClock starting at {}", sdf.format(new Date(currentTime)));

            LOGGER.debug("Check number of facts...");
            long fc = this.kieSession.getFactCount();
            LOGGER.debug("fc: {}", fc);

            /*threadSession = new Thread() {
                public void run() {
                    LOGGER.debug("Starting session thread...");
                    kieSession.fireUntilHalt();
                }
            };
            threadSession.start();*/

            LOGGER.info("Setting state to {} ...", CollisionServiceState.READY.toString());
            state = CollisionServiceState.READY;
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
     * Method to get the position events ordered by timestamps.
     * @return List<PositionEvent> the list of position events.
     */
    private List<PositionEvent> getPositionEventsFromDB() {
        LOGGER.info("Entering getPositionEventsFromDB...");
        List<PositionEvent> positionEvents = new ArrayList<PositionEvent>();
        List<PositionEventEntity> listPositionEventEntity =
                positionEventEntityRepository.findByOrderByTimestamp();
        Iterator<PositionEventEntity> itPositionEventEntity = listPositionEventEntity.iterator();
        while (itPositionEventEntity.hasNext()) {
            PositionEventEntity entity = itPositionEventEntity.next();
            PositionEvent pe = new PositionEvent(
                    entity.getIdEvent(),
                    entity.getIdOwner(),
                    entity.getName(),
                    entity.getTimestamp(),
                    entity.getLatitude(),
                    entity.getLongitude()
                    );
            positionEvents.add(pe);
        }
        return positionEvents;
    }

    /**
     * Method to preload the session with events.
     * @throws CollisionServiceException when the insertion fails.
     */
    public void preloadSession()
            throws CollisionServiceException, IllegalArgumentException {
        LOGGER.info("Entering preloadSession...");

        LOGGER.info("Setting state to {} ...", CollisionServiceState.LOADING_EVENTS.toString());
        state = CollisionServiceState.LOADING_EVENTS;

        LOGGER.debug("Creating dbPositionEventRuleRuntimeListener (inactive)...");
        dbPositionEventRuleRuntimeListener = new DBPositionEventRuleRuntimeListener(
                positionEventEntityRepository, false);

        LOGGER.debug("Getting stored position events...");
        List<PositionEvent> positionEvents = getPositionEventsFromDB();
        if (!positionEvents.isEmpty()) {
            LOGGER.debug("Inserting position events into kession...");
            insertPositionEvents(positionEvents);
        }


        LOGGER.debug("Setting dbPositionEventRuleRuntimeListener.active to true  ...");
        dbPositionEventRuleRuntimeListener.setActive(true);

        LOGGER.debug("Adding dbPositionEventRuleRuntimeListener to kieSession...");
        this.kieSession.addEventListener(dbPositionEventRuleRuntimeListener);

        LOGGER.info("Setting state to {} ...", CollisionServiceState.READY.toString());
        state = CollisionServiceState.READY;
    }

    /**
     * Method to insert position events.
     * @param positionEvents a list of PositionEvent objects.
     * @return number of events inserted into the ksession.
     * @throws CollisionServiceException when the insertion fails.
     * @throws IllegalArgumentException when the positionEvents is invalid.
     */
    @Override
    public int insertPositionEvents(final List<PositionEvent> positionEvents)
            throws CollisionServiceException, IllegalArgumentException {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Entering insertPositionEvents...");
        }
        if (positionEvents == null)
            throw new IllegalArgumentException("positionEvents is null");
        if (positionEvents.isEmpty())
            throw new IllegalArgumentException("positionEvents is empty");
        if (!state.equals(CollisionServiceState.LOADING_EVENTS)
                && !state.equals(CollisionServiceState.READY))
            throw new CollisionServiceException("Service is in state:" + this.state);

        int registersInserted = 0;
        try {
            long fc = this.entryPoint.getFactCount();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("fc: {}", fc);
            }
            for (PositionEvent positionEvent: positionEvents) {
                if (!isPositionEventInArea(positionEvent)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Skipping {}", positionEvent.getIdEvent());
                    }
                    continue;
                }
                entryPoint.insert(positionEvent);
                registersInserted++;

                long advanceTime = positionEvent.getTimestamp().getTime() - clock.getCurrentTime();
                if (advanceTime > 0) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Advancing time by {}", advanceTime);
                    }
                    clock.advanceTime(advanceTime, TimeUnit.MILLISECONDS);
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Clock time: {}", sdf.format(clock.getCurrentTime()));
                    }
                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Not advancing time. transaction time: {}, clock time: {}",
                            positionEvent.getTimestamp().getTime(),
                            clock.getCurrentTime());
                    }
                }
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("Firing rules!");
                }
                kieSession.fireAllRules();
            }

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new CollisionServiceException("Error during fact insert", e);
        }

        return registersInserted;
    }

    /**
     * Method to determine if the positionEvent belongs to the configured area.
     * @param positionEvent the PositionEvent param.
     * @return boolean indicating whether the position event belongs to the configured area or not.
     * @throws IllegalArgumentException when the positionEvent para is invalid.
     */
    private boolean isPositionEventInArea(final PositionEvent positionEvent) throws IllegalArgumentException {
        if (positionEvent == null) {
            throw new IllegalArgumentException("positionEvent is null");
        }
        return areaProperties.getMinLatitude() <= positionEvent.getLatitude()
                && positionEvent.getLatitude() <= areaProperties.getMaxLatitude()
                && areaProperties.getMinLongitude() <= positionEvent.getLongitude()
                && positionEvent.getLongitude() <= areaProperties.getMaxLongitude();
    }
}
