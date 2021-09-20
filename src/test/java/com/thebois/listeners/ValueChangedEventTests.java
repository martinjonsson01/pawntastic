package com.thebois.listeners;

import org.junit.jupiter.api.Test;

import com.thebois.listeners.events.ValueChangedEvent;

import static org.assertj.core.api.Assertions.*;

public class ValueChangedEventTests {

    @Test
    public void getNewValueReturnsNewValue() {
        // Arrange
        final int newValue = 123;
        final ValueChangedEvent<Integer> event = new ValueChangedEvent<>(0, newValue);

        // Act
        final int actualValue = event.getNewValue();

        // Assert
        assertThat(actualValue).isEqualTo(newValue);
    }

    @Test
    public void getOldValueReturnsOldValue() {
        // Arrange
        final int oldValue = 123;
        final ValueChangedEvent<Integer> event = new ValueChangedEvent<>(oldValue, 0);

        // Act
        final int actualValue = event.getOldValue();

        // Assert
        assertThat(actualValue).isEqualTo(oldValue);
    }

}
