package com.thebois.models.world.resources;

import org.junit.jupiter.api.Test;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;

public class StoneTests {

    @Test
    public void harvestReturnsRockItem() {
        // Arrange
        final Stone stone = new Stone(1, 1);

        // Act
        final IItem item = stone.harvest();

        // Assert
        assertThat(item.getType()).isEqualTo(ItemType.ROCK);
    }

    @Test
    public void getTypeFromRockResource() {
        // Arrange
        final Stone stone = new Stone(1, 1);

        // Act
        final ResourceType resourceType = stone.getType();

        // Assert
        assertThat(resourceType).isEqualTo(ResourceType.STONE);
    }

    @Test
    public void getDeepCloneShouldBeEqualToOriginal() {
        // Arrange
        final Stone stone = new Stone(1, 1);

        // Act
        final IResource deepClone = stone.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(stone);
    }

    @Test
    public void getCostIsFloatMax() {
        // Arrange
        final Stone stone = new Stone(1, 1);
        final float expectedValue = Float.MAX_VALUE;

        // Act
        final float actualValue = stone.getCost();

        // Assert
        assertThat(actualValue).isEqualTo(expectedValue);
    }

}
