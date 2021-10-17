package com.thebois.models.inventory;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class InventoryTests {

    public static Stream<Arguments> getItemTypes() {
        return Stream.of(Arguments.of(ItemType.LOG), Arguments.of(ItemType.ROCK));
    }

    /**
     * Gets max capacities and whether an inventory containing 20.1 kgs of items would be full with
     * that capacity.
     *
     * @return Max capacities associated with whether they are full.
     */
    public static Stream<Arguments> getCapacityAndExpectedFullState() {
        return Stream.of(Arguments.of(100f, false),
                         Arguments.of(25f, false),
                         Arguments.of(10000f, false),
                         Arguments.of(20.1f, true),
                         Arguments.of(20f, true),
                         Arguments.of(20.0001f, true),
                         Arguments.of(-1000f, true),
                         Arguments.of(0f, true));
    }

    @ParameterizedTest
    @MethodSource("getItemTypes")
    public void emptyInventoryIsEmpty(final ItemType itemType) {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        final Exception exception = assertThrows(IllegalArgumentException.class,
                                                 () -> inventory.take(itemType));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Specified ItemType not in inventory");
    }

    @ParameterizedTest
    @MethodSource("getCapacityAndExpectedFullState")
    public void isFullReturnsTrueWhenAtCapacityAndFalseWhenNot(
        final float capacity, final boolean expectedFull) {
        // Arrange
        final IInventory inventory = new Inventory(capacity);
        final IItem heavyItem = mock(IItem.class);
        when(heavyItem.getWeight()).thenReturn(20f);
        final IItem lightItem = mock(IItem.class);
        when(lightItem.getWeight()).thenReturn(0.1f);
        inventory.add(heavyItem);
        inventory.add(lightItem);

        // Act
        final boolean isFull = inventory.isFull();

        // Assert
        assertThat(isFull).isEqualTo(expectedFull);
    }

    @Test
    public void takeItemThrowsExceptionWhenOnlyOtherItemsInInventory() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.add(ItemFactory.fromType(ItemType.LOG));
        inventory.add(ItemFactory.fromType(ItemType.LOG));
        final Exception exception = assertThrows(IllegalArgumentException.class,
                                                 () -> inventory.take(ItemType.ROCK));

        // Assert
        assertThat(exception.getMessage()).isEqualTo("Specified ItemType not in inventory");
    }

    @Test
    public void canAddAndTakeItemToInventory() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.add(ItemFactory.fromType(ItemType.ROCK));
        inventory.add(ItemFactory.fromType(ItemType.LOG));
        final IItem item = inventory.take(ItemType.LOG);

        // Assert
        assertThat(item.getType()).isEqualTo(ItemType.LOG);
    }

    @Test
    public void countEmptyInventory() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        final int count = inventory.numberOf(ItemType.ROCK);

        // Assert
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void countIsCorrectValueWhenInventoryGotSpecifiedItemType() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.add(ItemFactory.fromType(ItemType.LOG));
        inventory.add(ItemFactory.fromType(ItemType.LOG));

        final int count = inventory.numberOf(ItemType.LOG);

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void countIsCorrectValueWhenInventoryDoesNotHaveSpecifiedType() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.add(ItemFactory.fromType(ItemType.LOG));
        inventory.add(ItemFactory.fromType(ItemType.LOG));

        final int count = inventory.numberOf(ItemType.ROCK);

        // Assert
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void takeMultipleItemsFromInventory() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.add(ItemFactory.fromType(ItemType.LOG));
        inventory.add(ItemFactory.fromType(ItemType.LOG));

        final ArrayList<IItem> result = inventory.takeAmount(ItemType.LOG, 2);

        // Assert
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getType()).isEqualTo(ItemType.LOG);
        assertThat(result.get(1).getType()).isEqualTo(ItemType.LOG);
    }

    @Test
    public void canNotTakeMultipleItemsFromInventory() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.add(ItemFactory.fromType(ItemType.LOG));
        inventory.add(ItemFactory.fromType(ItemType.LOG));

        final Exception exception = assertThrows(IllegalArgumentException.class,
                                                 () -> inventory.takeAmount(ItemType.ROCK, 2));

        // Assert
        assertThat(exception.getMessage()).isEqualTo(
            "Not enough of the specified ItemType in the inventory");
    }

    @Test
    public void inventoryHasMultipleOfItem() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.add(ItemFactory.fromType(ItemType.LOG));
        inventory.add(ItemFactory.fromType(ItemType.LOG));

        final boolean result = inventory.hasItem(ItemType.LOG, 2);

        // Assert
        assertThat(result).isTrue();
    }

    @Test
    public void inventoryDoesNotHaveMultipleOfItem() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        final boolean result = inventory.hasItem(ItemType.LOG, 2);

        // Assert
        assertThat(result).isFalse();
    }

    @Test
    public void addMultipleItemsToInventory() {
        // Arrange
        final Inventory inventory = new Inventory();
        final ArrayList<IItem> items = new ArrayList<>();
        items.add(ItemFactory.fromType(ItemType.LOG));
        items.add(ItemFactory.fromType(ItemType.LOG));

        // Act
        inventory.addMultiple(items);

        // Assert
        assertThat(inventory.hasItem(ItemType.LOG, 2)).isTrue();
    }

}
