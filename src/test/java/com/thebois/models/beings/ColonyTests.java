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
import org.junit.jupiter.params.provider.Arguments;
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

    @Test
    public void colonyContainsSamePawnsAfterDeserialization() throws ClassNotFoundException, IOException {
        // Arrange
        final World world = new TestWorld(3, ThreadLocalRandom.current());
        final Colony colony = new Colony(world, EventBus::new);
        colony.addBeing(new Pawn(new Position(), RoleFactory.idle()));

        // Act
        final byte[] serialized1 = serialize(colony);
        final Colony deserialized1 = (Colony) deserialize(serialized1);

        // Assert
        assertThat(colony.getBeings()).isEqualTo(deserialized1.getBeings());
    }

    private byte[] serialize(final Object object) throws IOException {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ObjectOutputStream outputStream = new ObjectOutputStream(byteArrayOutputStream);
        outputStream.writeObject(object);
        return byteArrayOutputStream.toByteArray();
    }

    private Object deserialize(final byte[] bytes) throws IOException, ClassNotFoundException {
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        final ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        return objectInputStream.readObject();
    }

    @Test
    public void onSpawnPawnsEventColonySpawnsPawns() {
        // Arrange
        final EventBus eventBus = new EventBus();
        final World world = new TestWorld(50, ThreadLocalRandom.current());
        final IPositionFinder positionFinder = mock(IPositionFinder.class);

//        when(positionFinder.tryGetEmptyPositionsNextTo(any(), anyInt(), anyFloat())).thenReturn();

        final Colony colony = new Colony(world, ()->eventBus);
//        colony.addBeing(new Pawn(new Position(), RoleFactory.idle()));

        final int numberOfBeings = 2;

        final StructureCompletedEvent structureCompletedEvent = new StructureCompletedEvent(
            StructureType.HOUSE, new Position(5, 5));

        // Act
        colony.onStructureCompletedEvent(structureCompletedEvent);
        final Collection<IBeing> returnedBeings = colony.getBeings();

        // Assert
        assertThat(returnedBeings.size()).isEqualTo(numberOfBeings);
    }

}
