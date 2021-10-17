package com.thebois.models.world.structures;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;

public class HouseTests {

    @Test
    public void getPositionSetInConstructor() {
        // Arrange
        final Position position = new Position(123, 456);
        final IStructure structure = StructureFactory.createStructure(StructureType.HOUSE,
                                                                      position);

        // Act
        final Position structurePosition = structure.getPosition();

        // Assert
        assertThat(structurePosition).isEqualTo(position);
    }

    @Test
    public void getTypeReturnsCorrectStructureType() {
        // Arrange
        final Position position = new Position(123, 456);
        final IStructure structure = StructureFactory.createStructure(
            StructureType.HOUSE,
            position);
        // Act

        StructureType returnedType = structure.getType();

        // Assert
        assertThat(returnedType).isEqualTo(StructureType.HOUSE);
    }

    @Test
    public void housesAreEqualWhenSameInstance() {
        // Arrange
        final Position positionA = new Position(123, 456);
        final Position positionB = new Position(456, 123);

        // Act
        final IStructure structureA = StructureFactory.createStructure(
            StructureType.HOUSE,
            positionA);
        final IStructure structureB = StructureFactory.createStructure(
            StructureType.HOUSE,
            positionB);

        // Assert
        assertThat(structureA).isNotEqualTo(structureB);
    }

    private static Stream<Arguments> tryDeliverItemReturnsExpectedValueSource() {
        return Stream.of(
            Arguments.of(10, 10, ItemFactory.fromType(ItemType.ROCK), false),
            Arguments.of(10, 10,  ItemFactory.fromType(ItemType.LOG), false),
            Arguments.of(0, 0,  ItemFactory.fromType(ItemType.ROCK), true),
            Arguments.of(12, 12,  ItemFactory.fromType(ItemType.ROCK), false),
            Arguments.of(10, 9,  ItemFactory.fromType(ItemType.ROCK), true));
    }

    @ParameterizedTest
    @MethodSource("tryDeliverItemReturnsExpectedValueSource")
    public void tryDeliverItemReturnsExpectedValue(
        final int totalLogs,
        final int totalRocks,
        final IItem itemToDeliver,
        final boolean expectedResult) {
        // Arrange
        final IStructure house = StructureFactory.createStructure(
            StructureType.HOUSE,
            new Position());

        for (int i = 0; i < totalLogs; i++) {
            house.tryDeliverItem(ItemFactory.fromType(ItemType.LOG));
        }
        for (int i = 0; i < totalRocks; i++) {
            house.tryDeliverItem(ItemFactory.fromType(ItemType.ROCK));
        }
        // Act
        final boolean deliveryResult = house.tryDeliverItem(itemToDeliver);

        // Assert
        assertThat(deliveryResult).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> houseReturnsExpectedItemWhenDismantledSource() {
        return Stream.of(
            Arguments.of(10, 10, ItemType.LOG, Optional.of(ItemFactory.fromType(ItemType.LOG))),
            Arguments.of(10, 10, ItemType.LOG, Optional.of(ItemFactory.fromType(ItemType.LOG))),
            Arguments.of(0, 0, ItemType.LOG, Optional.empty()),
            Arguments.of(12, 12, ItemType.LOG, Optional.of(ItemFactory.fromType(ItemType.LOG))),
            Arguments.of(10, 9, ItemType.LOG, Optional.of(ItemFactory.fromType(ItemType.LOG))));
    }

    @ParameterizedTest
    @MethodSource("houseReturnsExpectedItemWhenDismantledSource")
    public void houseReturnsExpectedItemWhenDismantled(
        final int totalLogs,
        final int totalRocks,
        final ItemType itemTypeToRetrieve,
        final Optional<IItem> expectedItem) {
        // Arrange
        final IStructure house = StructureFactory.createStructure(
            StructureType.HOUSE,
            new Position());

        for (int i = 0; i < totalLogs; i++) {
            house.tryDeliverItem(ItemFactory.fromType(ItemType.LOG));
        }
        for (int i = 0; i < totalRocks; i++) {
            house.tryDeliverItem(ItemFactory.fromType(ItemType.ROCK));
        }

        // Act
        final Optional<IItem> retrieveItem = house.tryDismantle(itemTypeToRetrieve);

        // Assert
        assertThat(retrieveItem).isEqualTo(expectedItem);
    }

    private static Stream<Arguments> whenItemsAreDeliveredBuiltRatioIsEqualToExpectedSource() {
        return Stream.of(
            Arguments.of(10, 10, 1f),
            Arguments.of(20, 20, 1f),
            Arguments.of(0, 0, 0f),
            Arguments.of(5, 5, 0.5f),
            Arguments.of(20, 0, 0.5f),
            Arguments.of(10, 0, 0.5f));
    }

    @ParameterizedTest
    @MethodSource("whenItemsAreDeliveredBuiltRatioIsEqualToExpectedSource")
    public void whenItemsAreDeliveredBuiltRatioIsEqualToExpected(
        final int totalLogs, final int totalRocks, final float expectedRatio) {

        // Arrange
        final IStructure house = StructureFactory.createStructure(
            StructureType.HOUSE,
            new Position());

        for (int i = 0; i < totalLogs; i++) {
            house.tryDeliverItem(ItemFactory.fromType(ItemType.LOG));
        }
        for (int i = 0; i < totalRocks; i++) {
            house.tryDeliverItem(ItemFactory.fromType(ItemType.ROCK));
        }

        // Act
        final float ratio = house.getBuiltRatio();

        // Assert
        assertThat(ratio).isEqualTo(expectedRatio);
    }

    private static Stream<Arguments> whenStructureHasReceivedItemsNeededItemsAreEqualToExpectedSource() {
        return Stream.of(
            Arguments.of(8, 10, List.of(ItemType.LOG, ItemType.LOG)),
            Arguments.of(10, 10, List.of()),
            Arguments.of(20, 20, List.of()),
            Arguments.of(9, 7, List.of(ItemType.ROCK, ItemType.ROCK, ItemType.ROCK, ItemType.LOG)));
    }

    @ParameterizedTest
    @MethodSource("whenStructureHasReceivedItemsNeededItemsAreEqualToExpectedSource")
    public void whenStructureHasReceivedItemsNeededItemsAreEqualToExpected(
        final int totalLogs,
        final int totalRocks,
        final Collection<ItemType> expectedNeededItems) {
        // Arrange
        final IStructure house = StructureFactory.createStructure(
            StructureType.HOUSE,
            new Position());

        for (int i = 0; i < totalLogs; i++) {
            house.tryDeliverItem(ItemFactory.fromType(ItemType.LOG));
        }
        for (int i = 0; i < totalRocks; i++) {
            house.tryDeliverItem(ItemFactory.fromType(ItemType.ROCK));
        }

        // Act
        final Collection<ItemType> neededItems = house.getNeededItems();

        // Assert
        assertThat(neededItems.containsAll(expectedNeededItems)).isTrue();
    }

    private static Stream<Arguments> whenItemsHaveBeenDeliveredStructureReturnsCorrectIsCompleteSource() {
        return Stream.of(
            Arguments.of(10, 10, true),
            Arguments.of(20, 20, true),
            Arguments.of(0, 0, false),
            Arguments.of(5, 5, false),
            Arguments.of(20, 0, false),
            Arguments.of(10, 0, false));
    }

    @ParameterizedTest
    @MethodSource("whenItemsHaveBeenDeliveredStructureReturnsCorrectIsCompleteSource")
    public void whenItemsHaveBeenDeliveredStructureReturnsCorrectIsComplete(
        final int totalLogs, final int totalRocks, final boolean expectedResult) {

        // Arrange
        final IStructure house = StructureFactory.createStructure(
            StructureType.HOUSE,
            new Position());

        for (int i = 0; i < totalLogs; i++) {
            house.tryDeliverItem(ItemFactory.fromType(ItemType.LOG));
        }
        for (int i = 0; i < totalRocks; i++) {
            house.tryDeliverItem(ItemFactory.fromType(ItemType.ROCK));
        }

        // Act
        final boolean isComplete = house.isCompleted();

        // Assert
        assertThat(isComplete).isEqualTo(expectedResult);
    }

}
