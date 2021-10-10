package com.thebois.models.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.Colony;
import com.thebois.models.beings.pathfinding.AstarPathFinder;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.structures.StructureType;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class WorldTests {

    /* For a 3x3 world */
    public static Stream<Arguments> getTileAndNeighbours() {
        return Stream.of(Arguments.of(mockTile(0, 0), List.of(mockTile(0, 1), mockTile(1, 0))),
                         Arguments.of(mockTile(2, 0), List.of(mockTile(2, 1), mockTile(1, 0))),
                         Arguments.of(mockTile(0, 2), List.of(mockTile(0, 1), mockTile(1, 2))),
                         Arguments.of(mockTile(2, 2), List.of(mockTile(2, 1), mockTile(1, 2))),
                         Arguments.of(mockTile(1, 1),
                                      List.of(mockTile(1, 0),
                                              mockTile(0, 1),
                                              mockTile(2, 1),
                                              mockTile(1, 2))));
    }

    private static ITile mockTile(final int positionX, final int positionY) {
        return new Grass(positionX, positionY);
    }

    public static Stream<Arguments> getPositionOutSideOfWorld() {
        return Stream.of(Arguments.of(new Position(-1, 0)),
                         Arguments.of(new Position(0, -1)),
                         Arguments.of(new Position(-1, -1)),
                         Arguments.of(new Position(-1000, -1000)),
                         Arguments.of(new Position(10000, 0)),
                         Arguments.of(new Position(0, 10000)));
    }

    /* For a 2x2 world */
    public static Stream<Arguments> getOutOfBoundsPositions() {
        return Stream.of(Arguments.of(new Position(-1, 0)),
                         Arguments.of(new Position(3, 0)),
                         Arguments.of(new Position(0, -1)),
                         Arguments.of(new Position(0, 3)),
                         Arguments.of(new Position(-1, 3)));
    }

    @BeforeEach
    public void setup() {
        RoleFactory.setWorld(mock(IWorld.class));
    }

    @AfterEach
    public void teardown() {
        RoleFactory.setWorld(null);
    }

    @Test
    public void getNearbyOfTypeResourceReturnsEmptyWhenNothingNearby() {
        // Arrange
        final IResourceFinder finder = createWorld(10);
        final Position origin = new Position();

        // Act
        final Optional<IStructure> maybeResource = finder.getNearbyOfType(origin,
                                                                          StructureType.HOUSE);

        // Assert
        assertThat(maybeResource).isEmpty();
    }

    private World createWorld(final int size) {
        return createWorld(size, mock(Random.class));
    }

    private World createWorld(final int size, final Random random) {
        return new World(size, random);
    }

    @Test
    public void getNearbyOfTypeResourceReturnsItWhenSingleNearby() {
        // Arrange
        final World world = createWorld(10);
        world.createStructure(5, 5);
        final IStructure expectedResource =
            world.getStructures().stream().findFirst().orElseThrow();
        final Position origin = new Position();
        final IResourceFinder finder = world;

        // Act
        final Optional<IStructure> maybeResource = finder.getNearbyOfType(origin,
                                                                          StructureType.HOUSE);

        // Assert
        assertThat(maybeResource).hasValue(expectedResource);
    }

    @Test
    public void getNearbyOfTypeResourceReturnsClosestWhenMultipleNearby() {
        // Arrange
        final World world = createWorld(10);
        final Position expectedResourcePosition = new Position(9, 6);
        world.createStructure(expectedResourcePosition);
        world.createStructure(3, 4);
        world.createStructure(5, 5);
        final IStructure expectedResource =
            world.getStructures().stream().filter(resource -> resource
                .getPosition()
                .equals(expectedResourcePosition)).findFirst().orElseThrow();
        final Position origin = new Position(9, 9);
        final IResourceFinder finder = world;

        // Act
        final Optional<IStructure> maybeResource = finder.getNearbyOfType(origin,
                                                                          StructureType.HOUSE);

        // Assert
        assertThat(maybeResource).hasValue(expectedResource);
    }

    @Test
    public void getRandomVacantSpotReturnsRandomTileWhenThereAreNoObstacles() {
        // Arrange
        final Random mockRandom = mock(Random.class);
        final int randomCoordinate = 1;
        when(mockRandom.nextInt(anyInt())).thenReturn(randomCoordinate);
        final IWorld world = createWorld(3, mockRandom);
        final Position expectedSpot = new Position(randomCoordinate, randomCoordinate);

        // Act
        final Position vacantSpot = world.getRandomVacantSpot().getPosition();

        // Assert
        assertThat(vacantSpot).isEqualTo(expectedSpot);
    }

    @Test
    public void getRandomVacantSpotReturnsRandomTileWhenThereObstacles() {
        // Arrange
        final Random mockRandom = mock(Random.class);
        final Position firstBlockedRandomSpot = new Position(0, 1);
        final Position secondBlockedRandomSpot = new Position(1, 1);
        final Position thirdEmptyRandomSpot = new Position(2, 0);
        when(mockRandom.nextInt(anyInt())).thenReturn((int) firstBlockedRandomSpot.getPosX(),
                                                      (int) firstBlockedRandomSpot.getPosY(),
                                                      (int) secondBlockedRandomSpot.getPosX(),
                                                      (int) secondBlockedRandomSpot.getPosY(),
                                                      (int) thirdEmptyRandomSpot.getPosX(),
                                                      (int) thirdEmptyRandomSpot.getPosY());
        final World world = createWorld(3, mockRandom);
        world.createStructure(firstBlockedRandomSpot);
        world.createStructure(secondBlockedRandomSpot);

        // Act
        final Position vacantSpot = world.getRandomVacantSpot().getPosition();

        // Assert
        assertThat(vacantSpot).isEqualTo(thirdEmptyRandomSpot);
    }

    @Test
    public void worldInitiated() {
        // Arrange
        final Collection<ITerrain> expectedTerrainTiles = mockTerrainTiles();
        final World world = createWorld(2);

        // Act
        final Collection<ITerrain> terrainTiles = world.getTerrainTiles();

        // Assert
        assertThat(terrainTiles).containsAll(expectedTerrainTiles);
    }

    private Collection<ITerrain> mockTerrainTiles() {
        final ArrayList<ITerrain> terrainTiles = new ArrayList<>();
        terrainTiles.add(new Grass(0, 0));
        terrainTiles.add(new Grass(0, 1));
        terrainTiles.add(new Grass(1, 0));
        terrainTiles.add(new Grass(1, 1));
        return terrainTiles;
    }

    @Test
    public void worldFind() {
        // Arrange
        final World world = createWorld(2);

        // Act
        final Object worldObject = world.find();

        // Assert
        assertThat(worldObject).isEqualTo(null);
    }

    @ParameterizedTest
    @MethodSource("getTileAndNeighbours")
    public void getNeighboursOfReturnsExpectedNeighbours(
        final ITile tile, final Iterable<ITile> expectedNeighbours) {
        // Arrange
        final IWorld world = createWorld(3);

        // Act
        final Iterable<ITile> actualNeighbours = world.getNeighboursOf(tile);

        // Assert
        assertThat(actualNeighbours).containsExactlyInAnyOrderElementsOf(expectedNeighbours);
    }

    @Test
    public void getTileAtReturnsTileAtGivenPosition() {
        // Arrange
        final Collection<ITerrain> expectedTerrainTiles = mockTerrainTiles();
        final World world = createWorld(2);

        for (final ITile tile : expectedTerrainTiles) {
            // Act
            final ITile tileAtPosition = world.getTileAt(tile.getPosition());

            // Assert
            assertThat(tileAtPosition).isEqualTo(tile);
        }
    }

    @ParameterizedTest
    @MethodSource("getOutOfBoundsPositions")
    public void getTileAtThrowsWhenOutOfBounds(final Position outOfBounds) {
        // Arrange
        final World world = createWorld(2);

        // Assert
        assertThatThrownBy(() -> world.getTileAt(outOfBounds)).isInstanceOf(
            IndexOutOfBoundsException.class);
    }

    @Test
    public void createWorldWithNoStructures() {
        // Arrange
        final World world = createWorld(2);

        // Act
        final Collection<IStructure> structures = world.getStructures();

        // Assert
        assertThat(structures.size()).isEqualTo(0);
    }

    @Test
    public void numberOfStructuresIncreasesIfStructureSuccessfullyPlaced() {
        // Arrange
        final World world = createWorld(2);
        final Position position = new Position(1, 1);
        final Collection<IStructure> structures;
        final boolean isBuilt;
        // Act
        isBuilt = world.createStructure(position);
        structures = world.getStructures();

        // Assert
        assertThat(isBuilt).isTrue();
        assertThat(structures.size()).isEqualTo(1);
    }

    @ParameterizedTest
    @MethodSource("getPositionOutSideOfWorld")
    public void structureFailedToBePlaced(final Position placementPosition) {
        // Arrange
        final World world = createWorld(2);
        final Collection<IStructure> structures;
        final boolean isBuilt;

        // Act
        isBuilt = world.createStructure(placementPosition);
        structures = world.getStructures();

        // Assert
        assertThat(isBuilt).isFalse();
        assertThat(structures.size()).isEqualTo(0);
    }

    @Test
    public void numberOfStructuresDoesNotChangeIfStructuresPlacedOutsideWorld() {
        // Arrange
        final World world = createWorld(2);
        final Position position1 = new Position(2, 2);
        final Position position2 = new Position(-1, -1);
        final Collection<IStructure> structures;

        // Act
        world.createStructure(position1);
        world.createStructure(position2);
        structures = world.getStructures();

        // Assert
        assertThat(structures.size()).isEqualTo(0);
    }

    @Test
    public void instantiateWithPawnCountCreatesCorrectNumberOfBeings() {
        // Arrange
        final Colony colony = mockColonyWithMockBeings();

        // Assert
        assertThat(colony.getBeings()).size().isEqualTo(5);
    }

    private Colony mockColonyWithMockBeings() {
        final List<Position> vacantPositions = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            vacantPositions.add(new Position(0, 0));
        }
        return new Colony(vacantPositions);
    }

    @Test
    public void instantiateWithPawnCountCreatesOnlyAsManyBeingsAsFitInTheWorld() {
        // Arrange
        final int pawnFitCount = 4;
        final World world = createWorld(2);
        final Iterable<Position> vacantPositions = world.findEmptyPositions(5);
        final IPathFinder pathFinder = new AstarPathFinder(world);

        // Act
        final Colony colony = new Colony(vacantPositions);

        // Assert
        assertThat(colony.getBeings()).size().isEqualTo(pawnFitCount);
    }

}
