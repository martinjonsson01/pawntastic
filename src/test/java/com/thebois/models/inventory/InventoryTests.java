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

    /**
     * Gets an item to add and whether it would fit if added to an inventory with 20 kg capacity
     * left.
     *
     * @return Items and if they should fit.
     */
    public static Stream<Arguments> getItemToAddAndWhetherItShouldFit() {
        return Stream.of(Arguments.of(mockItem(20f), true),
                         Arguments.of(mockItem(0.1f), true),
                         Arguments.of(mockItem(19.9f), true),
                         Arguments.of(mockItem(0f), true),
                         Arguments.of(mockItem(20.1f), false),
                         Arguments.of(mockItem(1000f), false));
    }

    private static IItem mockItem(final float weight) {
        return mockItem(weight, ItemType.LOG);
    }

    private static IItem mockItem(final float weight, final ItemType type) {
        final IItem item = mock(IItem.class);
        when(item.getWeight()).thenReturn(weight);
        when(item.getType()).thenReturn(type);
        return item;
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
        final IItem heavyItem = mockItem(0f);
        final IItem lightItem = mockItem(0f);
        inventory.tryAdd(heavyItem);
        inventory.tryAdd(lightItem);
        // Change the weight of mocked items after they have been added, so that they can be added
        // even if the inventory is full.
        when(heavyItem.getWeight()).thenReturn(20f);
        when(lightItem.getWeight()).thenReturn(0.1f);

        // Act
        final boolean isFull = inventory.isFull();

        // Assert
        assertThat(isFull).isEqualTo(expectedFull);
    }

    @ParameterizedTest
    @MethodSource("getItemToAddAndWhetherItShouldFit")
    public void addItemToInfiniteInventorySucceedsRegardlessOfIfItShould(
        final IItem item, final boolean shouldFit) {
        // Arrange
        final IInventory infiniteInventory = new Inventory();

        // Act
        final boolean succeeded = infiniteInventory.tryAdd(item);

        // Assert
        assertThat(succeeded).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getItemToAddAndWhetherItShouldFit")
    public void addItemReturnsIfItemFits(final IItem item, final boolean shouldFit) {
        // Arrange
        final IInventory inventory = new Inventory(100f);
        final IItem heavyItem = mockItem(20f);
        final IItem heavierItem = mockItem(60f);
        inventory.tryAdd(heavyItem);
        inventory.tryAdd(heavierItem);

        // Act
        final boolean fits = inventory.tryAdd(item);

        // Assert
        assertThat(fits).isEqualTo(shouldFit);
    }

    @ParameterizedTest
    @MethodSource("getItemToAddAndWhetherItShouldFit")
    public void addItemOnlyAddsItemIfItFits(final IItem item, final boolean shouldFit) {
        // Arrange
        final IInventory inventory = new Inventory(100f);
        final IItem heavyItem = mockItem(20f);
        final IItem heavierItem = mockItem(60f);
        inventory.tryAdd(heavyItem);
        inventory.tryAdd(heavierItem);

        final ItemType addedType = ItemType.FISH;
        when(item.getType()).thenReturn(addedType);

        // Act
        inventory.tryAdd(item);
        final boolean hasBeenAdded = inventory.hasItem(addedType);

        // Assert
        assertThat(hasBeenAdded).isEqualTo(shouldFit);
    }

    @Test
    public void isFullReturnsFalseWhenHasInfiniteCapacity() {
        // Arrange
        final IInventory infiniteInventory = new Inventory();
        final IItem heavyItem = mockItem(2000000f);
        final IItem heavierItem = mockItem(60000000f);
        infiniteInventory.tryAdd(heavyItem);
        infiniteInventory.tryAdd(heavierItem);

        // Act
        final boolean isFull = infiniteInventory.isFull();

        // Assert
        assertThat(isFull).isFalse();
    }

    @Test
    public void takeItemThrowsExceptionWhenOnlyOtherItemsInInventory() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));
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
        inventory.tryAdd(ItemFactory.fromType(ItemType.ROCK));
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));
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
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));

        final int count = inventory.numberOf(ItemType.LOG);

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void countIsCorrectValueWhenInventoryDoesNotHaveSpecifiedType() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));

        final int count = inventory.numberOf(ItemType.ROCK);

        // Assert
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void takeMultipleItemsFromInventory() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));

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
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));

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
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));
        inventory.tryAdd(ItemFactory.fromType(ItemType.LOG));

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
