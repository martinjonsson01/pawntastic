package com.thebois.listeners.events;

/**
 * Contains information about a value that has changed.
 *
 * @param <TValue> The type of the value
 *
 * @author Martin
 */
public class ValueChangedEvent<TValue> implements IEvent {

    private final TValue oldValue;
    private final TValue newValue;

    /**
     * Instantiates the event, providing the new value.
     *
     * @param oldValue The previous value
     * @param newValue The new value
     */
    public ValueChangedEvent(final TValue oldValue, final TValue newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public TValue getNewValue() {
        return newValue;
    }

    public TValue getOldValue() {
        return oldValue;
    }

}
