package com.thebois.models.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.abstractions.IPositionFinder;
import com.thebois.abstractions.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.Colony;
import com.thebois.models.beings.pathfinding.AstarPathFinder;
import com.thebois.models.beings.pathfinding.IPathFinder;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.structures.StructureType;
import com.thebois.models.world.terrains.Grass;

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

    private static Position mockPosition(final int x, final int y) {
        return new Position(x, y);
    }

    private static ITile mockTile(final int x, final int y) {
        return new Grass(x, y);
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

        // Act
        final boolean isBuilt = world.tryCreateStructure(StructureType.HOUSE, position);
        final Collection<IStructure> structures = world.getStructures();

        // Assert
        assertThat(isBuilt).isTrue();
        assertThat(structures.size()).isEqualTo(1);
    }

    @ParameterizedTest
    @MethodSource("getPositionOutSideOfWorld")
    public void structureFailedToBePlaced(final Position placementPosition) {
        // Arrange
        final World world = new World(2, 0);

        // Act
        final boolean isBuilt = world.tryCreateStructure(StructureType.HOUSE, placementPosition);
        final Collection<IStructure> structures = world.getStructures();

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

        // Act
        world.tryCreateStructure(StructureType.HOUSE, position1);
        world.tryCreateStructure(StructureType.HOUSE, position2);
        final Collection<IStructure> structures = world.getStructures();

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
        final IStructureFinder mockFinder = Mockito.mock(IStructureFinder.class);
        final IPositionFinder positionFinder = Mockito.mock(IPositionFinder.class);

        return new Colony(vacantPositions, pathFinder, mockFinder, positionFinder);
    }

    @Test
    public void instantiateWithPawnCountCreatesOnlyAsManyBeingsAsFitInTheWorld() {
        // Arrange
        final int pawnFitCount = 4;
        final World world = new World(2, 15);
        final Iterable<Position> vacantPositions = world.findEmptyPositions(5);
        final IPathFinder pathFinder = new AstarPathFinder(world);
        final IStructureFinder mockFinder = Mockito.mock(IStructureFinder.class);
        final IPositionFinder positionFinder = Mockito.mock(IPositionFinder.class);

        // Act
        final Colony colony = new Colony(vacantPositions, pathFinder, mockFinder, positionFinder);

        // Assert
        assertThat(colony.getBeings()).size().isEqualTo(pawnFitCount);
    }

    @Test
    public void findEmptyPositionsReturnsPositionWithNoResourcesOn() {
        // Arrange
        final World world = new World(10, 15);
        final Collection<IResource> resources = world.getResources();
        final Collection<Position> occupiedPositions = new ArrayList<>();
        for (final IResource resource : resources) {
            occupiedPositions.add(resource.getPosition());
        }

        // Act
        final Iterable<Position> emptyPositions = world.findEmptyPositions(4);

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
        fillWorldWithStructures(worldSize, world);

        // Act
        final Iterable<Position> emptyPositions = world.findEmptyPositions(amountOfWantedPositions);

        // Assert
        assertThat(emptyPositions).isEmpty();
    }

    private void fillWorldWithStructures(final int worldSize, final World world) {
        for (int y = 0; y < worldSize; y++) {
            for (int x = 0; x < worldSize; x++) {
                world.tryCreateStructure(StructureType.HOUSE, x, y);
            }
        }
    }

    @Test
    public void findEmptyPositionsReturnsEarlyIfAmountOfEmptyPositionsHaveBeenMet() {
        // Arrange
        final World world = new World(2, 15);
        final int numberOfWantedPositions = 3;

        // Act
        final int numberOfEmptyPositions = Lists.newArrayList(world.findEmptyPositions(
            numberOfWantedPositions)).size();

        // Assert
        assertThat(numberOfEmptyPositions).isEqualTo(numberOfWantedPositions);
    }

    @Test
    public void getTerrainsReturnsTerrainEvenIfStructuresAreOnTopOfTerrain() {
        // Arrange
        final int worldSize = 15;
        final int seed = 0;
        final World world = new World(worldSize, seed);
        fillWorldWithStructures(worldSize, world);
        final int expectedNumberOfTerrainTiles = worldSize * worldSize;

        // Act
        final int actualNumberOfTerrainTiles = world.getTerrainTiles().size();

        // Assert
        assertThat(actualNumberOfTerrainTiles).isEqualTo(expectedNumberOfTerrainTiles);
    }

    private static Stream<Arguments> getCorrectCoordinatesToTest() {
        return Stream.of(
            Arguments.of(
                new Position(25,5),
                new Position(0, 0),
                new Position(20, 20)),
            Arguments.of(
                new Position(5,5),
                new Position(0, 0),
                new Position(8, 8)),
            Arguments.of(
                new Position(0,0),
                new Position(5, 5),
                new Position(3, 3)),
            Arguments.of(
                new Position(5,5),
                new Position(0, 0),
                new Position(8, 8)),
            Arguments.of(
                new Position(20,20),
                new Position(5, 7),
                new Position(15, 15))
        );
    }

    @ParameterizedTest
    @MethodSource("getCorrectCoordinatesToTest")
    public void findNearestStructureReturnsCorrect(final Position startingPosition,
                                                   final Position incorrectPosition,
                                                   final Position expectedPosition) {

        // Arrange
        final World world = new TestWorld(50);
        world.tryCreateStructure(StructureType.HOUSE, expectedPosition);
        world.tryCreateStructure(StructureType.HOUSE, incorrectPosition);

        // Act
        final Optional<IStructure> foundStructure =
            world.getNearbyStructureOfType(startingPosition, StructureType.HOUSE);

        // Assert
        assertThat(foundStructure.orElseThrow().getPosition()).isEqualTo(expectedPosition);

    }

    @Test
    public void returnsNoNearestStructureWhenWorldIsEmpty() {
        // Arrange
        final World world = new TestWorld(50);

        // Act
        final Optional<IStructure> structure = world.getNearbyStructureOfType(
            new Position(20f, 20f), StructureType.HOUSE);

        // Assert
        assertThat(structure.isPresent()).isFalse();
    }

    private static Stream<Arguments> getPositionsAndNumberOfPositions() {
        return Stream.of(Arguments.of(List.of(new Position(10, 10),
                                              new Position(30, 5),
                                              new Position(6, 33),
                                              new Position(23, 23),
                                              new Position(0, 0)), 5),
                         Arguments.of(List.of(), 0));
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndNumberOfPositions")
    public void getStructureCollectionIsCorrectSize(
        final Collection<Position> positions,
        final int size) {
        // Arrange
        final World world = new TestWorld(50);

        // Act
        for (final Position position : positions) {
            world.tryCreateStructure(StructureType.HOUSE, position);
        }

        // Assert
        assertThat(world.getStructures().size()).isEqualTo(size);
    }

    @Test
    public void findNearestIncompleteStructureReturnsCorrect() {
        // Arrange
        final World world = new TestWorld(50);
        world.tryCreateStructure(StructureType.HOUSE, new Position(4, 2));
        world.tryCreateStructure(StructureType.HOUSE, new Position(9, 5));

        for (final IStructure structure : world.getStructures()) {
            for (int i = 0; i < 10; i++) {
                structure.tryDeliverItem(ItemFactory.fromType(ItemType.LOG));
            }
            for (int i = 0; i < 10; i++) {
                structure.tryDeliverItem(ItemFactory.fromType(ItemType.ROCK));
            }
        }

        world.tryCreateStructure(StructureType.HOUSE, new Position(1, 3));
        world.tryCreateStructure(StructureType.HOUSE, new Position(7, 9));

        // Act
        final Optional<IStructure> foundStructure =
            world.getNearbyIncompleteStructure(new Position(0, 0));

        // Assert
        assertThat(foundStructure.orElseThrow()
                                 .getPosition())
            .isEqualTo(new Position(1, 3));
    }

    @Test
    public void findNearestIncompleteStructureFindsNoIncompleteStructure() {
        // Arrange
        final World world = new TestWorld(50);
        world.tryCreateStructure(StructureType.HOUSE, new Position(4, 2));
        world.tryCreateStructure(StructureType.HOUSE, new Position(9, 5));

        for (final IStructure structure : world.getStructures()) {
            for (int i = 0; i < 10; i++) {
                structure.tryDeliverItem(ItemFactory.fromType(ItemType.LOG));
            }
            for (int i = 0; i < 10; i++) {
                structure.tryDeliverItem(ItemFactory.fromType(ItemType.ROCK));
            }
        }

        // Act
        final Optional<IStructure> foundStructure =
            world.getNearbyIncompleteStructure(new Position(0, 0));

        // Assert
        assertThat(foundStructure.isEmpty()).isTrue();
    }

}
