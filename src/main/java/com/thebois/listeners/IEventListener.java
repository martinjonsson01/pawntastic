package com.thebois.listeners;

import com.thebois.listeners.events.IEvent;

/**
 * Represents a way of being notified of new events.
 *
 * @param <TEvent> The type of the events to listen for
 *
 * @author Martin
 */
public interface IEventListener<TEvent extends IEvent> {

    /**
     * Notify the listener that a new event has occurred.
     *
     * @param event The event that has occurred
     */
    void onEvent(TEvent event);

}
