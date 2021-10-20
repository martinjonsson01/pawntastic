package com.thebois.models.world.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;

public class WaterTests {

    private static Water water;

    @BeforeEach
    public void setup() {
        water = new Water(1, 1);
    }

    @Test
    public void getHarvestTimeReturnsNonZeroValue() {
        // Act
        final float harvestTime = water.getHarvestTime();

        // Assert
        assertThat(harvestTime).isNotZero();
    }

    @Test
    public void harvestReturnsFishItem() {
        // Act
        final IItem item = water.harvest();

        // Assert
        assertThat(item.getType()).isEqualTo(ItemType.FISH);
    }

    @Test
    public void getTypeFromWaterResource() {
        // Act
        final ResourceType resourceType = water.getType();

        // Assert
        assertThat(resourceType).isEqualTo(ResourceType.WATER);
    }

    @Test
    public void getDeepCloneShouldBeEqualToOriginal() {
        // Act
        final IResource deepClone = water.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(water);
    }

    @Test
    public void getCostIsFloatMax() {
        // Arrange
        final float expectedValue = Float.MAX_VALUE;

        // Act
        final float actualValue = water.getCost();

        // Assert
        assertThat(actualValue).isEqualTo(expectedValue);
    }

}
