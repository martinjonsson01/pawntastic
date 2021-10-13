package com.thebois.models.world.generation;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.world.terrains.ITerrain;
import com.thebois.models.world.terrains.TerrainType;

import static org.assertj.core.api.Assertions.*;

public class TerrainGeneratorTest {

    public static Stream<Arguments> getWorldSizeAndOneSeed() {
        return Stream.of(Arguments.of(50, 40),
                         Arguments.of(17, 72),
                         Arguments.of(25, 5),
                         Arguments.of(15, 100));
    }

    public static Stream<Arguments> getWorldSizeAndTwoSeeds() {
        return Stream.of(Arguments.of(8, 4, 8),
                         Arguments.of(50, 40, 5234),
                         Arguments.of(25, 5, 615523),
                         Arguments.of(10, 100, 0));
    }

    @ParameterizedTest
    @MethodSource("getWorldSizeAndTwoSeeds")
    public void generatingTerrainReturnDifferentMatrixIfSeedIsDifferent(
        final int worldSize, final int seed1, final int seed2) {
        // Arrange
        final TerrainGenerator generator1 = new TerrainGenerator(worldSize, seed1);
        final TerrainGenerator generator2 = new TerrainGenerator(worldSize, seed2);
        final ITerrain[][] matrix1;
        final ITerrain[][] matrix2;
        final boolean isEqual;

        // Act
        matrix1 = generator1.generateTerrainMatrix();
        matrix2 = generator2.generateTerrainMatrix();
        isEqual = Arrays.deepEquals(matrix1, matrix2);
        // Assert
        // Assert did not have an IsNotDeepEqual()
        assertThat(isEqual).isFalse();
    }

    @ParameterizedTest
    @MethodSource("getWorldSizeAndOneSeed")
    public void generatingTerrainReturnSameMatrixIfSeedIsSame(
        final int worldSize, final int seed) {
        // Arrange
        final TerrainGenerator generator1 = new TerrainGenerator(worldSize, seed);
        final TerrainGenerator generator2 = new TerrainGenerator(worldSize, seed);
        final ITerrain[][] matrix1;
        final ITerrain[][] matrix2;
        final boolean isEqual;

        // Act
        matrix1 = generator1.generateTerrainMatrix();
        matrix2 = generator2.generateTerrainMatrix();
        isEqual = Arrays.deepEquals(matrix1, matrix2);

        // Assert
        assertThat(matrix1).isDeepEqualTo(matrix2);
        assertThat(isEqual).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getWorldSizeAndOneSeed")
    public void generatedTerrainMatrixContainsAllKindsOfTerrains(
        final int worldSize, final int seed) {
        // Arrange
        final TerrainGenerator generator = new TerrainGenerator(worldSize, seed);
        final ITerrain[][] matrix;
        final AtomicInteger actualNumberOfTerrains = new AtomicInteger(0);
        final int expectedNumberOfResources = TerrainType.values().length;
        // Act
        matrix = generator.generateTerrainMatrix();
        for (final TerrainType type : TerrainType.values()) {
            if (containsTerrain(matrix, type)) {
                actualNumberOfTerrains.incrementAndGet();
            }
        }
        // Assert
        assertThat(actualNumberOfTerrains.get()).isEqualTo(expectedNumberOfResources);
    }

    private boolean containsTerrain(
        final ITerrain[][] matrix, final TerrainType typeToSearchFor) {
        for (final ITerrain[] terrains : matrix) {
            for (final ITerrain resource : terrains) {
                if (resource != null) {
                    if (resource.getType() == typeToSearchFor) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
