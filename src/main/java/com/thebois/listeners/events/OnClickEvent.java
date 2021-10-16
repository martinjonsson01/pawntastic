package com.thebois.listeners.events;

/**
 * Sends out information about a value when notifier has been clicked.
 *
 * @param <TValue> The type of the value
 */
public class OnClickEvent<TValue> implements IEvent {

    private final TValue value;

    /**
     * Instantiates the event, providing the value.
     *
     * @param value The previous value
     */
    public OnClickEvent(final TValue value) {
        this.value = value;
    }

    public TValue getValue() {
        return value;
    }

}
