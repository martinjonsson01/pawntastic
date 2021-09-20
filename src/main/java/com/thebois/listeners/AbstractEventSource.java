package com.thebois.listeners;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Represents a source of events that can be listened to.
 *
 * @param <TEvent> The type of the events generated
 */
public abstract class AbstractEventSource<TEvent extends AbstractEvent> {

    private final Collection<IEventListener<TEvent>> listeners = new ArrayList<>();

    /**
     * Registers a new listener that will be notified about new events.
     *
     * @param listener The listener to register
     */
    public void addListener(final IEventListener<TEvent> listener) {
        listeners.add(listener);
    }

    /**
     * Unregisters a listener so that it will no longer be notified about new events.
     *
     * @param listener The listener to unregister
     *
     * @throws IllegalArgumentException When trying to unregister a listener that is not registered
     */
    public void removeListener(final IEventListener<TEvent> listener) {
        if (!listeners.contains(listener)) {
            throw new IllegalArgumentException("Can not remove listener that is not listening");
        }
        listeners.remove(listener);
    }

    /**
     * Sends a new event to all registered listeners.
     *
     * @param event The event to send out to the listeners
     */
    public void send(final TEvent event) {
        for (final IEventListener<TEvent> listener : listeners) {
            listener.onEvent(event);
        }
    }

}
