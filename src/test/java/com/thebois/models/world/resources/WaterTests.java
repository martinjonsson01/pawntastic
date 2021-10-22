package com.thebois.models.world.resources;

import org.junit.jupiter.api.Test;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;

public class WaterTests {

    @Test
    public void harvestReturnsFishItem() {
        // Arrange
        final IResource water = ResourceFactory.createResource(ResourceType.WATER,1,1);

        // Act
        final IItem item = water.harvest();

        // Assert
        assertThat(item.getType()).isEqualTo(ItemType.FISH);
    }

    @Test
    public void getTypeFromWaterResource() {
        // Arrange
        final IResource water = ResourceFactory.createResource(ResourceType.WATER,1,1);

        // Act
        final ResourceType resourceType = water.getType();

        // Assert
        assertThat(resourceType).isEqualTo(ResourceType.WATER);
    }

    @Test
    public void getDeepCloneShouldBeEqualToOriginal() {
        // Arrange
        final IResource water = ResourceFactory.createResource(ResourceType.WATER,1,1);

        // Act
        final IResource deepClone = water.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(water);
    }

    @Test
    public void getCostIsFloatMax() {
        // Arrange
        final IResource water = ResourceFactory.createResource(ResourceType.WATER,1,1);
        final float expectedValue = Float.MAX_VALUE;

        // Act
        final float actualValue = water.getCost();

        // Assert
        assertThat(actualValue).isEqualTo(expectedValue);
    }

}
