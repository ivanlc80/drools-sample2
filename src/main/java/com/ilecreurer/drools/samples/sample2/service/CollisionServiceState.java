package com.ilecreurer.drools.samples.sample2.service;

/**
 * CollisionServiceState enum.
 * @author ilecreurer.
 */
public enum CollisionServiceState {
    /**
     * Stopped.
     */
    STOPPED("stopped"),
    /**
     * Starting.
     */
    STARTING("starting"),
    /**
     * Loading events.
     */
    LOADING_EVENTS("loading events"),
    /**
     * Ready.
     */
    READY("ready");

    /**
     * The text.
     */
    private final String text;

    /**
     * Constructor.
     * @param textParam the text param.
     */
    CollisionServiceState(final String textParam) {
        this.text = textParam;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
