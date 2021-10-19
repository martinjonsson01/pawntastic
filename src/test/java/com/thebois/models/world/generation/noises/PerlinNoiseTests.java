package com.thebois.models.world.generation.noises;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.*;

public class PerlinNoiseTests {

    public static Stream<Arguments> getPositions() {
        return Stream.of(
            Arguments.of(1.0f, 1.0f),
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
        return Stream.of(
            Arguments.of(1.0f, 1.0f, 1.0f, 0),
            Arguments.of(1.0f, 1.23f, -5.0f, 2.23f),
            Arguments.of(4, 4.5f, 0, 4.5f),
            Arguments.of(0, 0, 1, -1),
            Arguments.of(-1.0f, -123.1253f, 1.4f, 155),
            Arguments.of(20.1235f, -7.534f, 1, 1),
            Arguments.of(0, 1.23f, 1452, 0),
            Arguments.of(-1.5615f, 0, 0.0523f, -0.5662f)

        );
    }

    public static Stream<Arguments> getPerlinNoiseSettingsParameters() {
        return Stream.of(
            Arguments.of(1, 1f, 1f, 1f),
            Arguments.of(1, 1.23f, 1.234f, 0.23423f),
            Arguments.of(4, 4.5f, 0.001f, 0.9f),
            Arguments.of(4, 4.5f, 0.001f, 0));
    }

    @ParameterizedTest
    @MethodSource("getPositions")
    public void perlinNoiseSameResultWithSameSamplePosition(
        final float x, final float y) {
        // Arrange
        final PerlinNoise perlinNoise = new PerlinNoise(1, 1, 1, 1);

        // Act
        final float heightValue1 = perlinNoise.sample(x, y);
        final float heightValue2 = perlinNoise.sample(x, y);

        // Assert
        assertThat(heightValue1).isEqualTo(heightValue2);
    }

    @ParameterizedTest
    @MethodSource("getPerlinNoiseSettingsParameters")
    public void perlinNoiseSameResultWithSameParameters(
        final int octave, final float amplitude, final float frequency, final float persistence) {
        // Arrange
        final PerlinNoise perlinNoise = new PerlinNoise(octave, amplitude, frequency, persistence);
        final int x = 1;
        final int y = 1;

        // Act
        final float heightValue1 = perlinNoise.sample(x, y);
        final float heightValue2 = perlinNoise.sample(x, y);

        // Assert
        assertThat(heightValue1).isEqualTo(heightValue2);
    }

    @ParameterizedTest
    @MethodSource("getTwoDifferentPositions")
    public void perlinNoiseDifferentResultWithDifferentValues(
        final float x1, final float y1, final float x2, final float y2) {
        // Arrange
        final PerlinNoise perlinNoise = new PerlinNoise(1, 1, 1, 1);

        // Act
        final float heightValue1 = perlinNoise.sample(x1, y1);
        final float heightValue2 = perlinNoise.sample(x2, y2);

        // Assert
        assertThat(heightValue1).isNotEqualTo(heightValue2);
    }

    @ParameterizedTest
    @MethodSource("getPositions")
    public void perlinNoiseDifferentResultWithSameValuesAndDifferentSeed(
        final float x, final float y) {
        // Arrange
        final PerlinNoise perlinNoise = new PerlinNoise(1, 1, 1, 1);
        final int seed1 = 0;
        final int seed2 = 1;
        perlinNoise.setSeed(seed1);

        // Act
        final float heightValue1 = perlinNoise.sample(x, y);
        perlinNoise.setSeed(seed2);
        final float heightValue2 = perlinNoise.sample(x, y);

        // Assert
        assertThat(heightValue1).isNotEqualTo(heightValue2);
    }

    @ParameterizedTest
    @MethodSource("getPositions")
    public void perlinNoiseDifferentResultWithSameValuesAndDifferentSettings(
        final float x, final float y) {
        // Arrange
        final PerlinNoise perlinNoise1 = new PerlinNoise(1, 1, 1, 1);
        final PerlinNoise perlinNoise2 = new PerlinNoise(2, 2, 2, 2);

        // Act
        final float heightValue1 = perlinNoise1.sample(x, y);
        final float heightValue2 = perlinNoise2.sample(x, y);

        // Assert
        assertThat(heightValue1).isNotEqualTo(heightValue2);
    }

    @ParameterizedTest
    @MethodSource("getPositions")
    public void perlinNoiseReturnsZeroIfAmplitudeIs0(final float x, final float y) {
        // Arrange
        final PerlinNoise perlinNoise = new PerlinNoise(1, 0, 1, 1);
        final float expectedHeightValue = 0;

        // Act
        final float heightValue = perlinNoise.sample(x, y);

        // Assert
        assertThat(heightValue).isEqualTo(expectedHeightValue);
    }

    @ParameterizedTest
    @MethodSource("getPositions")
    public void perlinNoiseReturnsZeroIfOctaveIs0(final float x, final float y) {
        // Arrange
        final PerlinNoise perlinNoise = new PerlinNoise(0, 1, 1, 1);
        final float expectedHeightValue = 0;

        // Act
        final float heightValue = perlinNoise.sample(x, y);

        // Assert
        assertThat(heightValue).isEqualTo(expectedHeightValue);
    }

    @ParameterizedTest
    @MethodSource("getPositions")
    public void perlinNoiseReturnsNotZeroIfFrequencyIs0(final float x, final float y) {
        // Arrange
        final PerlinNoise perlinNoise = new PerlinNoise(1, 1, 0, 1);

        // Act
        final float heightValue = perlinNoise.sample(x, y);

        // Assert
        assertThat(heightValue).isNotEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("getPositions")
    public void perlinNoiseReturnsNotZeroIfPersistenceIs0(final float x, final float y) {
        // Arrange
        final PerlinNoise perlinNoise = new PerlinNoise(1, 1, 1, 0);

        // Act
        final float heightValue = perlinNoise.sample(x, y);

        // Assert
        assertThat(heightValue).isNotEqualTo(0);
    }

    @ParameterizedTest
    @MethodSource("getPositions")
    public void perlinNoiseReturnsSameResultIfPersistenceIs0AndOctavesIsDifferentAndRestSame(
        final float x, final float y) {
        // Arrange
        final PerlinNoise perlinNoise1 = new PerlinNoise(3, 1, 1, 0);
        final PerlinNoise perlinNoise2 = new PerlinNoise(2, 1, 1, 0);

        // Act
        final float heightValue1 = perlinNoise1.sample(x, y);
        final float heightValue2 = perlinNoise2.sample(x, y);

        // Assert
        assertThat(heightValue1).isEqualTo(heightValue2);
    }

}
