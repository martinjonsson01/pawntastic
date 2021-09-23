package com.thebois.models.inventory;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.inventory.items.Log;

import static org.assertj.core.api.Assertions.*;

public class InventoryTests {

    public static Stream<Arguments> getItemTypes() {
        return Stream.of(Arguments.of(ItemType.LOG), Arguments.of(ItemType.ROCK));
    }

    @ParameterizedTest
    @MethodSource("getItemTypes")
    public void emptyInventoryIsEmpty(final ItemType itemType) {
        // Arrange
        final AbstractInventory inventory = new ColonyInventory();

        // Act
        final IItem result = inventory.takeItem(itemType);

        // Assert
        assertThat(result).isEqualTo(null);
    }

    @Test
    public void canAddAndTakeItemToInventory() {
        // Arrange
        final AbstractInventory inventory = new ColonyInventory();

        // Act
        inventory.addItem(new Log());
        final IItem item = inventory.takeItem(ItemType.LOG);

        // Assert
        assertThat(item.getType()).isEqualTo(ItemType.LOG);
    }

}
