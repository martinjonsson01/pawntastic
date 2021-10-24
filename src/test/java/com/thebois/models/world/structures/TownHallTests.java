package com.thebois.models.world.structures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.models.Position;
import com.thebois.models.inventory.IInventory;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TownHallTests {

    @BeforeEach
    public void setUp() {
        StructureFactory.setInventory(mock(IInventory.class));
    }

    @AfterEach
    public void teardown() {
        StructureFactory.setInventory(null);
    }

    @Test
    public void townHallIsAlwaysCompleted() {
        // Arrange
        final IStructure townHall =
            StructureFactory.createStructure(StructureType.TOWN_HALL, new Position());

        // Act
        final boolean isComplete = townHall.isCompleted();

        // Assert
        assertThat(isComplete).isTrue();
    }

    @Test
    public void townHallHasAlwaysOneAsBuiltRatio() {
        // Arrange
        final IStructure townHall =
            StructureFactory.createStructure(StructureType.TOWN_HALL, new Position());

        // Act
        final float builtRatio = townHall.getBuiltRatio();

        // Assert
        assertThat(builtRatio).isGreaterThanOrEqualTo(1f);
    }

    @Test
    public void townHallGetCostReturnsCorrect() {
        // Arrange
        final IStructure townHall =
            StructureFactory.createStructure(StructureType.TOWN_HALL, new Position());

        // Act
        final float tileCost = townHall.getCost();

        // Assert
        assertThat(tileCost).isEqualTo(Float.MAX_VALUE);
    }

}
