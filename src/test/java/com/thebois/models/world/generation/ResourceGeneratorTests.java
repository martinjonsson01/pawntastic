package com.thebois.models.world.generation;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceType;

import static org.assertj.core.api.Assertions.*;

public class ResourceGeneratorTests {

    public static Stream<Arguments> getWorldSizeAndOneSeed() {
        return Stream.of(Arguments.of(50, 40),
                         Arguments.of(17, 72),
                         Arguments.of(25, 5),
                         Arguments.of(15, 100));
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
        final ResourceGenerator generator1 = new ResourceGenerator(worldSize, seed1);
        final ResourceGenerator generator2 = new ResourceGenerator(worldSize, seed2);
        final IResource[][] matrix1;
        final IResource[][] matrix2;
        final boolean isEqual;

        // Act
        matrix1 = generator1.generateResourceMatrix();
        matrix2 = generator2.generateResourceMatrix();
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
        final ResourceGenerator generator1 = new ResourceGenerator(worldSize, seed);
        final ResourceGenerator generator2 = new ResourceGenerator(worldSize, seed);
        final IResource[][] matrix1;
        final IResource[][] matrix2;
        final boolean isEqual;

        // Act
        matrix1 = generator1.generateResourceMatrix();
        matrix2 = generator2.generateResourceMatrix();
        isEqual = Arrays.deepEquals(matrix1, matrix2);

        // Assert
        assertThat(matrix1).isDeepEqualTo(matrix2);
        assertThat(isEqual).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getWorldSizeAndOneSeed")
    public void generatedResourceMatrixContainsAllKindsOfResources(
        final int worldSize, final int seed) {
        // Arrange
        final ResourceGenerator generator = new ResourceGenerator(worldSize, seed);
        final IResource[][] matrix;
        int actualNumberOfResources = 0;
        final int expectedNumberOfResources = ResourceType.values().length;
        // Act
        matrix = generator.generateResourceMatrix();
        for (final ResourceType type : ResourceType.values()) {
            if (containsResource(matrix, type)) {
                actualNumberOfResources++;
            }
        }
        // Assert
        assertThat(actualNumberOfResources).isEqualTo(expectedNumberOfResources);
    }

    private boolean containsResource(
        final IResource[][] matrix, final ResourceType typeToSearchFor) {
        for (final IResource[] iResources : matrix) {
            for (final IResource iResource : iResources) {
                if (iResource != null) {
                    if (iResource.getType() == typeToSearchFor) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
