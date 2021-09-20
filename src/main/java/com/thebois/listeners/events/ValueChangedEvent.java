package com.thebois.listeners.events;

/**
 * Contains information about a value that has changed.
 *
 * @param <TValue> The type of the value
 */
public class ValueChangedEvent<TValue> implements IEvent {

    private final TValue newValue;

    /**
     * Instantiates the event, providing the new value.
     *
     * @param newValue The new value
     */
    public ValueChangedEvent(final TValue newValue) {
        this.newValue = newValue;
    }

    public TValue getNewValue() {
        return newValue;
    }

}
