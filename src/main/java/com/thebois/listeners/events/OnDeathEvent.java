package com.thebois.listeners.events;

import com.thebois.models.beings.IBeing;

/**
 * Contains information about a being that has died.
 */
public class OnDeathEvent {

    private final IBeing deadBeing;

    /**
     * Instantiate event with the dead being.
     *
     * @param deadBeing The being that has died.
     */
    public OnDeathEvent(final IBeing deadBeing) {
        this.deadBeing = deadBeing;
    }

    /**
     * Gets the dead being.
     *
     * @return The being that has died.
     */
    public IBeing getDeadBeing() {
        return deadBeing;
    }

}
