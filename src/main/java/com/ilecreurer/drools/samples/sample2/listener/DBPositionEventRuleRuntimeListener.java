package com.ilecreurer.drools.samples.sample2.listener;

import org.kie.api.event.rule.DefaultRuleRuntimeEventListener;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ilecreurer.drools.samples.sample2.entity.PositionEventEntity;
import com.ilecreurer.drools.samples.sample2.entity.PositionEventEntityRepository;
import com.ilecreurer.drools.samples.sample2.event.PositionEvent;

/**
 * DBPositionEventRuleRuntimeListener class.
 * @author ilecreurer
 */
public class DBPositionEventRuleRuntimeListener extends DefaultRuleRuntimeEventListener {
    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(DBPositionEventRuleRuntimeListener.class);

    /**
     * PositionEventEntityRepository attribute.
     */
    private PositionEventEntityRepository positionEventEntityRepository;

    /**
     * Constructor.
     * @param positionEventEntityRepository the repository.
     */
    public DBPositionEventRuleRuntimeListener(
            final PositionEventEntityRepository positionEventEntityRepository)
                    throws IllegalArgumentException {
        if (positionEventEntityRepository == null) {
            throw new IllegalArgumentException("positionEventEntityRepository is null");
        }
        this.positionEventEntityRepository = positionEventEntityRepository;
    }

    /**
     * Method called when an object is inserted into the knowledge session.
     */
    public void objectInserted(ObjectInsertedEvent event) {
        if (event.getObject() instanceof PositionEvent) {
            try {
                PositionEvent pe = (PositionEvent)event.getObject();
                LOGGER.debug("Inserting event: {}", pe.getIdEvent());
                PositionEventEntity entity = new PositionEventEntity(
                        pe.getIdEvent(),
                        pe.getIdOwner(),
                        pe.getType(),
                        pe.getTimestamp(),
                        pe.getLatitude(),
                        pe.getLongitude()
                );
                positionEventEntityRepository.save(entity);
            } catch (Exception e) {
                LOGGER.error("Failed to insert entity", e);
            }
        }
    }

    /**
     * Method called when an object is deleted from the knowledge session.
     */
    public void objectDeleted(ObjectDeletedEvent event) {
        if (event.getOldObject() instanceof PositionEvent) {
            try {
                PositionEvent pe = (PositionEvent)event.getOldObject();
                LOGGER.debug("Deleting event: {}", pe.getIdEvent());
                positionEventEntityRepository.deleteById(pe.getIdEvent());
            } catch (Exception e) {
                LOGGER.error("Failed to delete entity", e);
            }
        }
    }

    /**
     * Method called when an object is updated in the knowledge session.
     */
    public void objectUpdated(ObjectUpdatedEvent event) {
        if (event.getObject() instanceof PositionEvent) {
            try {
                PositionEvent pe = (PositionEvent)event.getObject();
                LOGGER.debug("Updating event: {}", pe.getIdEvent());
                PositionEventEntity entity = new PositionEventEntity(
                        pe.getIdEvent(),
                        pe.getIdOwner(),
                        pe.getType(),
                        pe.getTimestamp(),
                        pe.getLatitude(),
                        pe.getLongitude()
                );
                positionEventEntityRepository.save(entity);
            } catch (Exception e) {
                LOGGER.error("Failed to delete entity", e);
            }
        }
    }
}
