package com.thebois.listeners;

/**
 * Represents a way of being notified of new events.
 *
 * @param <TEvent> The type of the events to listen for
 */
public interface IEventListener<TEvent extends AbstractEvent> {

    /**
     * Notify the listener that a new event has occurred.
     *
     * @param event The event that has occurred
     */
    void onEvent(TEvent event);

}
