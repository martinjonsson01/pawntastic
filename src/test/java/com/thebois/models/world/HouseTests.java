package com.thebois.models.world;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.world.structures.House;
import com.thebois.models.world.structures.StructureType;

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

}
