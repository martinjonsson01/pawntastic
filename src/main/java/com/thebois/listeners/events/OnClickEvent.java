package com.thebois.listeners.events;

/**
 * Contains information about a value that has been clicked.
 *
 * @param <TValue> The type of the value
 *
 * @author Mathias
 */
public class OnClickEvent<TValue> implements IEvent {

    private final TValue value;

    /**
     * Instantiates the event, providing the value.
     *
     * @param value The value
     */
    public OnClickEvent(final TValue value) {
        this.value = value;
    }

    public TValue getValue() {
        return value;
    }

}
