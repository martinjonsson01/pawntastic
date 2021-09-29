package com.thebois.models.inventory;

import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.inventory.items.Log;
import com.thebois.models.inventory.items.Rock;

import static org.assertj.core.api.Assertions.*;

public class InventoryTests {

    public static Stream<Arguments> getItemTypes() {
        return Stream.of(Arguments.of(ItemType.LOG), Arguments.of(ItemType.ROCK));
    }

    @ParameterizedTest
    @MethodSource("getItemTypes")
    public void emptyInventoryIsEmpty(final ItemType itemType) {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        final Optional<IItem> result = inventory.take(itemType);

        // Assert
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void takeItemReturnsEmptyWhenOnlyOtherItemsInInventory() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.add(new Log());
        inventory.add(new Log());
        final Optional<IItem> result = inventory.take(ItemType.ROCK);

        // Assert
        assertThat(result.isEmpty()).isTrue();
    }

    @Test
    public void canAddAndTakeItemToInventory() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.add(new Rock());
        inventory.add(new Log());
        final Optional<IItem> item = inventory.take(ItemType.LOG);

        // Assert
        assertThat(item.isPresent()).isTrue();
        assertThat(item.get().getType()).isEqualTo(ItemType.LOG);
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
        inventory.add(new Log());
        inventory.add(new Log());

        final int count = inventory.numberOf(ItemType.LOG);

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void countIsCorrectValueWhenInventoryDoesNotHaveSpecifiedType() {
        // Arrange
        final Inventory inventory = new Inventory();

        // Act
        inventory.add(new Log());
        inventory.add(new Log());

        final int count = inventory.numberOf(ItemType.ROCK);

        // Assert
        assertThat(count).isEqualTo(0);
    }

}
