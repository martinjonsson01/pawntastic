package com.thebois.models.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.models.Position;
import com.thebois.models.beings.Colony;
import com.thebois.models.beings.pathfinding.AstarPathFinder;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.Water;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.terrains.Dirt;
import com.thebois.models.world.terrains.Grass;
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

    private Collection<ITerrain> mockDirtTiles() {
        final ArrayList<ITerrain> terrainTiles = new ArrayList<>();
        terrainTiles.add(new Dirt(0, 0));
        terrainTiles.add(new Dirt(0, 1));
        terrainTiles.add(new Dirt(1, 0));
        terrainTiles.add(new Dirt(1, 1));
        return terrainTiles;
    }

    private Collection<Position> mockPositions() {
        final ArrayList<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(0, 1));
        positions.add(new Position(1, 0));
        positions.add(new Position(1, 1));
        return positions;
    }

    @Test
    public void worldFind() {
        // Arrange

        final World world = new World(2, 0);

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
        final IWorld world = new World(3, 15);

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
        final Collection<Position> expectedTilePositions = mockPositions();
        final Collection<Position> actualTilePositions = new ArrayList<>();
        final World world = new World(2, 0);

        // Act
        for (final Position position : expectedTilePositions) {
            actualTilePositions.add(world.getTileAt(position).getPosition());
        }
        // Assert
        assertThat(actualTilePositions).containsExactlyInAnyOrderElementsOf(expectedTilePositions);
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
        final World world = new World(2, 15);
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
        return new Colony(vacantPositions, pathFinder);
    }

    @Test
    public void instantiateWithPawnCountCreatesOnlyAsManyBeingsAsFitInTheWorld() {
        // Arrange
        final int pawnFitCount = 4;
        final World world = new World(2, 15);
        final Iterable<Position> vacantPositions = world.findEmptyPositions(5);
        final IPathFinder pathFinder = new AstarPathFinder(world);

        final Colony colony = new Colony(vacantPositions, pathFinder);
        // Assert
        assertThat(colony.getBeings()).size().isEqualTo(pawnFitCount);
    }

    @Test
    public void findEmptyPositionsReturnsPositionWithNoResourcesOn() {
        // Arrange
        // Instantiate a world filled with dirt.
        final World world = new World(10, 15);
        final Collection<IResource> resources = world.getResources();
        final Collection<Position> occupiedPositions = new ArrayList<>();
        final Iterable<Position> emptyPositions;
        for (final IResource resource : resources) {
            occupiedPositions.add(resource.getPosition());
        }

        // Act
        emptyPositions = world.findEmptyPositions(4);

        // Assert
        assertThat(emptyPositions).doesNotContainAnyElementsOf(occupiedPositions);
    }

    @Test
    public void findEmptyPositionsReturnsEmptyIfNoEmptyPositionsWasFound() {
        // Arrange
        final int worldSize = 10;
        final int seed = 0;
        final int amountOfWantedPositions = 5;
        final World world = new World(worldSize, seed);
        final Iterable<Position> emptyPositions;
        // Fill world with Structures
        for (int y = 0; y < worldSize; y++) {
            for (int x = 0; x < worldSize; x++) {
                world.createStructure(x, y);
            }
        }

        // Act
        emptyPositions = world.findEmptyPositions(amountOfWantedPositions);

        // Assert
        assertThat(emptyPositions).isEmpty();
    }

    @Test
    public void findEmptyPositionsReturnsEarlyIfAmountOfEmptyPositionsHaveBeenMet() {
        // Arrange
        // Instantiate a world filled with dirt.
        final World world = new World(2, 15);
        final int numberOfWantedPositions = 3;
        final int numberOfEmptyPositions;

        // Act
        numberOfEmptyPositions =
            Lists.newArrayList(world.findEmptyPositions(numberOfWantedPositions)).size();

        // Assert
        assertThat(numberOfEmptyPositions).isEqualTo(numberOfWantedPositions);
    }

    @Test
    public void getResourcesActuallyReturnsResources() {
        // Arrange
        // Instantiate a world filled with water.
        final World world = new World(2, 0);
        final Collection<IResource> expectedResources = new ArrayList<>();
        expectedResources.add(new Water(0, 0));
        expectedResources.add(new Water(1, 0));
        expectedResources.add(new Water(0, 1));
        expectedResources.add(new Water(1, 1));
        final Collection<IResource> actualResources;

        // Act
        actualResources = world.getResources();

        // Assert
        assertThat(actualResources).containsExactlyInAnyOrderElementsOf(expectedResources);
    }

}
