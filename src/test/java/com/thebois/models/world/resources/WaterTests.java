package com.thebois.models.world.resources;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class WaterTests {

    @Test
    public void getTypeFromWaterResource() {
        // Arrange
        final Water water = new Water(1, 1);
        final ResourceType resourceType;

        // Act
        resourceType = water.getType();

        // Assert
        assertThat(resourceType).isEqualTo(ResourceType.WATER);
    }

    @Test
    public void getDeepCloneShouldBeEqualToOriginal() {
        // Arrange
        final Water water = new Water(1, 1);
        final IResource deepClone;

        // Act
        deepClone = water.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(water);
    }

}
