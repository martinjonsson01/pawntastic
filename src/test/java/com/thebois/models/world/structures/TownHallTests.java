package com.thebois.models.world.structures;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class TownHallTests {

    @Test
    public void TownHallIsAlwaysCompleted() {

        // Arrange
        final IStructure house = StructureFactory.createStructure(
            StructureType.TOWN_HALL,
            new Position());

        // Act
        final boolean isComplete = house.isCompleted();

        // Assert
        assertThat(isComplete).isTrue();
    }

    @Test
    public void TownHallHasAlways1AsBuiltRatio() {

        // Arrange
        final IStructure house = StructureFactory.createStructure(
            StructureType.TOWN_HALL,
            new Position());

        // Act
        final float builtRatio = house.getBuiltRatio();

        // Assert
        assertThat(Float.compare(builtRatio, 1) >= 0).isTrue();
    }

}
