package com.thebois.models.beings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import com.google.common.eventbus.EventBus;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.abstractions.IPositionFinder;
import com.thebois.abstractions.IStructureFinder;
import com.thebois.listeners.events.StructureCompletedEvent;
import com.thebois.models.Position;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.TestWorld;
import com.thebois.models.world.World;
import com.thebois.models.world.resources.ResourceType;
import com.thebois.models.world.resources.Stone;
import com.thebois.models.world.resources.Tree;
import com.thebois.models.world.resources.Water;
import com.thebois.models.world.structures.StructureType;

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

//    @Test
//    public void constructWithTilesCreatesOneBeingPerPosition() {
//        // Arrange
//        final int beingCount = 25;
//        final EventBus mockEventBusSource = mock(EventBus.class);
//        final IPositionFinder positionFinder = mock(IPositionFinder.class);
//        final List<Position> positions = new ArrayList<>(beingCount);
//        for (int i = 0; i < beingCount; i++) {
//            positions.add(new Position(0, 0));
//        }
//        final IWorld mockWorld = mock(IWorld.class);
//        when(mockWorld.getTileAt(any())).thenReturn(new Grass(new Position()));
//
//        // Act
//        final Colony colony = new Colony(positionFinder, ()->mockEventBusSource);
//
//        // Assert
//        assertThat(colony.getBeings().size()).isEqualTo(beingCount);
//    }

    private Colony mockColony() {
        final IPositionFinder positionFinder = Mockito.mock(IPositionFinder.class);

        final EventBus mockEventBusSource = mock(EventBus.class);
        return new Colony(positionFinder, ()->mockEventBusSource);
    }

    @Test
    public void ensureBeingsUpdatesWhenColonyUpdates() {
        // Arrange
        final IBeing being = Mockito.mock(IBeing.class);
        final Colony colony = mockColony();

        colony.addBeing(being);
        final float deltaTime = 0.1f;

        // Act
        colony.update(deltaTime);

        // Assert
        verify(being, times(1)).update(deltaTime);
    }

    public static Stream<Arguments> getStructureCompletedEventsToTest() {
        final Position position = new Position(10, 10);

        return Stream.of(
            Arguments.of(new StructureCompletedEvent(StructureType.HOUSE, position), 2),
            Arguments.of(new StructureCompletedEvent(StructureType.TOWN_HALL, position), 5),
            Arguments.of(new StructureCompletedEvent(StructureType.STOCKPILE, position), 0));
    }

    @ParameterizedTest
    @MethodSource("getStructureCompletedEventsToTest")
    public void onSpawnPawnsEventColonySpawnsPawns(final StructureCompletedEvent event, final int numberOfBeings) {
        // Arrange
        final EventBus eventBus = new EventBus();
        final World world = new TestWorld(50, ThreadLocalRandom.current());
        final Colony colony = new Colony(world, ()->eventBus);

        // Act
        colony.onStructureCompletedEvent(event);
        final Collection<IBeing> returnedBeings = colony.getBeings();

        // Assert
        assertThat(returnedBeings.size()).isEqualTo(numberOfBeings);
    }

}
