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

import com.thebois.abstractions.IPositionFinder;
import com.thebois.abstractions.IResourceFinder;
import com.thebois.abstractions.IStructureFinder;
import com.thebois.listeners.events.OnDeathEvent;
import com.thebois.listeners.events.StructureCompletedEvent;
import com.thebois.models.Position;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.inventory.Inventory;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.TestWorld;
import com.thebois.models.world.World;
import com.thebois.models.world.structures.StructureType;

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
    public void ensureAliveBeingsUpdatesWhenColonyUpdates() {
        // Arrange
        final IBeing being = Mockito.mock(IBeing.class);
        when(being.getHealthRatio()).then(returnValue -> 1f);
        final Colony colony = createColony();

        colony.addBeing(being);
        final float deltaTime = 0.1f;

        // Act
        colony.update(deltaTime);

        // Assert
        verify(being, times(1)).update(deltaTime);
    }

    private Colony createColony() {
        final IPositionFinder positionFinder = Mockito.mock(IPositionFinder.class);

        final EventBus mockEventBusSource = mock(EventBus.class);
        return new Colony(positionFinder, () -> mockEventBusSource);
    }

    public static Stream<Arguments> getStructureCompletedEventsToTest() {
        final Position position = new Position(10, 10);

        return Stream.of(Arguments.of(
                             new StructureCompletedEvent(StructureType.HOUSE, position),
                             1),
                         Arguments.of(new StructureCompletedEvent(
                             StructureType.TOWN_HALL,
                             position), 5),
                         Arguments.of(new StructureCompletedEvent(
                             StructureType.STOCKPILE,
                             position), 0));
    }

    @ParameterizedTest
    @MethodSource("getStructureCompletedEventsToTest")
    public void onSpawnPawnsEventColonySpawnsPawns(
        final StructureCompletedEvent event,
        final int numberOfBeings) {
        // Arrange
        final EventBus eventBus = new EventBus();
        final World world = new TestWorld(50, ThreadLocalRandom.current());
        final Colony colony = new Colony(world, () -> eventBus);

        // Act
        colony.onStructureCompletedEvent(event);
        final Collection<IBeing> returnedBeings = colony.getBeings();

        // Assert
        assertThat(returnedBeings.size()).isEqualTo(numberOfBeings);
    }

    @Test
    public void beingGroupRemovesDeadBeings() {
        // Arrange
        final float deltaTime = 0.1f;
        final IBeing being = Mockito.mock(IBeing.class);
        final Colony colony = createColony();
        colony.addBeing(being);
        colony.update(deltaTime);
        final int expectedAmountOfBeings = 0;

        // Act
        colony.onDeathEvent(new OnDeathEvent(being));
        colony.update(deltaTime);
        final int actualAmountOfBeings = colony.getBeings().size();

        // Assert
        assertThat(actualAmountOfBeings).isEqualTo(expectedAmountOfBeings);
    }

    @Test
    public void colonyContainsSamePawnsAfterDeserialization() throws
                                                              ClassNotFoundException,
                                                              IOException {
        // Arrange
        final World world = new TestWorld(50, ThreadLocalRandom.current());
        final Colony colony = new Colony(world, EventBus::new);

        RoleFactory.setResourceFinder(world);
        RoleFactory.setStructureFinder(world);
        RoleFactory.setWorld(world);

        colony.addBeing(new Pawn(
            new Position(),
            RoleFactory.idle(),
            RoleFactory.fisher(),
            EventBus::new,
            new Inventory()));

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

}
