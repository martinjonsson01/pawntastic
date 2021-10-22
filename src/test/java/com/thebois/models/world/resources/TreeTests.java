package com.thebois.models.world.resources;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;

public class TreeTests {

    private static IResource tree;

    @BeforeEach
    public void setup() {
        tree = ResourceFactory.createResource(ResourceType.TREE, 0,0);
    }

    @Test
    public void getHarvestTimeReturnsNonZeroValue() {
        // Act
        final float harvestTime = tree.getHarvestTime();

        // Assert
        assertThat(harvestTime).isNotZero();
    }

    @Test
    public void harvestReturnsLog() {
        // Act
        final IItem item = tree.harvest();

        // Assert
        assertThat(item.getType()).isEqualTo(ItemType.LOG);
    }

    @Test
    public void getTypeFromTreeResource() {
        // Act
        final ResourceType resourceType = tree.getType();

        // Assert
        assertThat(resourceType).isEqualTo(ResourceType.TREE);
    }

    @Test
    public void getDeepCloneShouldBeEqualToOriginal() {
        // Act
        final IResource deepClone = tree.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(tree);
    }

    @Test
    public void getCostIsFloatMax() {
        // Arrange
        final float expectedValue = Float.MAX_VALUE;

        // Act
        final float actualValue = tree.getCost();

        // Assert
        assertThat(actualValue).isEqualTo(expectedValue);
    }

}
