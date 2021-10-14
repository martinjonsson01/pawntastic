package com.thebois.models.world.resources;

import org.junit.jupiter.api.Test;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;

public class TreeTests {

    @Test
    public void harvestReturnsLog() {
        // Arrange
        final Tree tree = new Tree(0, 0);

        // Act
        final IItem item = tree.harvest();

        // Assert
        assertThat(item.getType()).isEqualTo(ItemType.LOG);
    }

    @Test
    public void getTypeFromTreeResource() {
        // Arrange
        final Tree tree = new Tree(1, 1);
        final ResourceType resourceType;

        // Act
        resourceType = tree.getType();

        // Assert
        assertThat(resourceType).isEqualTo(ResourceType.TREE);
    }

    @Test
    public void getDeepCloneShouldBeEqualToOriginal() {
        // Arrange
        final Tree tree = new Tree(1, 1);
        final IResource deepClone;

        // Act
        deepClone = tree.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(tree);
    }

    @Test
    public void getCostIsFloatMax() {
        // Arrange
        final Tree tree = new Tree(1, 1);
        final float expectedValue = Float.MAX_VALUE;
        final float actualValue;

        // Act
        actualValue = tree.getCost();

        // Assert
        assertThat(actualValue).isEqualTo(expectedValue);
    }

}
