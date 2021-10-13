package com.thebois.models.world.generation;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceType;
import com.thebois.utils.MatrixUtils;

import static org.assertj.core.api.Assertions.*;

public class ResourceGeneratorTests {

    public static Stream<Arguments> getWorldSizeAndOneSeed() {
        return Stream.of(Arguments.of(2, 4),
                         Arguments.of(50, 40),
                         Arguments.of(25, 5),
                         Arguments.of(10, 100));
    }

    public static Stream<Arguments> getWorldSizeAndTwoSeeds() {
        return Stream.of(Arguments.of(2, 4, 8),
                         Arguments.of(50, 40, 5234),
                         Arguments.of(25, 5, 615523),
                         Arguments.of(10, 100, 0));
    }

    @ParameterizedTest
    @MethodSource("getWorldSizeAndTwoSeeds")
    public void generatingResourcesReturnDifferentMatrixIfSeedIsDifferent(
        final int worldSize, final int seed1, final int seed2) {
        // Arrange
        final ResourceGenerator generator1 = new ResourceGenerator(seed1);
        final ResourceGenerator generator2 = new ResourceGenerator(seed2);
        final IResource[][] matrix1;
        final IResource[][] matrix2;
        final boolean isEqual;

        // Act
        matrix1 = generator1.generateResourceMatrix(worldSize);
        matrix2 = generator2.generateResourceMatrix(worldSize);
        isEqual = Arrays.deepEquals(matrix1, matrix2);
        // Assert
        // Assert did not have an IsNotDeepEqual()
        assertThat(isEqual).isFalse();
    }

    @ParameterizedTest
    @MethodSource("getWorldSizeAndOneSeed")
    public void generatingResourcesReturnSameMatrixIfSeedIsSame(
        final int worldSize, final int seed) {
        // Arrange
        final ResourceGenerator generator1 = new ResourceGenerator(seed);
        final ResourceGenerator generator2 = new ResourceGenerator(seed);
        final IResource[][] matrix1;
        final IResource[][] matrix2;
        final boolean isEqual;

        // Act
        matrix1 = generator1.generateResourceMatrix(worldSize);
        matrix2 = generator2.generateResourceMatrix(worldSize);
        isEqual = Arrays.deepEquals(matrix1, matrix2);

        // Assert
        assertThat(matrix1).isDeepEqualTo(matrix2);
        assertThat(isEqual).isTrue();
    }

    @Test
    public void generatedResourceMatrixIsNotEmpty() {
        // Arrange
        final ResourceGenerator generator = new ResourceGenerator(0);
        final IResource[][] matrix;
        final AtomicBoolean moreThanZeroResources = new AtomicBoolean(false);
        // Act
        matrix = generator.generateResourceMatrix(50);
        MatrixUtils.forEachElement(matrix, maybeResource -> {
            if (maybeResource != null) {
                moreThanZeroResources.set(true);
            }
        });

        // Assert
        assertThat(moreThanZeroResources).isTrue();
    }

    @Test
    public void generatedResourceMatrixContainsAtLeastTwoDifferentTypeResources() {
        // Arrange
        final ResourceGenerator generator = new ResourceGenerator(0);
        final IResource[][] matrix;
        final AtomicBoolean containsWater = new AtomicBoolean(false);
        final AtomicBoolean containsTree = new AtomicBoolean(false);
        // Act
        matrix = generator.generateResourceMatrix(50);
        MatrixUtils.forEachElement(matrix, maybeResource -> {
            if (maybeResource != null) {
                if (maybeResource.getType().equals(ResourceType.TREE)) {
                    containsTree.set(true);
                }
                if (maybeResource.getType().equals(ResourceType.WATER)) {
                    containsWater.set(true);
                }
            }
        });

        // Assert
        assertThat(containsTree.get() & containsWater.get()).isTrue();
    }

}
