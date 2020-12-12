package com.ilecreurer.drools.samples.sample2.service;

/**
 * CollisionServiceState enum.
 * @author ilecreurer.
 */
public enum CollisionServiceState {
    STOPPED("stopped"),
    STARTING("starting"),
    CHECKING_EXISTING_SESSION("checking existing session"),
    LOADING_EXISTING_SESSION("loading existing session"),
    LOADING_NEW_SESSION("loading new session"),
    READY("ready"),
    PROCESSING("processing"),
    SAVING_SESSION("saving session");

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
