package com.thebois.models.world.structures;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class TownHallTests {

    @Test
    public void townHallIsAlwaysCompleted() {
        // Arrange
        final IStructure townHall = StructureFactory.createStructure(
            StructureType.TOWN_HALL,
            new Position());

        // Act
        final boolean isComplete = townHall.isCompleted();

        // Assert
        assertThat(isComplete).isTrue();
    }

    @Test
    public void townHallHasAlwaysOneAsBuiltRatio() {
        // Arrange
        final IStructure townHall = StructureFactory.createStructure(
            StructureType.TOWN_HALL,
            new Position());

        // Act
        final float builtRatio = townHall.getBuiltRatio();

        // Assert
        assertThat(builtRatio).isGreaterThanOrEqualTo(1f);
    }

    @Test
    public void townHallGetCostReturnsCorrect() {
        // Arrange
        final IStructure townHall = StructureFactory.createStructure(
            StructureType.TOWN_HALL,
            new Position());

        // Act
        final float tileCost = townHall.getCost();

        // Assert
        assertThat(tileCost).isEqualTo(Float.MAX_VALUE);
    }

}
