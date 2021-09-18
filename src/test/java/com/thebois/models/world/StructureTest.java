package com.thebois.models.world;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class StructureTest {

    @Test
    void structureEqualsTest() {
        final Position postion = new Position(1, 1);
        final Structure structure1 = new Structure(postion);
        final Structure structure2 = new Structure(postion);
        final boolean isEqual = structure1.equals(structure2);
        assertThat(isEqual).as("Structure 1 is equal to Structure 2").isEqualTo(true);
    }

    @Test
    public void structureGetPositionTest() {
        final Position position = new Position(1, 4);
        final Structure structure = new Structure(position);
        final boolean isEqual = structure.getPosition().equals(position);
        assertThat(isEqual).isEqualTo(true);
    }

}
