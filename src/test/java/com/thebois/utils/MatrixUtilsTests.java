package com.thebois.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MatrixUtilsTests {

    public static Stream<Arguments> getMatricesAndTheirElements() {
        return Stream.of(
            Arguments.of(new Integer[][] { { 1 }, { 2 } }, List.of(1, 2)),
            Arguments.of(new Integer[][] { { 1, 2 }, { 3, 4 } }, List.of(1, 2, 3, 4)),
            Arguments.of(
                new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } },
                List.of(1, 2, 3, 4, 5, 6, 7, 8, 9)),
            Arguments.of(new Integer[][] { { -1 } }, List.of(-1)));
    }

    public static Stream<Arguments> getMatricesToTest() {
        return Stream.of(
            Arguments.of(new String[][] {
                { "1", null, "3" },
                { null, null, "6" },
                { "7", "8", "9" },
                }, List.of("1", "3", "6", "7", "8", "9")),

            Arguments.of(new String[][] {
                { "1", "1" }, { "1", "1" }, { "7", "8" },
                }, List.of("1", "1", "1", "1", "7", "8")));
    }

    @SuppressWarnings("unchecked")
    @ParameterizedTest
    @MethodSource("getMatricesAndTheirElements")
    public void forEachElementGetsAllElementsInOrder(
        final Integer[][] matrix, final Collection<Integer> expectedElements) {
        // Arrange
        final Consumer<Integer> elementAction = (Consumer<Integer>) Mockito.mock(Consumer.class);
        final ArgumentCaptor<Integer> elementCaptor = ArgumentCaptor.forClass(Integer.class);

        // Act
        MatrixUtils.forEachElement(matrix, elementAction);

        // Assert
        final int wantedNumberOfInvocations = expectedElements.size();
        Mockito
            .verify(elementAction, times(wantedNumberOfInvocations))
            .accept(elementCaptor.capture());
        final List<Integer> actualElements = elementCaptor.getAllValues();
        assertThat(actualElements).containsExactlyElementsOf(expectedElements);
    }

    @Test
    public void populateElementsPopulatesMatrixWithX() {

        // Arrange
        final int matrixSize = 10;
        final Integer[][] intMatrix = new Integer[matrixSize][matrixSize];
        final Integer[][] expectedMatrix = new Integer[matrixSize][matrixSize];
        final Integer[] row = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        for (int y = 0; y < matrixSize; y++) {
            expectedMatrix[y] = row;
        }

        // Act
        MatrixUtils.populateElements(intMatrix, (x, y) -> x);

        // Assert
        assertThat(intMatrix).isDeepEqualTo(expectedMatrix);
    }

    @Test
    public void populateElementsPopulatesMatrixWithY() {

        // Arrange
        final int matrixSize = 10;
        final Integer[][] intMatrix = new Integer[matrixSize][matrixSize];
        final Integer[][] expectedMatrix = new Integer[matrixSize][matrixSize];
        for (int y = 0; y < matrixSize; y++) {
            for (int x = 0; x < matrixSize; x++) {
                expectedMatrix[y][x] = y;
            }
        }

        // Act
        MatrixUtils.populateElements(intMatrix, (x, y) -> y);

        // Assert
        assertThat(intMatrix).isDeepEqualTo(expectedMatrix);
    }

    @Test
    public void populateElementsPopulatesMatrixWithBooleanTrue() {

        // Arrange
        final int matrixSize = 10;
        final Boolean[][] intMatrix = new Boolean[matrixSize][matrixSize];
        final Boolean[][] expectedMatrix = new Boolean[matrixSize][matrixSize];
        for (int y = 0; y < matrixSize; y++) {
            for (int x = 0; x < matrixSize; x++) {
                expectedMatrix[y][x] = true;
            }
        }

        // Act
        MatrixUtils.populateElements(intMatrix, (x, y) -> true);

        // Assert
        assertThat(intMatrix).isDeepEqualTo(expectedMatrix);
    }

    @ParameterizedTest
    @MethodSource("getMatricesToTest")
    public void matrixToCollectionReturnsExpectedElements(final String[][] matrix,
                                                          final Collection<String> expectedElements) {
        // Act
        final Collection<String> stringCollection = MatrixUtils.toCollection(matrix);

        // Assert
        assertThat(stringCollection).isEqualTo(expectedElements);
    }

    private static Stream<Arguments> getCorrectCoordinatesToTest() {
        final String[][] stringMatrix = new String[][] {
            { "1", null, "3" },
            { null, null, "6" },
            { "7", "8", "9" },
            };

        return Stream.of(Arguments.of(stringMatrix, 0, 1, "1"),
                         Arguments.of(stringMatrix, 2, 1, "8")
        );
    }

    @ParameterizedTest
    @MethodSource("getCorrectCoordinatesToTest")
    public void matrixSpiralSearchFindsCorrectElement(final String[][] stringMatrix, final int startRow,
                                                      final int startCol,
                                                      final String expectedString) {
        // Act
        final String foundString = MatrixUtils.matrixSpiralSearch(
            stringMatrix,
            startRow,
            startCol,
            5, 1).iterator().next();

        // Assert
        assertThat(foundString).isEqualTo(expectedString);
    }

}
