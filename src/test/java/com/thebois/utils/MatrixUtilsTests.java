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
        return Stream.of(Arguments.of(new Integer[][] { { 1 }, { 2 } }, List.of(1, 2)),
                         Arguments.of(new Integer[][] { { 1, 2 }, { 3, 4 } }, List.of(1, 2, 3, 4)),
                         Arguments.of(new Integer[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } },
                                      List.of(1, 2, 3, 4, 5, 6, 7, 8, 9)),
                         Arguments.of(new Integer[][] { { -1 } }, List.of(-1)));
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

}
