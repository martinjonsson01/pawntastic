package com.thebois.models.world.resources;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class TreeTests {

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
