package com.thebois.models.world.structures;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemType;

import static org.assertj.core.api.Assertions.*;

public class StructureTests {

    @Test
    public void structureDoesNotEqualNull() {
        // Arrange
        final IStructure structure = StructureFactory.createStructure(StructureType.HOUSE, 0, 0);

        // Act
        final boolean isEqual = structure.equals(null);

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
        final boolean isEqual = structure.equals(structure);

        // Assert
        assertThat(isEqual).isTrue();
    }

    @Test
    public void structureEqualIsFalseIfSamePositionAndType() {
        // Arrange
        final Position position = new Position(1, 1);
        final IStructure structure1 = StructureFactory.createStructure(StructureType.HOUSE,
                                                                       position);
        final IStructure structure2 = StructureFactory.createStructure(StructureType.HOUSE,
                                                                       position);

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
        final IStructure structure1 = StructureFactory.createStructure(StructureType.HOUSE,
                                                                       position1);
        final IStructure structure2 = StructureFactory.createStructure(StructureType.HOUSE,
                                                                       position2);

        // Act
        final boolean isEqual = structure1.equals(structure2);

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void structureHashCodeIsDifferentForIdenticalStructures() {
        // Arrange
        final Position position = new Position(1, 1);
        final IStructure structure1 = StructureFactory.createStructure(StructureType.HOUSE,
                                                                       position);
        final IStructure structure2 = StructureFactory.createStructure(StructureType.HOUSE,
                                                                       position);

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
        final IStructure structure1 = StructureFactory.createStructure(StructureType.HOUSE,
                                                                       position1);
        final IStructure structure2 = StructureFactory.createStructure(StructureType.HOUSE,
                                                                       position2);

        // Act
        final boolean isEqual = structure1.hashCode() == structure2.hashCode();

        // Assert
        assertThat(isEqual).isFalse();
    }

    @Test
    public void deepCloneReturnsExpectedClone() {
        // Arrange
        final Position expectedPosition = new Position(20f, 15f);

        final IStructure house = StructureFactory.createStructure(StructureType.HOUSE,
                                                                  expectedPosition);

        // Act
        final IStructure houseDeepClone = house.deepClone();

        // Assert
        assertThat(houseDeepClone.getPosition()).isEqualTo(houseDeepClone.getPosition());
        assertThat(houseDeepClone.getType()).isEqualTo(houseDeepClone.getType());
    }

    @Test
    public void hasItemReturnsFalse() {
        // Arrange
        final ItemType itemType = ItemType.FISH;
        final IStructure structure = StructureFactory.createStructure(StructureType.HOUSE, 0, 0);

        // Act
        final boolean hasItem = structure.hasItem(itemType);

        // Assert
        assertThat(hasItem).isFalse();
    }

    @Test
    public void takeItemReturnsNull() {
        // Arrange
        final ItemType itemType = ItemType.FISH;
        final IStructure structure = StructureFactory.createStructure(StructureType.HOUSE, 0, 0);
        final IItem expectedValue = null;

        // Act
        final IItem actualValue = structure.takeItem(itemType);

        // Assert
        assertThat(actualValue).isEqualTo(expectedValue);
    }

    @Test
    public void canItemFitReturnsFalse() {
        // Arrange
        final ItemType itemType = ItemType.FISH;
        final IStructure structure = StructureFactory.createStructure(StructureType.HOUSE, 0, 0);

        // Act
        final boolean canFitItem = structure.canFitItem(itemType);

        // Assert
        assertThat(canFitItem).isFalse();
    }

}
