package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mockito;

import com.thebois.Pawntastic;
import com.thebois.listeners.events.OnDeathEvent;
import com.thebois.models.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.terrains.Grass;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ColonyTests {

    public static Stream<Arguments> getItemTypes() {
        return Stream.of(Arguments.of(ItemType.LOG), Arguments.of(ItemType.ROCK));
    }

    @BeforeEach
    public void setup() {
        RoleFactory.setWorld(mock(IWorld.class));
        RoleFactory.setResourceFinder(mock(IResourceFinder.class));
        RoleFactory.setStructureFinder(mock(IStructureFinder.class));
    }

    @AfterEach
    public void teardown() {
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
        RoleFactory.setStructureFinder(null);
    }

    @Test
    public void constructWithTilesCreatesOneBeingPerPosition() {
        // Arrange
        final int beingCount = 25;
        final List<Position> positions = new ArrayList<>(beingCount);
        for (int i = 0; i < beingCount; i++) {
            positions.add(new Position(0, 0));
        }
        final IWorld mockWorld = mock(IWorld.class);
        when(mockWorld.getTileAt(any())).thenReturn(new Grass(new Position()));

        // Act
        final Colony colony = new Colony(positions);

        // Assert
        assertThat(colony.getBeings().size()).isEqualTo(beingCount);
    }

    private Colony mockColony() {
        final List<Position> positions = new ArrayList<>();
        return new Colony(positions);
    }

    @Test
    public void ensureAliveBeingsUpdatesWhenColonyUpdates() {
        // Arrange
        final IBeing being = Mockito.mock(IBeing.class);
        when(being.getHealthRatio()).then(returnValue -> 1f);
        final Colony colony = mockColony();

        colony.addBeing(being);
        final float deltaTime = 0.1f;

        // Act
        colony.update(deltaTime);

        // Assert
        verify(being, times(1)).update(deltaTime);
    }

    @Test
    public void beingGroupRemovesDeadBeings() {
        // Arrange
        final IBeing being = Mockito.mock(IBeing.class);
        final Colony colony = mockColony();
        colony.addBeing(being);
        final int expectedAmountOfBeings = 0;
        final float deltaTime = 0.1f;

        // Act
        Pawntastic.getEventBus().post(new OnDeathEvent(being));
        colony.update(deltaTime);
        final int actualAmountOfBeings = colony.getBeings().size();

        // Assert
        assertThat(actualAmountOfBeings).isEqualTo(expectedAmountOfBeings);
    }

}
