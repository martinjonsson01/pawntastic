package com.thebois.models.world;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.world.structures.Structure;
import com.thebois.models.world.structures.StructureType;

import static org.assertj.core.api.Assertions.*;

public class StructureTest {

    @Test
    public void getPositionTest() {
        final Position position = new Position(123, 456);
        final Structure structure = new Structure(123, 456);
        final Position structurePosition = structure.getPosition();

        assertThat(structurePosition).isEqualTo(position);
    }

    @Test
    public void getTypeTest() {

        final Position position = new Position(123, 456);
        final Structure structure = new Structure(position, StructureType.HOUSE);

        assertThat(structure.getType()).isEqualTo(StructureType.HOUSE);
    }

}
