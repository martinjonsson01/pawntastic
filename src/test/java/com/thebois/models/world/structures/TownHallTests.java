package com.thebois.models.world.structures;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.abstractions.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.world.IWorld;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
    public void townHallHasAlways1AsBuiltRatio() {
        // Arrange
        final IStructure townHall = StructureFactory.createStructure(
            StructureType.TOWN_HALL,
            new Position());

        // Act
        final float builtRatio = townHall.getBuiltRatio();

        // Assert
        assertThat(builtRatio).isGreaterThanOrEqualTo(1f);
    }

}
