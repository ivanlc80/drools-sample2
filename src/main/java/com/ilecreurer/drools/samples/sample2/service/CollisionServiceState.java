package com.ilecreurer.drools.samples.sample2.service;

/**
 * CollisionServiceState enum.
 * @author ilecreurer.
 */
public enum CollisionServiceState {
    STOPPED("stopped"),
    STARTING("starting"),
    LOADING_EVENTS("loading events"),
    READY("ready"),
    PROCESSING("processing");

    /**
     * The text.
     */
    private final String text;

    /**
     * Constructor.
     * @param text the text param.
     */
    CollisionServiceState(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
