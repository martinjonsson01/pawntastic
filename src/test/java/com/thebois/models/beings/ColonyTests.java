package com.thebois.models.beings;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mockito;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.IStructureFinder;
import com.thebois.abstractions.IPositionFinder;
import com.thebois.abstractions.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.beings.pathfinding.AstarPathFinder;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.inventory.items.IItem;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.IWorld;
import com.thebois.models.world.TestWorld;
import com.thebois.models.world.World;
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
        final IPositionFinder positionFinder = Mockito.mock(IPositionFinder.class);
        return new Colony(positionFinder);
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
        final World world = new TestWorld(3);
        final IPathFinder pathFinder = new AstarPathFinder(world);

        final Colony colony = new Colony(pathFinder, world, world);

        colony.addBeing(new Pawn(new Position(), new Position(), new Random(), pathFinder, world));

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
