package com.thebois.utils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
            Arguments.of(new Optional[][] {
                { Optional.of("1"), Optional.empty(), Optional.of("3") },
                { Optional.empty(), Optional.empty(), Optional.of("6") },
                { Optional.of("7"), Optional.of("8"), Optional.of("9") },
                }, List.of("1", "3", "6", "7", "8", "9")),

            Arguments.of(new Optional[][] {
                { Optional.of("1"), Optional.of("1") },
                { Optional.of("1"), Optional.of("1") },
                { Optional.of("7"), Optional.of("8") },
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
    public void matrixToCollectionTest(final Optional<String>[][] matrix,
                                       final Collection<String> expectedElements) {
        // Arrange

        // Act
        final Collection<String> stringCollection = MatrixUtils.matrixToCollection(matrix);

        // Assert
        assertThat(stringCollection).isEqualTo(expectedElements);
    }

    private static Stream<Arguments> getCorrectCoordinatesToTest() {
        final Optional<String>[][] stringOptionalMatrix = new Optional[][] {
            { Optional.of("1"), Optional.empty(), Optional.of("3") },
            { Optional.empty(), Optional.empty(), Optional.of("6") },
            { Optional.of("7"), Optional.of("8"), Optional.of("9") },
            };

        return Stream.of(Arguments.of(stringOptionalMatrix, 0, 0, "1"),
                         Arguments.of(stringOptionalMatrix, 2, 2, "9")
        );
    }

    @ParameterizedTest
    @MethodSource("getCorrectCoordinatesToTest")
    public void testMatrixSpiralSearch(final Optional<String>[][] stringMatrix, final int startX,
                                         final int startY,
                                         final String expectedString) {

        // Arrange

        // Act
        final Optional<String> foundStructure = MatrixUtils.matrixSpiralSearch(
            stringMatrix,
            startX,
            startY,
            3);

        // Assert
        if (foundStructure.isPresent()) {
            assertThat(foundStructure.get()).isEqualTo(expectedString);
        }
        else {
            assertThat(foundStructure.isEmpty()).isEqualTo(false);
        }
    }

    private static Stream<Arguments> getMatricesAndSubMatricesToTest() {
        final Optional<Integer>[][] integerMatrix = new Optional[][] {
            { Optional.of(1), Optional.of(2), Optional.of(3), Optional.of(4), Optional.of(5) },
            { Optional.of(6), Optional.of(7), Optional.of(8), Optional.of(9), Optional.of(10) },
            { Optional.of(11), Optional.of(12), Optional.of(13), Optional.of(14), Optional.of(15) },
            };

        return Stream.of(

            Arguments.of(integerMatrix, new Optional[][] {
                { Optional.of(1), Optional.of(2), Optional.of(3) },
                { Optional.of(6), Optional.of(7), Optional.of(8) },
                { Optional.of(11), Optional.of(12), Optional.of(13) },
                }, 0, 0, 2, 2),

            Arguments.of(integerMatrix, new Optional[][] {
                { Optional.of(9), Optional.of(10), Optional.empty()},
                { Optional.of(14), Optional.of(15), Optional.empty() },
                { Optional.empty(), Optional.empty(), Optional.empty() },
                }, 1, 3, 3, 5));
    }

    @ParameterizedTest
    @MethodSource("getMatricesAndSubMatricesToTest")
    public void testGetSubMatrix(final Optional<Integer>[][] matrix,
                                 final Optional<Integer>[][] expectedMatrix,
                                 final int startRow,
                                 final int startCol,
                                 final int endRow,
                                 final int endCol) {
        // Arrange

        // Act
        final Optional<Integer>[][] resultingMatrix = MatrixUtils.getSubMatrix(matrix,
                                                                               startRow,
                                                                               startCol,
                                                                               endRow,
                                                                               endCol);

        // Assert
        assertThat(resultingMatrix).isEqualTo(expectedMatrix);
    }

}
