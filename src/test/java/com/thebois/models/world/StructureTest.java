package com.thebois.models.world;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.world.structures.Structure;
import com.thebois.models.world.structures.StructureType;

import static org.assertj.core.api.Assertions.*;

public class StructureTest {

    @Test
    public void getPositionTest() {

        // Arrange
        final Position position = new Position(123, 456);
        final Structure structure = new Structure(123, 456);

        // Act
        final Position structurePosition = structure.getPosition();

        // Assert
        assertThat(structurePosition).isEqualTo(position);
    }

    @Test
    public void getTypeTest() {

        // Arrange
        final Position position = new Position(123, 456);

        // Act
        final Structure structure = new Structure(position, StructureType.HOUSE);

        // Assert
        assertThat(structure.getType()).isEqualTo(StructureType.HOUSE);
    }

}
