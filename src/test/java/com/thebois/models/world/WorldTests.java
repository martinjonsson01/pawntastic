package com.thebois.models.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.models.Position;
import com.thebois.models.beings.Colony;
import com.thebois.models.inventory.IInventory;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.terrains.Dirt;
import com.thebois.models.world.terrains.ITerrain;

import static org.assertj.core.api.Assertions.*;

public class WorldTests {

    /* For a 3x3 world */
    public static Stream<Arguments> getTileAndNeighbours() {
        return Stream.of(Arguments.of(mockTile(0, 0),
                                      List.of(mockPosition(0, 1), mockPosition(1, 0))),
                         Arguments.of(mockTile(2, 0),
                                      List.of(mockPosition(2, 1), mockPosition(1, 0))),
                         Arguments.of(mockTile(0, 2),
                                      List.of(mockPosition(0, 1), mockPosition(1, 2))),
                         Arguments.of(mockTile(2, 2),
                                      List.of(mockPosition(2, 1), mockPosition(1, 2))),
                         Arguments.of(mockTile(1, 1), List.of(mockPosition(1, 0),
                                                              mockPosition(0, 1),
                                                              mockPosition(2, 1),
                                                              mockPosition(1, 2))));
    }

    private static Position mockPosition(final int positionX, final int positionY) {
        return new Position(positionX, positionY);
    }

    private static ITile mockTile(final int positionX, final int positionY) {
        return new Dirt(positionX, positionY);
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
    public void getColonyInventoryReturnsNotNull() {
        // Arrange
        final World world = new World(2, 5, 0);

        // Act
        final IInventory inventory = world.getColonyInventory();

        // Assert
        assertThat(inventory).isNotNull();
    }

    public static Stream<Arguments> getWorldSizeAndPawnCount() {
        return Stream.of(Arguments.of(2, 4),
                         Arguments.of(100, 40),
                         Arguments.of(25, 5),
                         Arguments.of(10, 100));
    }

    @ParameterizedTest
    @MethodSource("getWorldSizeAndPawnCount")
    public void worldInitiated(final int worldSize, final int pawnCount) {
        // Arrange
        final World world = new World(worldSize, pawnCount, 0);
        final int actualWorldSize;
        final int actualPawnCount;

        // Act
        actualWorldSize = (int) Math.sqrt(world.getTerrainTiles().size());
        actualPawnCount = world.getColony().getBeings().size();

        // Assert
        assertThat(actualPawnCount).as("Pawn count").isEqualTo(pawnCount);
        assertThat(actualWorldSize).as("World size").isEqualTo(worldSize);
    }

    private Collection<ITerrain> mockTerrainTiles() {
        final ArrayList<ITerrain> terrainTiles = new ArrayList<>();
        terrainTiles.add(new Dirt(0, 0));
        terrainTiles.add(new Dirt(0, 1));
        terrainTiles.add(new Dirt(1, 0));
        terrainTiles.add(new Dirt(1, 1));
        return terrainTiles;
    }

    @Test
    public void worldFind() {
        // Arrange
        final World world = new World(2, 5, 0);

        // Act
        final Object worldObject = world.find();

        // Assert
        assertThat(worldObject).isEqualTo(null);
    }

    @ParameterizedTest
    @MethodSource("getTileAndNeighbours")
    public void getNeighboursOfReturnsExpectedNeighbours(
        final ITile tile, final Iterable<Position> expectedNeighbours) {
        // Arrange
        final IWorld world = new World(3, 0, 0);

        // Act
        final Iterable<ITile> actualNeighbours = world.getNeighboursOf(tile);
        final Collection<Position> positions = new ArrayList<>();
        for (final ITile actualNeighbour : actualNeighbours) {
            positions.add(actualNeighbour.getPosition());
        }
        // Assert
        assertThat(positions).containsExactlyInAnyOrderElementsOf(expectedNeighbours);
    }

    @Test
    public void getTileAtReturnsTileAtGivenPosition() {
        // Arrange
        final Collection<ITerrain> expectedTerrainTiles = mockTerrainTiles();
        final World world = new World(2, 0, 0);

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
        final World world = new World(2, 0, 0);

        // Assert
        assertThatThrownBy(() -> world.getTileAt(outOfBounds)).isInstanceOf(
            IndexOutOfBoundsException.class);
    }

    @Test
    public void createWorldWithNoStructures() {
        // Arrange
        final World world = new World(2, 0, 0);

        // Act
        final Collection<IStructure> structures = world.getStructures();

        // Assert
        assertThat(structures.size()).isEqualTo(0);
    }

    @Test
    public void numberOfStructuresIncreasesIfStructureSuccessfullyPlaced() {
        // Arrange
        final World world = new World(2, 0, 0);
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
        final World world = new World(2, 0, 0);
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
        final World world = new World(2, 0, 0);
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
        final Colony colony = Mockito.mock(Colony.class);
        final World world = new World(2, colony, 0);

        // Assert
        assertThat(world.getColony()).isEqualTo(colony);
    }

    @Test
    public void getRoleAllocatorReturnsRoleAllocator() {
        // Arrange
        final Colony colony = Mockito.mock(Colony.class);
        final World world = new World(2, colony, 0);

        // Assert
        assertThat(world.getRoleAllocator()).isEqualTo(colony);
    }

    @Test
    public void instantiateWithPawnCountCreatesCorrectNumberOfBeings() {
        // Arrange
        final int pawnCount = 5;

        // Act
        final World world = new World(3, pawnCount, 0);

        // Assert
        assertThat(world.getColony().getBeings()).size().isEqualTo(pawnCount);
    }

    @Test
    public void instantiateWithPawnCountCreatesOnlyAsManyBeingsAsFitInTheWorld() {
        // Arrange
        final int pawnCount = 5;
        final int pawnFitCount = 4;

        // Act
        final World world = new World(2, pawnCount, 0);

        // Assert
        assertThat(world.getColony().getBeings()).size().isEqualTo(pawnFitCount);
    }

    @Test
    public void testsIfColonyGetsUpdate() {
        // Arrange
        final Colony colony = Mockito.mock(Colony.class);
        final World world = new World(2, colony, 0);

        // Act
        world.update();

        // Arrange
        Mockito.verify(colony, Mockito.atLeastOnce()).update();
        assertThat(world.getColony()).isEqualTo(colony);
    }

}
