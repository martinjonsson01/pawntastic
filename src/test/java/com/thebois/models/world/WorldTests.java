package com.thebois.models.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.models.Position;
import com.thebois.models.beings.Colony;
import com.thebois.models.world.structures.IStructure;

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

    @Test
    public void getRandomVacantSpotReturnsRandomTileWhenThereAreNoObstacles() {
        // Arrange
        final Random mockRandom = mock(Random.class);
        final int randomCoordinate = 1;
        when(mockRandom.nextInt(anyInt())).thenReturn(randomCoordinate);
        final IWorld world = new World(3, 0, mockRandom);
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
        final World world = new World(3, 0, mockRandom);
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
        final World world = new World(2, 5, mock(Random.class));

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
        final World world = new World(2, 5, mock(Random.class));

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
        final IWorld world = new World(3, 0, mock(Random.class));

        // Act
        final Iterable<ITile> actualNeighbours = world.getNeighboursOf(tile);

        // Assert
        assertThat(actualNeighbours).containsExactlyInAnyOrderElementsOf(expectedNeighbours);
    }

    @Test
    public void getTileAtReturnsTileAtGivenPosition() {
        // Arrange
        final Collection<ITerrain> expectedTerrainTiles = mockTerrainTiles();
        final World world = new World(2, 0, mock(Random.class));

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
        final World world = new World(2, 0, mock(Random.class));

        // Assert
        assertThatThrownBy(() -> world.getTileAt(outOfBounds)).isInstanceOf(
            IndexOutOfBoundsException.class);
    }

    @Test
    public void createWorldWithNoStructures() {
        // Arrange
        final World world = new World(2, 0, mock(Random.class));

        // Act
        final Collection<IStructure> structures = world.getStructures();

        // Assert
        assertThat(structures.size()).isEqualTo(0);
    }

    @Test
    public void numberOfStructuresIncreasesIfStructureSuccessfullyPlaced() {
        // Arrange
        final World world = new World(2, 0, mock(Random.class));
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
        final World world = new World(2, 0, mock(Random.class));
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
        final World world = new World(2, 0, mock(Random.class));
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
    public void getColonyReturnsSameColony() {
        // Arrange
        final Colony colony = mock(Colony.class);
        final World world = new World(2, colony, mock(Random.class));

        // Assert
        assertThat(world.getColony()).isEqualTo(colony);
    }

    @Test
    public void getRoleAllocatorReturnsRoleAllocator() {
        // Arrange
        final Colony colony = mock(Colony.class);
        final World world = new World(2, colony, mock(Random.class));

        // Assert
        assertThat(world.getRoleAllocator()).isEqualTo(colony);
    }

    @Test
    public void instantiateWithPawnCountCreatesCorrectNumberOfBeings() {
        // Arrange
        final int pawnCount = 5;

        // Act
        final World world = new World(3, pawnCount, mock(Random.class));

        // Assert
        assertThat(world.getColony().getBeings()).size().isEqualTo(pawnCount);
    }

    @Test
    public void instantiateWithPawnCountCreatesOnlyAsManyBeingsAsFitInTheWorld() {
        // Arrange
        final int pawnCount = 5;
        final int pawnFitCount = 4;

        // Act
        final World world = new World(2, pawnCount, mock(Random.class));

        // Assert
        assertThat(world.getColony().getBeings()).size().isEqualTo(pawnFitCount);
    }

    @Test
    public void testsIfColonyGetsUpdate() {
        // Arrange
        final Colony colony = mock(Colony.class);
        final World world = new World(2, colony, mock(Random.class));

        // Act
        world.update();

        // Arrange
        Mockito.verify(colony, Mockito.atLeastOnce()).update();
        assertThat(world.getColony()).isEqualTo(colony);
    }

}
