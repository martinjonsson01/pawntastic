package com.thebois.listeners;

import java.io.Serializable;

import com.google.common.eventbus.EventBus;

/**
 * Allows access to an eventbus.
 *
 * @author Jacob
 * @author Martin
 */
@FunctionalInterface
public interface IEventBusSource extends Serializable {

    /**
     * Returns an eventbus.
     *
     * @return Returns an eventbus.
     */
    EventBus getEventBus();

}
