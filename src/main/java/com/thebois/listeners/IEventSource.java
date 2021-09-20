package com.thebois.listeners;

/**
 * Represents a source of events that can be listened to.
 *
 * @param <TEvent> The type of the events generated
 */
public interface IEventSource<TEvent extends AbstractEvent> {

    /**
     * Registers a new listener that will be notified about new events.
     *
     * @param listener The listener to register
     */
    void addListener(IEventListener<TEvent> listener);

    /**
     * Unregisters a listener so that it will no longer be notified about new events.
     *
     * @param listener The listener to unregister
     *
     * @throws IllegalArgumentException When trying to unregister a listener that is not registered
     */
    void removeListener(IEventListener<TEvent> listener);

}
