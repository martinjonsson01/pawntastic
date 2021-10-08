package com.thebois.utils;

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

    public static Stream<Arguments> getDoubles() {
        return Stream.of(
            Arguments.of(1.0f),
            Arguments.of(-1.0f),
            Arguments.of(0f),
            Arguments.of(1234.12356445743f),
            Arguments.of(-1234.12356445743f)

        );
    }

    public static Stream<Arguments> getPositiveIntegers() {
        return Stream.of(
            Arguments.of(1),
            Arguments.of(10),
            Arguments.of(20),
            Arguments.of(1234),
            Arguments.of(1234745)

        );
    }

    public static Stream<Arguments> getNegativeIntegersAnd0() {
        return Stream.of(
            Arguments.of(-1),
            Arguments.of(-10),
            Arguments.of(-20),
            Arguments.of(-1234),
            Arguments.of(-1234745),
            Arguments.of(0)

        );
    }

    public static Stream<Arguments> getPerlinNoiseSettingsParameters() {
        return Stream.of(
            Arguments.of(1, 1f, 1f, 1f, 1),
            Arguments.of(1, 1.23f, 1.234f, 0.23423f, 20505054),
            Arguments.of(4, 4.5f, 0.001f, 0.9f, 20)

        );
    }

    @ParameterizedTest
    @MethodSource("getPositions")
    public void perlinNoiseSameResultWithSameValues(final float posX, final float posY) {
        // Arrange
        final PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator();
        final float heightValue1;
        final float heightValue2;

        // Act
        heightValue1 = perlinNoiseGenerator.perlinNoise(posX, posY);
        heightValue2 = perlinNoiseGenerator.perlinNoise(posX, posY);

        // Assert
        assertThat(heightValue1).isEqualTo(heightValue2);
    }

    @ParameterizedTest
    @MethodSource("getTwoDifferentPositions")
    public void perlinNoiseDifferentResultWithDifferentValues(
        final float posX1, final float posY1, final float posX2, final float posY2) {
        // Arrange
        final PerlinNoiseGenerator perlinNoiseGenerator = new PerlinNoiseGenerator();
        perlinNoiseGenerator.setSeed(0);
        final float heightValue1;
        final float heightValue2;

        // Act
        heightValue1 = perlinNoiseGenerator.perlinNoise(posX1, posY1);
        heightValue2 = perlinNoiseGenerator.perlinNoise(posX2, posY2);

        // Assert
        assertThat(heightValue1).isNotEqualTo(heightValue2);
    }

}
