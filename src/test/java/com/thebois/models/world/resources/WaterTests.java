package com.thebois.models.world.resources;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class WaterTests {

    @Test
    public void getTypeFromWaterResource() {
        // Arrange
        final Water water = new Water(1, 1);

        // Act
        final ResourceType resourceType = water.getType();

        // Assert
        assertThat(resourceType).isEqualTo(ResourceType.WATER);
    }

    @Test
    public void getDeepCloneShouldBeEqualToOriginal() {
        // Arrange
        final Water water = new Water(1, 1);

        // Act
        final IResource deepClone = water.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(water);
    }

    @Test
    public void getCostIsFloatMax() {
        // Arrange
        final Water water = new Water(1, 1);
        final float expectedValue = Float.MAX_VALUE;

        // Act
        final float actualValue = water.getCost();

        // Assert
        assertThat(actualValue).isEqualTo(expectedValue);
    }

}
