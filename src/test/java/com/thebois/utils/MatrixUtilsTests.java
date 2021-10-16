package com.thebois.utils;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

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
    public void forEachElementGetsAllElementsInOrder(final Integer[][] matrix,
                                                     final Collection<Integer> expectedElements) {
        // Arrange
        final Consumer<Integer> elementAction = (Consumer<Integer>) Mockito.mock(Consumer.class);
        final ArgumentCaptor<Integer> elementCaptor = ArgumentCaptor.forClass(Integer.class);

        // Act
        MatrixUtils.forEachElement(matrix, elementAction);

        // Assert
        final int wantedNumberOfInvocations = expectedElements.size();
        Mockito.verify(elementAction, times(wantedNumberOfInvocations))
               .accept(elementCaptor.capture());
        final List<Integer> actualElements = elementCaptor.getAllValues();
        assertThat(actualElements).containsExactlyElementsOf(expectedElements);
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
            { "1", null, "3" }, { null, null, "6" }, { "7", "8", "9" },
            };

        return Stream.of(Arguments.of(stringMatrix, 0, 0, "1"),
                         Arguments.of(stringMatrix, 2, 2, "9")
        );
    }

    @ParameterizedTest
    @MethodSource("getCorrectCoordinatesToTest")
    public void matrixSpiralSearchFindsCorrectElement(final String[][] stringMatrix, final int startX,
                                         final int startY,
                                         final String expectedString) {
        // Act
        final String foundString = MatrixUtils.matrixSpiralSearch(
            stringMatrix,
            startX,
            startY,
            3, 1).iterator().next();

        // Assert
        assertThat(foundString).isEqualTo(expectedString);
    }
}
