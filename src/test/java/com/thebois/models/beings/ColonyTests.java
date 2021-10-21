package com.thebois.models.beings;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.thebois.Pawntastic;
import com.thebois.abstractions.IResourceFinder;
import com.thebois.listeners.events.OnDeathEvent;
import com.thebois.models.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.terrains.Grass;
import com.thebois.testutils.InMemorySerialize;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ColonyTests {

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

    @Test
    public void sameObjectAfterDeserialization() throws ClassNotFoundException, IOException {
        // Arrange
        final AbstractBeingGroup colony = mock(Colony.class);
        final OnDeathEvent deathEvent = new OnDeathEvent(mock(IBeing.class));
        //Act
        final byte[] serializedColony = InMemorySerialize.serialize(colony);
        final AbstractBeingGroup deserializedColony =
            (AbstractBeingGroup) InMemorySerialize.deserialize(serializedColony);
        Pawntastic.getEventBus().post(deathEvent);

        // Assert
        verify(deserializedColony, times(1)).onDeathEvent(deathEvent);
    }

}
