package com.thebois.models.world.structures;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;

public class StructureTests {

    @Test
    public void structureDoesNotEqualNull() {
        // Arrange
        final IStructure structure = StructureFactory.createStructure(StructureType.HOUSE, 0, 0);

        // Act
        @SuppressWarnings("ConstantConditions") final boolean isEqual = structure.equals(null);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void structureDoesNotEqualOtherClass() {
        // Arrange
        final IStructure structure = StructureFactory.createStructure(StructureType.HOUSE, 1, 1);
        final Object object = new Object();

        // Act
        final boolean isEqual = structure.equals(object);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void structureEqualItSelfIsTrue() {
        // Arrange
        final IStructure structure = StructureFactory.createStructure(StructureType.HOUSE, 0, 0);

        // Act
        @SuppressWarnings("EqualsWithItself") final boolean isEqual = structure.equals(structure);

        // Assert
        //noinspection ConstantConditions
        assertThat(isEqual).isTrue();
    }

    @Test
    public void structureEqualIsFalseIfSamePositionAndType() {
        // Arrange
        final Position position = new Position(1, 1);
        final IStructure structure1 =
            StructureFactory.createStructure(StructureType.HOUSE, position);
        final IStructure structure2 =
            StructureFactory.createStructure(StructureType.HOUSE, position);

        // Act
        final boolean isEqual = structure1.equals(structure2);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void structureEqualIsFalseIfPositionIsDifferent() {
        // Arrange
        final Position position1 = new Position(1, 1);
        final Position position2 = new Position(2, 2);
        final IStructure structure1 =
            StructureFactory.createStructure(StructureType.HOUSE, position1);
        final IStructure structure2 =
            StructureFactory.createStructure(StructureType.HOUSE, position2);

        // Act
        final boolean isEqual = structure1.equals(structure2);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void structureHashCodeIsDifferentForIdenticalStructures() {
        // Arrange
        final Position position = new Position(1, 1);
        final IStructure structure1 =
            StructureFactory.createStructure(StructureType.HOUSE, position);
        final IStructure structure2 =
            StructureFactory.createStructure(StructureType.HOUSE, position);

        // Act
        final boolean isEqual = structure1.hashCode() == structure2.hashCode();

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void structureHashCodeIsNotSameForDifferentStructures() {
        // Arrange
        final Position position1 = new Position(1, 1);
        final Position position2 = new Position(2, 2);
        final IStructure structure1 =
            StructureFactory.createStructure(StructureType.HOUSE, position1);
        final IStructure structure2 =
            StructureFactory.createStructure(StructureType.HOUSE, position2);

        // Act
        final boolean isEqual = structure1.hashCode() == structure2.hashCode();

        // Assert
        assertThat(isEqual).isFalse();
    }

}
