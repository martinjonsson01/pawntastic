package com.thebois.utils;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

public class PerlinNoiseTests {

    public static Stream<Arguments> getPositions() {
        return Stream.of(Arguments.of(1.0f, 1.0f),
                         Arguments.of(1.0f, 1.23f),
                         Arguments.of(4, 4.5f),
                         Arguments.of(0, 0),
                         Arguments.of(-1.0f, -123.1253f),
                         Arguments.of(20.1235f, -7.534f),
                         Arguments.of(0, 1.23f),
                         Arguments.of(-1.5615f, 0)

        );
    }

    public static Stream<Arguments> getTwoDifferentPositions() {
        return Stream.of(Arguments.of(1.0f, 1.0f, 1.0f, 0),
                         Arguments.of(1.0f, 1.23f, -5.0f, 2.23f),
                         Arguments.of(4, 4.5f, 0, 4.5f),
                         Arguments.of(0, 0, 1, -1),
                         Arguments.of(-1.0f, -123.1253f, 1.4f, 155),
                         Arguments.of(20.1235f, -7.534f, 1, 1),
                         Arguments.of(0, 1.23f, 1452, 0),
                         Arguments.of(-1.5615f, 0, 0.0523f, -0.5662f)

        );
    }

    public static Stream<Arguments> getDoubles() {
        return Stream.of(Arguments.of(1.0f),
                         Arguments.of(-1.0f),
                         Arguments.of(0f),
                         Arguments.of(1234.12356445743f),
                         Arguments.of(-1234.12356445743f)

        );
    }

    public static Stream<Arguments> getPositiveIntegers() {
        return Stream.of(Arguments.of(1),
                         Arguments.of(10),
                         Arguments.of(20),
                         Arguments.of(1234),
                         Arguments.of(1234745)

        );
    }

    public static Stream<Arguments> getNegativeIntegersAnd0() {
        return Stream.of(Arguments.of(-1),
                         Arguments.of(-10),
                         Arguments.of(-20),
                         Arguments.of(-1234),
                         Arguments.of(-1234745),
                         Arguments.of(0)

        );
    }

    public static Stream<Arguments> getPerlinNoiseSettingsParameters() {
        return Stream.of(Arguments.of(1, 1f, 1f, 1f, 1),
                         Arguments.of(1, 1.23f, 1.234f, 0.23423f, 20505054),
                         Arguments.of(4, 4.5f, 0.001f, 0.9f, 20)

        );
    }

    @ParameterizedTest
    @MethodSource("getPositions")
    public void perlinNoiseSameResultWithSameValues(final float x, final float y) {
        // Arrange
        final PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator();
        final float heightValue1;
        final float heightValue2;

        // Act
        heightValue1 = perlinNoiseGenerator.perlinNoise(x, y);
        heightValue2 = perlinNoiseGenerator.perlinNoise(x, y);

        // Assert
        assertThat(heightValue1).isEqualTo(heightValue2);
    }

    @ParameterizedTest
    @MethodSource("getTwoDifferentPositions")
    public void perlinNoiseDifferentResultWithDifferentValues(final float x1,
                                                              final float y1,
                                                              final float x2,
                                                              final float y2) {
        // Arrange
        final PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator();
        final float heightValue1;
        final float heightValue2;

        // Act
        heightValue1 = perlinNoiseGenerator.perlinNoise(x1, y1);
        heightValue2 = perlinNoiseGenerator.perlinNoise(x2, y2);

        // Assert
        assertThat(heightValue1).isNotEqualTo(heightValue2);
    }

    // Getters, Setters and Constructor.

    @ParameterizedTest
    @MethodSource("getPerlinNoiseSettingsParameters")
    public void perlinNoiseConstructor(final int octaves,
                                       final float amplitude,
                                       final float frequency,
                                       final float persistence,
                                       final int seed) {
        // Arrange
        final PerlinNoiseGenerator perlinNoiseGenerator;

        // Act
        perlinNoiseGenerator = new PerlinNoiseGenerator(octaves,
                                                        amplitude,
                                                        frequency,
                                                        persistence,
                                                        seed);
        // Assert
        assertThat(perlinNoiseGenerator.getAmplitude()).as("Amplitude").isEqualTo(amplitude);
        assertThat(perlinNoiseGenerator.getOctaves()).as("Octaves").isEqualTo(octaves);
        assertThat(perlinNoiseGenerator.getFrequency()).as("Frequency").isEqualTo(frequency);
        assertThat(perlinNoiseGenerator.getPersistence()).as("Persistence").isEqualTo(persistence);
        assertThat(perlinNoiseGenerator.getSeed()).as("Seed").isEqualTo(seed);
    }

    @ParameterizedTest
    @MethodSource("getDoubles")
    public void perlinNoiseSetAmplitude(final float amplitude) {
        // Arrange
        final PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator();

        // Act
        perlinNoiseGenerator.setAmplitude(amplitude);

        // Assert
        assertThat(perlinNoiseGenerator.getAmplitude()).isEqualTo(amplitude);
    }

    @ParameterizedTest
    @MethodSource("getDoubles")
    public void perlinNoiseSetFrequency(final float frequency) {
        // Arrange
        final PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator();

        // Act
        perlinNoiseGenerator.setFrequency(frequency);

        // Assert
        assertThat(perlinNoiseGenerator.getFrequency()).isEqualTo(frequency);
    }

    @ParameterizedTest
    @MethodSource("getDoubles")
    public void perlinNoiseSetPersistence(final float persistence) {
        // Arrange
        final PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator();

        // Act
        perlinNoiseGenerator.setPersistence(persistence);

        // Assert
        assertThat(perlinNoiseGenerator.getPersistence()).isEqualTo(persistence);
    }

    @ParameterizedTest
    @MethodSource("getPositiveIntegers")
    public void perlinNoiseSetPositiveOctaves(final int octaves) {
        // Arrange
        final PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator();

        // Act
        perlinNoiseGenerator.setOctaves(octaves);

        // Assert
        assertThat(perlinNoiseGenerator.getOctaves()).isEqualTo(octaves);
    }

    @ParameterizedTest
    @MethodSource({ "getPositiveIntegers", "getNegativeIntegersAnd0" })
    public void perlinNoiseSetSeed(final int seed) {
        // Arrange
        final PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator();

        // Act
        perlinNoiseGenerator.setSeed(seed);

        // Assert
        assertThat(perlinNoiseGenerator.getSeed()).isEqualTo(seed);
    }

    @ParameterizedTest
    @MethodSource("getNegativeIntegersAnd0")
    public void perlinNoiseSetNegativeOctaves(final int octaves) {
        // Arrange
        final PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator();
        boolean threwException;

        // Act
        try {
            perlinNoiseGenerator.setOctaves(octaves);
            threwException = false;
        }
        catch (final IllegalArgumentException illegalArgumentException) {
            threwException = true;
        }

        // Assert
        assertThat(threwException).isTrue();
    }

}
