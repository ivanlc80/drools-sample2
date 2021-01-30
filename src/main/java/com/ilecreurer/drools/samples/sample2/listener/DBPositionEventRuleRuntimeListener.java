package com.ilecreurer.drools.samples.sample2.listener;

import org.kie.api.event.rule.DefaultRuleRuntimeEventListener;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     * Determines if the listener is active or passive.
     * <br>if active then the insertions take place.
     * <br>if passive then the insertions are not done.
     */
    private boolean active;

    /**
     * Constructor.
     * @param positionEventEntityRepositoryParam the repository.
     * @param activeParam boolean indicating if storage is active.
     */
    public DBPositionEventRuleRuntimeListener(
            final PositionEventEntityRepository positionEventEntityRepositoryParam,
            final boolean activeParam)
                    throws IllegalArgumentException {
        if (positionEventEntityRepositoryParam == null) {
            throw new IllegalArgumentException("positionEventEntityRepositoryParam is null");
        }
        this.positionEventEntityRepository = positionEventEntityRepositoryParam;
        this.active = activeParam;
    }

    /**
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * @param activeParam the active to set
     */
    public void setActive(final boolean activeParam) {
        this.active = activeParam;
    }

    /**
     * Method called when an object is inserted into the knowledge session.
     * @param event the ObjectInsertedEvent.
     */
    public void objectInserted(final ObjectInsertedEvent event) {
        if (!active) {
            return;
        }
        if (event.getObject() instanceof PositionEvent) {
            try {
                PositionEvent pe = (PositionEvent) event.getObject();
                LOGGER.debug("Inserting event: {}", pe.getIdEvent());
                PositionEventEntity entity = new PositionEventEntity(
                        pe.getIdEvent(),
                        pe.getIdOwner(),
                        pe.getName(),
                        pe.getTimestamp(),
                        pe.getLatitude(),
                        pe.getLongitude()
                );
                positionEventEntityRepository.save(entity);
            } catch (Exception e) {
                LOGGER.error("Failed to insert entity: {}", e.getMessage());
            }
        }
    }

    /**
     * Method called when an object is deleted from the knowledge session.
     * @param event the ObjectDeletedEvent.
     */
    public void objectDeleted(final ObjectDeletedEvent event) {
        if (!active) {
            return;
        }
        if (event.getOldObject() instanceof PositionEvent) {
            try {
                PositionEvent pe = (PositionEvent) event.getOldObject();
                LOGGER.debug("Deleting event: {}", pe.getIdEvent());
                positionEventEntityRepository.deleteById(pe.getIdEvent());
            } catch (Exception e) {
                LOGGER.error("Failed to delete entity: {}", e.getMessage());
            }
        }
    }

    /**
     * Method called when an object is updated in the knowledge session.
     * @param event the ObjectUpdatedEvent.
     */
    public void objectUpdated(final ObjectUpdatedEvent event) {
        if (!active) {
            return;
        }
        if (event.getObject() instanceof PositionEvent) {
            try {
                PositionEvent pe = (PositionEvent) event.getObject();
                LOGGER.debug("Updating event: {}", pe.getIdEvent());
                PositionEventEntity entity = new PositionEventEntity(
                        pe.getIdEvent(),
                        pe.getIdOwner(),
                        pe.getName(),
                        pe.getTimestamp(),
                        pe.getLatitude(),
                        pe.getLongitude()
                );
                positionEventEntityRepository.save(entity);
            } catch (Exception e) {
                LOGGER.error("Failed to delete entity: {}", e.getMessage());
            }
        }
    }
}
