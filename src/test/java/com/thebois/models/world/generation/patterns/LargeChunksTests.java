package com.thebois.models.world.generation.patterns;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class LargeChunksTests {

    @Test
    public void getOctavesReturnsCorrectValue() {
        // Arrange
        final IGenerationPattern actualSettings = new LargeChunks();
        final int expectedOctave = 4;
        final int actualOctave;

        // Act
        actualOctave = actualSettings.getOctave();

        // Assert
        assertThat(actualOctave).isEqualTo(expectedOctave);
    }

    @Test
    public void getFrequencyReturnsCorrectValue() {
        // Arrange
        final IGenerationPattern actualSettings = new LargeChunks();
        final float expectedFrequency = 0.05f;
        final float actualFrequency;

        // Act
        actualFrequency = actualSettings.getFrequency();

        // Assert
        assertThat(actualFrequency).isEqualTo(expectedFrequency);
    }

    @Test
    public void getAmplitudeReturnsCorrectValue() {
        // Arrange
        final IGenerationPattern actualSettings = new LargeChunks();
        final float expectedAmplitude = 4.0f;
        final float actualFrequency;

        // Act
        actualFrequency = actualSettings.getAmplitude();

        // Assert
        assertThat(actualFrequency).isEqualTo(expectedAmplitude);
    }

    @Test
    public void getPersistenceReturnsCorrectValue() {
        // Arrange
        final IGenerationPattern actualSettings = new LargeChunks();
        final float expectedPersistence = 1.0f;
        final float actualPersistence;

        // Act
        actualPersistence = actualSettings.getPersistence();

        // Assert
        assertThat(actualPersistence).isEqualTo(expectedPersistence);
    }

}
