package com.thebois.models.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Stack;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.models.IFinder;
import com.thebois.models.beings.Colony;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.inventory.items.Log;
import com.thebois.models.inventory.items.Rock;

import static org.assertj.core.api.Assertions.*;

public class ColonyInventoryTests {

    private static Stream<Arguments> calculatedDifferenceEqualToExpectedSource() {
        return Stream.of(
            Arguments.of(
                List.of(new Rock(), new Rock(), new Rock(), new Log(), new Log(), new Log()),
                List.of(ItemType.ROCK),
                List.of()),
            Arguments.of(
                List.of(new Rock(), new Rock()),
                List.of(ItemType.ROCK, ItemType.ROCK, ItemType.ROCK),
                List.of(ItemType.ROCK)),
            Arguments.of(
                List.of(),
                List.of(ItemType.ROCK, ItemType.ROCK),
                List.of(ItemType.ROCK, ItemType.ROCK))
        );
    }

    @ParameterizedTest
    @MethodSource("calculatedDifferenceEqualToExpectedSource")
    public void calculatedDifferenceEqualToExpected(
        final List<IItem> listA,
        final List<ItemType> listB,
        final List<ItemType> expectedDifference) {
        // Arrange
        final IInventory colony = new Colony(new ArrayList<>(),
                                              Mockito.mock(IPathFinder.class),
                                              Mockito.mock(IFinder.class));

        colony.addMultiple(listA);

        // Act
        final Collection<ItemType> difference = colony.calculateDifference(listB);

        // Assert
        assertThat(difference.containsAll(expectedDifference)).isTrue();
    }

    private static Stream<Arguments> emptyReturnsCorrectValueSource() {
        return Stream.of(
            Arguments.of(new ArrayList<>(List.of(new Rock(), new Rock())), false),
            Arguments.of(new ArrayList<>(List.of()), true));
    }

    @ParameterizedTest
    @MethodSource("emptyReturnsCorrectValueSource")
    public void emptyReturnsCorrectValue(
        final ArrayList<IItem> items, final boolean expectedResult) {
        // Arrange
        final IInventory colony = new Colony(new ArrayList<>(),
                                             Mockito.mock(IPathFinder.class),
                                             Mockito.mock(IFinder.class));
        colony.addMultiple(items);

        // Assert
        assertThat(colony.isEmpty()).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> sizeReturnsCorrectValueSource() {
        return Stream.of(Arguments.of(
                             new Colony(new ArrayList<>(),
                                        Mockito.mock(IPathFinder.class),
                                        Mockito.mock(IFinder.class)),
                             new ArrayList<>(List.of(new Rock(), new Rock())),
                             2),
                         Arguments.of(
                             new Colony(new ArrayList<>(),
                                        Mockito.mock(IPathFinder.class),
                                        Mockito.mock(IFinder.class)),
                             new ArrayList<>(List.of()),
                             0));
    }

    @ParameterizedTest
    @MethodSource("sizeReturnsCorrectValueSource")
    public void sizeReturnsCorrectValue(final IInventory inventory,
                                        final ArrayList<IItem> items,
                                        final int expectedSize) {
        // Arrange
        inventory.addMultiple(items);

        // Assert
        assertThat(inventory.size()).isEqualTo(expectedSize);
    }

}
