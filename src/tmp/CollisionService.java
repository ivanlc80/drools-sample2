package com.ilecreurer.drools.samples.sample2.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.EntryPoint;
import org.kie.api.time.SessionPseudoClock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

/**
 * CollisionService class.
 * @author ilecreurer.
 */
@Service
@Scope("singleton")
public class CollisionService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(CollisionService.class);

    /**
     * Current state of the engine.
     */
    private CollisionServiceState state = CollisionServiceState.STOPPED;

    /**
     * SimpleDateFormat object.
     */
    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy'T'HH:mm:ss.SSSZ");

    //@Autowired
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
     * Initialization method.
     */
    //@PostConstruct
    void onStart() {
        try {
            state = CollisionServiceState.STARTING;
            LOGGER.info("The application is starting...");

            LOGGER.info("kieContainer is null: {}", (kieContainer == null));

            /*
            LOGGER.info("The application is starting...");

            LOGGER.info("Creating ks...");
            KieServices ks = KieServices.Factory.get();

            LOGGER.info("Creating kc...");
            KieContainer kc = ks.getKieClasspathContainer();

            LOGGER.info("Creating kieSession...");
            this.kieSession = kc.newKieSession("ksessionceprules");

            //this.kieSession.setGlobal("LOGGER_DRL", LOGGER_DRL);


            LOGGER.info("Creating entryPoint...");
            this.entryPoint = this.kieSession.getEntryPoint("PositionEventStream");

            clock = (SessionPseudoClock)this.kieSession.getSessionClock();

            long currentTime = clock.getCurrentTime();
            LOGGER.debug("PseudoClock starting at {}", sdf.format(new Date(currentTime)));
*/
            state = CollisionServiceState.READY;
            LOGGER.info("The application is ready.");
        } catch (Exception e) {
            state = CollisionServiceState.STOPPED;
            LOGGER.error("Failed to start the knowledge session.", e);
        }
    }


    /**
     * Finalization method.
     */
    //@PreDestroy
    void onStop() {
        /*try {
            if (state.equals(CollisionServiceState.READY)) {
                state = CollisionServiceState.SAVING_SESSION;
                LOGGER.info("The application is saving session...");

                this.kieSession.dispose();
            }

            state = CollisionServiceState.STOPPED;
            LOGGER.info("The application is stopped...");

        } catch (Exception e) {
            state = CollisionServiceState.STOPPED;
            LOGGER.error("Failed to stop the knowledge session.", e);
        }*/

    }

    /**
     * Method to insert the position events into the knowledge session.
     * @param positionEvents
     */
    public void insertPositionEvents(List<PositionEvent> positionEvents) {
        LOGGER.info("Entering insertPositionEvents...");



    }

}
