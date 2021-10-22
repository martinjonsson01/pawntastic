package com.thebois.models.world.resources;

import org.junit.jupiter.api.Test;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;

public class TreeTests {

    @Test
    public void harvestReturnsLog() {
        // Arrange
        final IResource tree = ResourceFactory.createResource(ResourceType.TREE, 0,0);

        // Act
        final IItem item = tree.harvest();

        // Assert
        assertThat(item.getType()).isEqualTo(ItemType.LOG);
    }

    @Test
    public void getTypeFromTreeResource() {
        // Arrange
        final IResource tree = ResourceFactory.createResource(ResourceType.TREE, 1,1);

        // Act
        final ResourceType resourceType = tree.getType();

        // Assert
        assertThat(resourceType).isEqualTo(ResourceType.TREE);
    }

    @Test
    public void getDeepCloneShouldBeEqualToOriginal() {
        // Arrange
        final IResource tree = ResourceFactory.createResource(ResourceType.TREE, 1,1);

        // Act
        final IResource deepClone = tree.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(tree);
    }

    @Test
    public void getCostIsFloatMax() {
        // Arrange
        final IResource tree = ResourceFactory.createResource(ResourceType.TREE, 1,1);
        final float expectedValue = Float.MAX_VALUE;

        // Act
        final float actualValue = tree.getCost();

        // Assert
        assertThat(actualValue).isEqualTo(expectedValue);
    }

}
