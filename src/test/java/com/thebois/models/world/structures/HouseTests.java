package com.thebois.models.world.structures;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.inventory.items.Log;
import com.thebois.models.inventory.items.Rock;

import static org.assertj.core.api.Assertions.*;

public class HouseTests {

    @Test
    public void getPositionSetInConstructor() {
        // Arrange
        final Position position = new Position(123, 456);
        final House structure = new House(123, 456);

        // Act
        final Position structurePosition = structure.getPosition();

        // Assert
        assertThat(structurePosition).isEqualTo(position);
    }

    @Test
    public void getTypeEqualsHouse() {
        // Arrange
        final Position position = new Position(123, 456);

        // Act
        final House structure = new House(position);

        // Assert
        assertThat(structure.getType()).isEqualTo(StructureType.HOUSE);
    }

    @Test
    public void areHousesEqual() {
        // Arrange
        final Position position = new Position(123, 456);

        // Act
        final House structure = new House(position);

        // Assert
        assertThat(structure.getType()).isEqualTo(StructureType.HOUSE);
    }

    private static Stream<Arguments> tryDeliverItemsSource() {
        return Stream.of(
            Arguments.of(10, 10, new Rock(), false),
            Arguments.of(10, 10, new Log(), false),
            Arguments.of(0, 0, new Rock(), true),
            Arguments.of(12, 12, new Rock(), false),
            Arguments.of(10, 9, new Rock(), true));
    }

    @ParameterizedTest
    @MethodSource("tryDeliverItemsSource")
    public void tryDeliverItem(
        final int totalLogs,
        final int totalRocks,
        final IItem itemToDeliver,
        final boolean expectedResult) {
        // Arrange
        final House house = new House(new Position());

        for (int i = 0; i < totalLogs; i++) {
            house.deliverItem(new Log());
        }
        for (int i = 0; i < totalRocks; i++) {
            house.deliverItem(new Rock());
        }
        // Act
        final boolean deliveryResult = house.deliverItem(itemToDeliver);

        // Assert
        assertThat(deliveryResult).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> tryDismantleHouseSource() {
        return Stream.of(
            Arguments.of(10, 10, ItemType.LOG, new Log()),
            Arguments.of(10, 10, ItemType.LOG, new Log()),
            Arguments.of(0, 0, ItemType.LOG, null),
            Arguments.of(12, 12, ItemType.LOG, new Log()),
            Arguments.of(10, 9, ItemType.LOG, new Log()));
    }

    @ParameterizedTest
    @MethodSource("tryDismantleHouseSource")
    public void tryDismantleHouse(
        final int totalLogs,
        final int totalRocks,
        final ItemType itemTypeToRetrieve,
        final IItem expectedItem) {
        // Arrange
        final House house = new House(new Position());

        for (int i = 0; i < totalLogs; i++) {
            house.deliverItem(new Log());
        }
        for (int i = 0; i < totalRocks; i++) {
            house.deliverItem(new Rock());
        }
        // Act
        final IItem retrieveItem = house.dismantle(itemTypeToRetrieve);

        // Assert
        assertThat(retrieveItem).isEqualTo(expectedItem);
    }

    private static Stream<Arguments> isBuiltRatioEqualToExpectedSource() {
        return Stream.of(
            Arguments.of(10, 10, 1f),
            Arguments.of(20, 20, 1f),
            Arguments.of(0, 0, 0f),
            Arguments.of(5, 5, 0.5f),
            Arguments.of(20, 0, 0.5f),
            Arguments.of(10, 0, 0.5f));
    }

    @ParameterizedTest
    @MethodSource("isBuiltRatioEqualToExpectedSource")
    public void isBuiltRatioEqualToExpected(
        final int totalLogs, final int totalRocks, final float expectedRatio) {

        // Arrange
        final House house = new House(new Position());

        for (int i = 0; i < totalLogs; i++) {
            house.deliverItem(new Log());
        }
        for (int i = 0; i < totalRocks; i++) {
            house.deliverItem(new Rock());
        }

        // Act
        final float ratio = house.getBuiltRatio();

        // Assert
        assertThat(ratio).isEqualTo(expectedRatio);
    }

    private static Stream<Arguments> neededItemsEqualToExpectedSource() {
        return Stream.of(
            Arguments.of(8, 10, List.of(ItemType.LOG, ItemType.LOG)),
            Arguments.of(10, 10, List.of()),
            Arguments.of(20, 20, List.of()),
            Arguments.of(9, 7, List.of(ItemType.ROCK, ItemType.ROCK, ItemType.ROCK, ItemType.LOG)));
    }

    @ParameterizedTest
    @MethodSource("neededItemsEqualToExpectedSource")
    public void neededItemsEqualToExpected(
        final int totalLogs,
        final int totalRocks,
        final Collection<ItemType> expectedNeededItems) {
        // Arrange
        final House house = new House(new Position());

        for (int i = 0; i < totalLogs; i++) {
            house.deliverItem(new Log());
        }
        for (int i = 0; i < totalRocks; i++) {
            house.deliverItem(new Rock());
        }

        // Act
        final Collection<ItemType> neededItems = house.getNeededItems();

        // Assert
        assertThat(neededItems.containsAll(expectedNeededItems)).isTrue();
    }

}
