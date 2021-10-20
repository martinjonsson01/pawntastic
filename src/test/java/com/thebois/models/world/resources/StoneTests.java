package com.thebois.models.world.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;

public class StoneTests {

    private static Stone stone;

    @BeforeEach
    public void setup() {
        stone = new Stone(1, 1);
    }

    @Test
    public void getHarvestTimeReturnsNonZeroValue() {
        // Act
        final float harvestTime = stone.getHarvestTime();

        // Assert
        assertThat(harvestTime).isNotZero();
    }

    @Test
    public void harvestReturnsRockItem() {
        // Act
        final IItem item = stone.harvest();

        // Assert
        assertThat(item.getType()).isEqualTo(ItemType.ROCK);
    }

    @Test
    public void getTypeFromRockResource() {
        // Act
        final ResourceType resourceType = stone.getType();

        // Assert
        assertThat(resourceType).isEqualTo(ResourceType.STONE);
    }

    @Test
    public void getDeepCloneShouldBeEqualToOriginal() {
        // Act
        final IResource deepClone = stone.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(stone);
    }

    @Test
    public void getCostIsFloatMax() {
        // Arrange
        final float expectedValue = Float.MAX_VALUE;

        // Act
        final float actualValue = stone.getCost();

        // Assert
        assertThat(actualValue).isEqualTo(expectedValue);
    }

}
