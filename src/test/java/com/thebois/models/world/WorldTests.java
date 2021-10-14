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

import com.thebois.models.IFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.Colony;
import com.thebois.models.beings.pathfinding.AstarPathFinder;
import com.thebois.models.beings.pathfinding.IPathFinder;
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
        final World world = new World(2);

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
        final IWorld world = new World(3);

        // Act
        final Iterable<ITile> actualNeighbours = world.getNeighboursOf(tile);

        // Assert
        assertThat(actualNeighbours).containsExactlyInAnyOrderElementsOf(expectedNeighbours);
    }

    @Test
    public void getTileAtReturnsTileAtGivenPosition() {
        // Arrange
        final Collection<ITerrain> expectedTerrainTiles = mockTerrainTiles();
        final World world = new World(2);

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
        final World world = new World(2);

        // Assert
        assertThatThrownBy(() -> world.getTileAt(outOfBounds)).isInstanceOf(
            IndexOutOfBoundsException.class);
    }

    @Test
    public void createWorldWithNoStructures() {
        // Arrange
        final World world = new World(2);

        // Act
        final Collection<IStructure> structures = world.getStructures();

        // Assert
        assertThat(structures.size()).isEqualTo(0);
    }

    @Test
    public void numberOfStructuresIncreasesIfStructureSuccessfullyPlaced() {
        // Arrange
        final World world = new World(2);
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
        final World world = new World(2);
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
        final World world = new World(2);
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
        final IPathFinder pathFinder = Mockito.mock(IPathFinder.class);
        final IFinder mockFinder = Mockito.mock(IFinder.class);
        return new Colony(vacantPositions, pathFinder, mockFinder);
    }

    @Test
    public void instantiateWithPawnCountCreatesOnlyAsManyBeingsAsFitInTheWorld() {
        // Arrange
        final int pawnFitCount = 4;
        final World world = new World(2);
        final Iterable<Position> vacantPositions = world.findEmptyPositions(5);
        final IPathFinder pathFinder = new AstarPathFinder(world);
        final IFinder mockFinder = Mockito.mock(IFinder.class);

        // Act
        final Colony colony = new Colony(vacantPositions, pathFinder, mockFinder);

        // Assert
        assertThat(colony.getBeings()).size().isEqualTo(pawnFitCount);
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
    public void findNearestStructureReturnsCorrect(final int startX,
                                         final int startY,
                                         final int endX,
                                         final int endY) {

        // Arrange
        final World world = new World(50);
        world.createStructure(endX, endY);

        // Act
        final Optional<IStructure> foundStructure = world.findNearestStructure(new Position(
            startX,
            startY));

        // Assert
        assertThat(foundStructure.orElseThrow().getPosition()).isEqualTo(new Position(endX, endY));

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
    public void findNearestStructureReturnsIncorrect(final int startX,
                                         final int startY,
                                         final int endX,
                                         final int endY) {

        // Arrange
        final World world = new World(50);
        world.createStructure(endX, endY);

        // Act
        final Optional<IStructure> foundStructure = world.findNearestStructure(new Position(
            startX,
            startY));

        // Assert
        assertThat(foundStructure.isEmpty()).isTrue();
    }

    @Test
    public void returnsNoNearestStructure() {
        // Arrange
        final World world = new World(50);

        // Act
        final Optional<IStructure> structure = world.findNearestStructure(
            new Position(20f, 20f));

        // Assert
        assertThat(structure.isPresent()).isFalse();
    }

    private static Stream<Arguments> getPositionsAndSizeToTest() {
        return Stream.of(Arguments.of(List.of(new Position(10, 10),
                                              new Position(30, 5),
                                              new Position(6, 33),
                                              new Position(23, 23),
                                              new Position(0, 0)), 5),
                         Arguments.of(List.of(), 0));
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndSizeToTest")
    public void getStructureCollectionIsCorrectSize(
        final Collection<Position> positions,
        final int size) {
        // Arrange
        final World world = new World(50);

        // Act
        for (final Position position : positions) {
            world.createStructure(position);
        }

        // Assert
        assertThat(world.getStructures().size()).isEqualTo(size);
    }

}
