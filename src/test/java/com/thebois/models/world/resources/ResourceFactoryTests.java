package com.thebois.models.world.resources;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ResourceFactoryTests {

    @Test
    public void createResourceWithTreeEnumReturnsTreeResource() {
        // Arrange
        final int x = 0;
        final int y = 0;
        final IResource expectedTree = new Tree(x, y);
        final IResource actualTree;

        // Act
        actualTree = ResourceFactory.createResource(ResourceType.TREE, x, y);

        // Assert
        assertThat(actualTree).isEqualTo(expectedTree);
    }

    @Test
    public void createResourceWithWaterEnumReturnsWaterResource() {
        // Arrange
        final int x = 0;
        final int y = 0;
        final IResource expectedWater = new Water(x, y);
        final IResource actualWater;

        // Act
        actualWater = ResourceFactory.createResource(ResourceType.WATER, x, y);

        // Assert
        assertThat(actualWater).isEqualTo(expectedWater);
    }

    @Test
    public void createResourceWithRockEnumReturnsRockResource() {
        // Arrange
        final int x = 0;
        final int y = 0;
        final IResource expectedRock = new Rock(x, y);
        final IResource actualRock;

        // Act
        actualRock = ResourceFactory.createResource(ResourceType.ROCK, x, y);

        // Assert
        assertThat(actualRock).isEqualTo(expectedRock);
    }

}
