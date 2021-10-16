package com.thebois.models.world.structures;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class StructureTests {

    @Test
    public void structureDoesNotEqualNull() {
        // Arrange
        final AbstractStructure structure = new House(1, 1);

        // Act
        final boolean isEqual = structure.equals(null);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void structureDoesNotEqualOtherClass() {
        // Arrange
        final AbstractStructure structure = new House(1, 1);
        final Object object = new Object();

        // Act
        final boolean isEqual = structure.equals(object);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void structureEqualItSelfIsTrue() {
        // Arrange
        final AbstractStructure structure = new House(1, 1);

        // Act
        final boolean isEqual = structure.equals(structure);

        // Assert
        assertThat(isEqual).isTrue();
    }

    @Test
    public void structureEqualIsTrueIfSamePositionAndType() {
        // Arrange
        final Position position = new Position(1, 1);
        final AbstractStructure structure1 = new House(position);
        final AbstractStructure structure2 = new House(position);

        // Act
        final boolean isEqual = structure1.equals(structure2);

        // Assert
        assertThat(isEqual).isTrue();
    }

    @Test
    public void structureEqualIsFalseIfPositionIsDifferent() {
        // Arrange
        final Position position1 = new Position(1, 1);
        final Position position2 = new Position(2, 2);
        final AbstractStructure structure1 = new House(position1);
        final AbstractStructure structure2 = new House(position2);

        // Act
        final boolean isEqual = structure1.equals(structure2);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void structureHashCodeIsSameForIdenticalStructures() {
        // Arrange
        final Position position = new Position(1, 1);
        final AbstractStructure structure1 = new House(position);
        final AbstractStructure structure2 = new House(position);

        // Act
        final boolean isEqual = structure1.hashCode() == structure2.hashCode();

        // Assert
        assertThat(isEqual).isTrue();
    }

    @Test
    public void structureHashCodeIsNotSameForDifferentStructures() {
        // Arrange
        final Position position1 = new Position(1, 1);
        final Position position2 = new Position(2, 2);
        final AbstractStructure structure1 = new House(position1);
        final AbstractStructure structure2 = new House(position2);

        // Act
        final boolean isEqual = structure1.hashCode() == structure2.hashCode();

        // Assert
        assertThat(isEqual).isFalse();
    }

}
