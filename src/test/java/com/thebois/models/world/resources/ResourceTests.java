package com.thebois.models.world.resources;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class ResourceTests {

    @Test
    public void getPositionForResource() {
        // Arrange
        final Position position = new Position(1, 1);
        final IResource resource = new Water(position);

        // Act
        final Position actualPosition = resource.getPosition();

        // Assert
        assertThat(actualPosition).isEqualTo(position);
    }

    @Test
    public void constructorTestWithFloatsInsteadOfPosition() {
        // Arrange
        final Position position = new Position(1, 1);
        final IResource resource = new Water(1, 1);

        // Act
        final Position actualPosition = resource.getPosition();

        // Assert
        assertThat(actualPosition).isEqualTo(position);
    }

    @Test
    public void resourceIsEqualToItself() {
        // Arrange
        final IResource resource = new Water(1, 1);
        final boolean isEqual;

        // Act
        isEqual = resource.equals(resource);

        // Assert
        assertThat(isEqual).isTrue();
    }

    @Test
    public void resourceIsNotEqualToNull() {
        // Arrange
        final IResource resource = new Water(1, 1);
        final boolean isEqual;

        // Act
        isEqual = resource.equals(null);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void resourceIsNotEqualToOtherObject() {
        // Arrange
        final IResource resource = new Water(1, 1);
        final Object object = new Object();
        final boolean isEqual;

        // Act
        isEqual = resource.equals(object);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void resourceIsNotEqualToResourceWithOtherPosition() {
        // Arrange
        final Position position1 = new Position(1, 1);
        final Position position2 = new Position(2, 2);
        final IResource resource1 = new Water(position1);
        final IResource resource2 = new Water(position2);
        final boolean isEqual;

        // Act
        isEqual = resource1.equals(resource2);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void resourceIsEqualToIdenticalResource() {
        // Arrange
        final Position position = new Position(1, 1);
        final IResource resource1 = new Water(position);
        final IResource resource2 = new Water(position);
        final boolean isEqual;

        // Act
        isEqual = resource1.equals(resource2);

        // Assert
        assertThat(isEqual).isTrue();
    }

    @Test
    public void hashCodeTest() {
        // Arrange
        final Position position = new Position(1, 1);
        final IResource resource1 = new Water(position);
        final IResource resource2 = new Water(position);
        final boolean isEqual;

        // Act
        isEqual = resource1.hashCode() == resource2.hashCode();

        // Assert
        assertThat(isEqual).isTrue();
    }

}
