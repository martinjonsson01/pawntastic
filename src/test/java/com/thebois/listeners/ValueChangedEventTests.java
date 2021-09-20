package com.thebois.listeners;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ValueChangedEventTests {

    @Test
    public void getNewValueReturnsNewValue() {
        // Arrange
        final int newValue = 123;
        final ValueChangedEvent<Integer> event = new ValueChangedEvent<>(newValue);

        // Act
        final int actualValue = event.getNewValue();

        // Assert
        assertThat(actualValue).isEqualTo(newValue);
    }

}
