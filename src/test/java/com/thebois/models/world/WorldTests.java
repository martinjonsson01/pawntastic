package com.thebois.models.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
    public void worldInitiated() {
        // Arrange
        final Collection<ITerrain> expectedTerrainTiles = mockTerrainTiles();
        final World world = new World(2, 5);

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

    @ParameterizedTest
    @MethodSource("getTileAndNeighbours")
    public void getNeighboursOfReturnsExpectedNeighbours(
        final ITile tile, final Iterable<ITile> expectedNeighbours) {
        // Arrange
        final IWorld world = new World(3, 0);

        // Act
        final Iterable<ITile> actualNeighbours = world.getNeighboursOf(tile);

        // Assert
        assertThat(actualNeighbours).containsExactlyInAnyOrderElementsOf(expectedNeighbours);
    }

    @Test
    public void getTileAtReturnsTileAtGivenPosition() {
        // Arrange
        final Collection<ITerrain> expectedTerrainTiles = mockTerrainTiles();
        final World world = new World(2, 0);

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
        final World world = new World(2, 0);

        // Assert
        assertThatThrownBy(() -> world.getTileAt(outOfBounds)).isInstanceOf(
            IndexOutOfBoundsException.class);
    }

    @Test
    public void createWorldWithNoStructures() {
        // Arrange
        final World world = new World(2, 0);

        // Act
        final Collection<IStructure> structures = world.getStructures();

        // Assert
        assertThat(structures.size()).isEqualTo(0);
    }

    @Test
    public void numberOfStructuresIncreasesIfStructureSuccessfullyPlaced() {
        // Arrange
        final World world = new World(2, 0);
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
        final World world = new World(2, 0);
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
        final World world = new World(2, 0);
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
        final World world = new World(2, colony);

        // Assert
        assertThat(world.getColony()).isEqualTo(colony);
    }

    @Test
    public void getRoleAllocatorReturnsRoleAllocator() {
        // Arrange
        final Colony colony = Mockito.mock(Colony.class);
        final World world = new World(2, colony);

        // Assert
        assertThat(world.getRoleAllocator()).isEqualTo(colony);
    }

    @Test
    public void instantiateWithPawnCountCreatesCorrectNumberOfBeings() {
        // Arrange
        final int pawnCount = 5;

        // Act
        final World world = new World(3, pawnCount);

        // Assert
        assertThat(world.getColony().getBeings()).size().isEqualTo(pawnCount);
    }

    @Test
    public void instantiateWithPawnCountCreatesOnlyAsManyBeingsAsFitInTheWorld() {
        // Arrange
        final int pawnCount = 5;
        final int pawnFitCount = 4;

        // Act
        final World world = new World(2, pawnCount);

        // Assert
        assertThat(world.getColony().getBeings()).size().isEqualTo(pawnFitCount);
    }

    @Test
    public void testsIfColonyGetsUpdate() {
        // Arrange
        final Colony colony = Mockito.mock(Colony.class);
        final World world = new World(2, colony);

        // Act
        world.update();

        // Arrange
        Mockito.verify(colony, Mockito.atLeastOnce()).update();
        assertThat(world.getColony()).isEqualTo(colony);
    }

    private boolean matrixEquals(final ITile[][] worldMatrix1, final ITile[][] worldMatrix2) {
        for (int y = 0; y < worldMatrix2.length; y++) {
            final ITile[] row = worldMatrix2[y];
            for (int x = 0; x < row.length; x++) {
                if (worldMatrix1[y][x] != null) {
                    if (!worldMatrix1[y][x].equals(worldMatrix2[y][x])) {
                        return false;
                    }
                }
                else {
                    if (worldMatrix2[y][x] != null) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private static Stream<Arguments> getCorrectCoordinatesToTest() {
        return Stream.of(Arguments.of(0, 0, 2, 2),
                         Arguments.of(5, 5, 10, 10),
                         Arguments.of(0, 0, 0, 0),
                         Arguments.of(20, 20, 11, 11),
                         Arguments.of(49, 49, 40, 40)
        );
    }

    @ParameterizedTest
    @MethodSource("getCorrectCoordinatesToTest")
    public void testFindNearestStructure(final int startX,
                                         final int startY,
                                         final int endX,
                                         final int endY) {

        // Arrange
        final World world = new World(50, 10);
        world.createStructure(endX, endY);

        // Act
        final Optional<IStructure> foundStructure = world.findNearestStructure(new Position(
            startX,
            startY));

        // Assert
        if (foundStructure.isPresent()) {
            assertThat(foundStructure.get().getPosition()).isEqualTo(new Position(endX, endY));
        }
        else {
            assertThat(foundStructure.isEmpty()).isEqualTo(false);
        }
    }

    private static Stream<Arguments> getInCorrectCoordinatesToTest() {
        return Stream.of(Arguments.of(0, 0, 60, 60),
                         Arguments.of(5, 5, 100, 100),
                         Arguments.of(0, 0, 49, 26),
                         Arguments.of(20, 10, 49, 49),
                         Arguments.of(40, 0, 40, 40)
        );
    }

    @ParameterizedTest
    @MethodSource("getInCorrectCoordinatesToTest")
    public void testNotEqualsFindNearestStructure(final int startX,
                                         final int startY,
                                         final int endX,
                                         final int endY) {

        // Arrange
        final World world = new World(50, 10);
        world.createStructure(endX, endY);

        // Act
        final Optional<IStructure> foundStructure = world.findNearestStructure(new Position(
            startX, startY));

        // Assert
        if (foundStructure.isPresent()) {
            assertThat(foundStructure.get().getPosition()).isNotEqualTo(new Position(endX, endY));
        }
        else {
            assertThat(foundStructure.isEmpty()).isEqualTo(true);
        }
    }

    private static Stream<Arguments> getCoordinatesToTest() {
        return Stream.of(Arguments.of(0, 0),
                         Arguments.of(5, 5),
                         Arguments.of(0, 25),
                         Arguments.of(20, 10),
                         Arguments.of(40, 0)
        );
    }

    @ParameterizedTest
    @MethodSource("getCoordinatesToTest")
    public void getStructureAtTest(final int posX, final int posY) {
        // Arrange
        final World world = new World(50, 0);
        world.createStructure(posX, posY);
        final Position position = new Position(posX, posY);

        // Act
        final Optional<IStructure> structure = world.getStructureAt(position);

        // Assert
        assertThat(structure.isPresent()).isTrue();
        assertThat(structure.get().getPosition()).isEqualTo(position);
    }

}
