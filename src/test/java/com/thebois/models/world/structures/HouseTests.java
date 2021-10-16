package com.thebois.models.world.structures;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class HouseTests {

    @Test
    public void getPositionSetInConstructor() {
        // Arrange
        final Position position = new Position(123, 456);
        final House structure = new House(123, 456);

        // Act
        final Position structurePosition = structure.getPosition();

        // Assert
        assertThat(structurePosition).isEqualTo(position);
    }

    @Test
    public void getTypeEqualsHouse() {
        // Arrange
        final Position position = new Position(123, 456);

        // Act
        final House structure = new House(position);

        // Assert
        assertThat(structure.getType()).isEqualTo(StructureType.HOUSE);
    }

    @Test
    public void deepCloneableIsEqualToOriginal() {
        // Arrange
        final Position position = new Position(0, 0);
        final House house = new House(position);

        // Act
        final House deepClone = house.deepClone();

        // Assert
        assertThat(deepClone).isEqualTo(house);
    }

}
