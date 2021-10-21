package com.thebois.models.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

import org.assertj.core.api.ThrowableAssert;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import com.thebois.abstractions.IResourceFinder;
import com.thebois.models.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.Colony;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.inventory.items.ItemFactory;
import com.thebois.models.inventory.items.ItemType;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceFactory;
import com.thebois.models.world.resources.ResourceType;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.structures.StructureType;
import com.thebois.models.world.terrains.ITerrain;
import com.thebois.models.world.terrains.TerrainFactory;
import com.thebois.models.world.terrains.TerrainType;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
                         Arguments.of(
                             mockTile(1, 1),
                             List.of(mockPosition(1, 0),
                                     mockPosition(0, 1),
                                     mockPosition(2, 1),
                                     mockPosition(1, 2))));
    }

    private static ITile mockTile(final int x, final int y) {
        return TerrainFactory.createTerrain(TerrainType.GRASS, x, y);
    }

    private static Position mockPosition(final int x, final int y) {
        return new Position(x, y);
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

    /**
     * Pairs a from-position with the position of the neighbour tile that is closest.
     * <p>
     * The neighbour tiles are all neighbours to the same tile, the one at 3,3 in a 7x7 world.
     * </p>
     *
     * @return The arguments described above.
     */
    public static Stream<Arguments> getPositionsAndClosestNeighbourPosition() {
        return Stream.of(Arguments.of(new Position(0, 3), new Position(2, 3)),
                         Arguments.of(new Position(6, 3), new Position(4, 3)),
                         Arguments.of(new Position(3, 6), new Position(3, 4)),
                         Arguments.of(new Position(2, 0), new Position(2, 3)),
                         Arguments.of(new Position(4, 0), new Position(4, 3)));
    }

    private static Stream<Arguments> getCorrectCoordinatesToTest() {
        return Stream.of(Arguments.of(new Position(25, 5),
                                      new Position(0, 0),
                                      new Position(20, 20)),
                         Arguments.of(new Position(5, 5), new Position(0, 0), new Position(8, 8)),
                         Arguments.of(new Position(0, 0), new Position(5, 5), new Position(3, 3)),
                         Arguments.of(new Position(5, 5), new Position(0, 0), new Position(8, 8)),
                         Arguments.of(new Position(20, 20),
                                      new Position(5, 7),
                                      new Position(15, 15)));
    }

    private static Stream<Arguments> getPositionsAndNumberOfPositions() {
        return Stream.of(Arguments.of(List.of(new Position(10, 10),
                                              new Position(30, 5),
                                              new Position(6, 33),
                                              new Position(23, 23),
                                              new Position(0, 0)), 5), Arguments.of(List.of(), 0));
    }

    /**
     * Assumes a world size of 20x20.
     */
    public static Stream<Arguments> getSearchPerimeterWithCorrectCoordinateRanges() {
        //                                 origin, radius, minX, maxX, minY, maxY
        return Stream.of(Arguments.of(new Position(0, 0), 1, 0, 1, 0, 1),
                         Arguments.of(new Position(0, 0), 10, 0, 10, 0, 10),
                         Arguments.of(new Position(5, 5), 3, 2, 8, 2, 8),
                         Arguments.of(new Position(15, 15), 9, 6, 19, 6, 19),
                         Arguments.of(new Position(5, 0), 3, 2, 8, 0, 3),
                         Arguments.of(new Position(0, 5), 3, 0, 3, 2, 8));
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

    @ParameterizedTest
    @MethodSource("getPositionsAndClosestNeighbourPosition")
    public void getClosestNeighbourOfReturnsPositionWhenNeighbourIsOccupied(
        final Position from, final Position closestNeighbour) {
        // Arrange
        /*
          X = destination tile
          N = empty neighbour
          O = occupied tile
          * * * * * * *
          * * * * * * *
          * * O N O * *
          * * N X N * *
          * * O O O * *
          * * * * * * *
          * * * * * * *
         */
        final World world = createTestWorld(7);
        world.createStructure(StructureType.HOUSE, 2, 4);
        world.createStructure(StructureType.HOUSE, 4, 4);
        world.createStructure(StructureType.HOUSE, 2, 2);
        world.createStructure(StructureType.HOUSE, 3, 2);
        world.createStructure(StructureType.HOUSE, 4, 2);
        final ITile tile = mockTile(3, 3);

        // Act
        final Optional<Position> actualNeighbour = world.getClosestNeighbourOf(tile, from);

        // Assert
        assertThat(actualNeighbour).hasValue(closestNeighbour);
    }

    private World createTestWorld(final int size) {
        return createTestWorld(size, mock(ThreadLocalRandom.class));
    }

    private World createTestWorld(final int size, final ThreadLocalRandom random) {
        return new TestWorld(size, random);
    }

    @Test
    public void getClosestNeighbourOfReturnsEmptyWhenAllNeighboursOccupied() {
        // Arrange
        final World world = createTestWorld(3);
        world.createStructure(StructureType.HOUSE, 1, 0);
        world.createStructure(StructureType.HOUSE, 0, 1);
        world.createStructure(StructureType.HOUSE, 2, 1);
        world.createStructure(StructureType.HOUSE, 1, 2);
        final ITile tile = mockTile(1, 1);
        final Position from = new Position(0, 0);

        // Act
        final Optional<Position> actualNeighbour = world.getClosestNeighbourOf(tile, from);

        // Assert
        assertThat(actualNeighbour).isEmpty();
    }

    @Test
    public void getNearbyOfTypeResourceReturnsEmptyWhenNothingNearby() {
        // Arrange
        final IResourceFinder finder = createTestWorld(10);
        final Position origin = new Position();

        // Act
        final Optional<IResource> maybeResource = finder.getNearbyOfType(origin, ResourceType.TREE);

        // Assert
        assertThat(maybeResource).isEmpty();
    }

    @Test
    public void getNearbyOfTypeResourceReturnsItWhenSingleNearby() {
        // Arrange
        final World world = new ResourceTestWorld(mock(ThreadLocalRandom.class));
        final IResource expectedResource = world.getResources().stream().findFirst().orElseThrow();
        final Position origin = new Position();

        // Act
        final Optional<IResource> maybeResource = world.getNearbyOfType(origin, ResourceType.TREE);

        // Assert
        assertThat(maybeResource).hasValue(expectedResource);
    }

    @Test
    public void getNearbyOfTypeResourceReturnsClosestWhenMultipleNearby() {
        // Arrange
        final World world = new ResourceTestWorld(mock(ThreadLocalRandom.class));
        final Position expectedResourcePosition = new Position(9, 9);
        final IResource expectedResource = world.getResources()
                                                .stream()
                                                .filter(resource -> resource.getPosition()
                                                                            .equals(
                                                                                expectedResourcePosition))
                                                .findFirst()
                                                .orElseThrow();
        final Position from = new Position(8, 8);

        // Act
        final Optional<IResource> maybeResource = world.getNearbyOfType(from, ResourceType.TREE);

        // Assert
        assertThat(maybeResource).hasValue(expectedResource);
    }

    @ParameterizedTest
    @MethodSource("getSearchPerimeterWithCorrectCoordinateRanges")
    public void getRandomVacantSpotCallsRandomWithCorrectRange(
        final Position origin,
        final int radius,
        final int minX,
        final int maxX,
        final int minY,
        final int maxY) {
        // Arrange
        final ThreadLocalRandom mockRandom = mock(ThreadLocalRandom.class);
        final IWorld world = createTestWorld(20, mockRandom);

        // Act
        world.getRandomVacantSpotInRadiusOf(origin, radius);

        // Assert
        // nextInt is [min, max), hence the +1
        verify(mockRandom, atLeastOnce()).nextInt(minX, maxX + 1);
        verify(mockRandom, atLeastOnce()).nextInt(minY, maxY + 1);
    }

    @Test
    public void getRandomVacantSpotReturnsRandomTileInRadiusWhenThereAreNoObstacles() {
        // Arrange
        final ThreadLocalRandom mockRandom = mock(ThreadLocalRandom.class);
        final int randomCoordinate = 1;
        when(mockRandom.nextInt(anyInt(), anyInt())).thenReturn(randomCoordinate);
        final IWorld world = createTestWorld(3, mockRandom);
        final Position expectedSpot = new Position(randomCoordinate, randomCoordinate);

        // Act
        final Position vacantSpot = world.getRandomVacantSpotInRadiusOf(new Position(), 10)
                                         .getPosition();

        // Assert
        assertThat(vacantSpot).isEqualTo(expectedSpot);
    }

    @Test
    public void getRandomVacantSpotReturnsRandomTileInRadiusWhenThereObstacles() {
        // Arrange
        final ThreadLocalRandom mockRandom = mock(ThreadLocalRandom.class);
        final Position firstBlockedRandomSpot = new Position(0, 1);
        final Position secondBlockedRandomSpot = new Position(1, 1);
        final Position thirdEmptyRandomSpot = new Position(2, 0);
        when(mockRandom.nextInt(anyInt(), anyInt())).thenReturn((int) firstBlockedRandomSpot.getX(),
                                                                (int) firstBlockedRandomSpot.getY(),
                                                                (int) secondBlockedRandomSpot.getX(),
                                                                (int) secondBlockedRandomSpot.getY(),
                                                                (int) thirdEmptyRandomSpot.getX(),
                                                                (int) thirdEmptyRandomSpot.getY());
        final World world = createTestWorld(3, mockRandom);
        world.createStructure(StructureType.HOUSE, firstBlockedRandomSpot);
        world.createStructure(StructureType.HOUSE, secondBlockedRandomSpot);

        // Act
        final Position vacantSpot = world.getRandomVacantSpotInRadiusOf(new Position(), 10)
                                         .getPosition();

        // Assert
        assertThat(vacantSpot).isEqualTo(thirdEmptyRandomSpot);
    }

    @ParameterizedTest
    @MethodSource("getTileAndNeighbours")
    public void getNeighboursOfReturnsExpectedNeighbours(
        final ITile tile, final Iterable<Position> expectedNeighbours) {
        // Arrange
        final World world = createWorld(3, 15);

        // Act
        final Iterable<ITile> actualNeighbours = world.getNeighboursOf(tile);
        final Collection<Position> positions = new ArrayList<>();
        for (final ITile actualNeighbour : actualNeighbours) {
            positions.add(actualNeighbour.getPosition());
        }
        // Assert
        assertThat(positions).containsExactlyInAnyOrderElementsOf(expectedNeighbours);
    }

    private World createWorld(final int size, final int seed) {
        return createWorld(size, seed, mock(ThreadLocalRandom.class));
    }

    private World createWorld(final int size, final int seed, final ThreadLocalRandom random) {
        return new World(size, seed, random);
    }

    @Test
    public void getTileAtReturnsTileAtGivenPosition() {
        // Arrange
        final Collection<Position> expectedTilePositions = mockPositions();
        final Collection<Position> actualTilePositions = new ArrayList<>();
        final World world = createWorld(2);

        // Act
        for (final Position position : expectedTilePositions) {
            actualTilePositions.add(world.getTileAt(position).getPosition());
        }
        // Assert
        assertThat(actualTilePositions).containsExactlyInAnyOrderElementsOf(expectedTilePositions);
    }

    private Collection<Position> mockPositions() {
        final ArrayList<Position> positions = new ArrayList<>();
        positions.add(new Position(0, 0));
        positions.add(new Position(0, 1));
        positions.add(new Position(1, 0));
        positions.add(new Position(1, 1));
        return positions;
    }

    private World createWorld(final int size) {
        return createWorld(size, 0);
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
        final World world = createWorld(2, 15);
        final Position position = new Position(1, 1);

        // Act
        final boolean isBuilt = world.createStructure(StructureType.HOUSE, position);
        final Collection<IStructure> structures = world.getStructures();

        // Assert
        assertThat(isBuilt).isTrue();
        assertThat(structures.size()).isEqualTo(1);
    }

    @ParameterizedTest
    @MethodSource("getPositionOutSideOfWorld")
    public void structureFailedToBePlaced(final Position placementPosition) {
        // Arrange
        final World world = createWorld(2);

        // Act
        final boolean isBuilt = world.createStructure(StructureType.HOUSE, placementPosition);
        final Collection<IStructure> structures = world.getStructures();

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

        // Act
        world.createStructure(StructureType.HOUSE, position1);
        world.createStructure(StructureType.HOUSE, position2);
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
        return new Colony(vacantPositions);
    }

    @Test
    public void findEmptyPositionsWithCountThatWontFitThrowsException() {
        // Arrange
        final World world = createWorld(2, 15);
        final ThrowableAssert.ThrowingCallable findPositions = () -> world.findEmptyPositions(5);

        // Act
        assertThatThrownBy(findPositions).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void findEmptyPositionsReturnsPositionWithNoResourcesOn() {
        // Arrange
        final World world = createWorld(10, 15, ThreadLocalRandom.current());
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
    public void findEmptyPositionsReturnsEarlyIfAmountOfEmptyPositionsHaveBeenMet() {
        // Arrange
        final ThreadLocalRandom random = mock(ThreadLocalRandom.class);
        when(random.nextInt(anyInt())).thenReturn(0, 0, 0, 1, 1, 0, 1, 1);
        final World world = createWorld(2, 15, random);
        final int numberOfWantedPositions = 3;

        // Act
        final Iterable<Position> emptyPositions = world.findEmptyPositions(numberOfWantedPositions);

        // Assert
        final int numberOfEmptyPositions = Lists.newArrayList(emptyPositions).size();
        assertThat(numberOfEmptyPositions).isEqualTo(numberOfWantedPositions);
    }

    @Test
    public void getTerrainsReturnsTerrainEvenIfStructuresAreOnTopOfTerrain() {
        // Arrange
        final int worldSize = 15;
        final World world = createWorld(worldSize);
        fillWorldWithStructures(worldSize, world);
        final int expectedNumberOfTerrainTiles = worldSize * worldSize;

        // Act
        final int actualNumberOfTerrainTiles = world.getTerrainTiles().size();

        // Assert
        assertThat(actualNumberOfTerrainTiles).isEqualTo(expectedNumberOfTerrainTiles);
    }

    private void fillWorldWithStructures(final int worldSize, final World world) {
        for (int y = 0; y < worldSize; y++) {
            for (int x = 0; x < worldSize; x++) {
                world.createStructure(StructureType.HOUSE, x, y);
            }
        }
    }

    @ParameterizedTest
    @MethodSource("getCorrectCoordinatesToTest")
    public void findNearestStructureReturnsCorrect(
        final Position startingPosition,
        final Position incorrectPosition,
        final Position expectedPosition) {

        // Arrange
        final World world = createTestWorld(50);
        world.createStructure(StructureType.HOUSE, expectedPosition);
        world.createStructure(StructureType.HOUSE, incorrectPosition);

        // Act
        final Optional<IStructure> foundStructure = world.getNearbyStructureOfType(startingPosition,
                                                                                   StructureType.HOUSE);

        // Assert
        assertThat(foundStructure.orElseThrow().getPosition()).isEqualTo(expectedPosition);
    }

    @Test
    public void returnsNoNearestStructureWhenWorldIsEmpty() {
        // Arrange
        final World world = createWorld(50);

        // Act
        final Optional<IStructure> structure = world.getNearbyStructureOfType(new Position(20f,
                                                                                           20f),
                                                                              StructureType.HOUSE);

        // Assert
        assertThat(structure.isPresent()).isFalse();
    }

    @ParameterizedTest
    @MethodSource("getPositionsAndNumberOfPositions")
    public void getStructureCollectionIsCorrectSize(
        final Collection<Position> positions, final int size) {
        // Arrange
        final World world = createTestWorld(50);

        // Act
        for (final Position position : positions) {
            world.createStructure(StructureType.HOUSE, position);
        }

        // Assert
        assertThat(world.getStructures().size()).isEqualTo(size);
    }

    @Test
    public void findNearestIncompleteStructureReturnsCorrect() {
        // Arrange
        final World world = createTestWorld(50);
        world.createStructure(StructureType.HOUSE, new Position(4, 2));
        world.createStructure(StructureType.HOUSE, new Position(9, 5));

        for (final IStructure structure : world.getStructures()) {
            for (int i = 0; i < 10; i++) {
                structure.tryDeliverItem(ItemFactory.fromType(ItemType.LOG));
            }
            for (int i = 0; i < 10; i++) {
                structure.tryDeliverItem(ItemFactory.fromType(ItemType.ROCK));
            }
        }

        world.createStructure(StructureType.HOUSE, new Position(1, 3));
        world.createStructure(StructureType.HOUSE, new Position(7, 9));

        // Act
        final Optional<IStructure> foundStructure = world.getNearbyIncompleteStructure(new Position(0,
                                                                                                    0));

        // Assert
        assertThat(foundStructure.orElseThrow().getPosition()).isEqualTo(new Position(1, 3));
    }

    @Test
    public void findNearestIncompleteStructureFindsNoIncompleteStructure() {
        // Arrange
        final World world = createTestWorld(50);
        world.createStructure(StructureType.HOUSE, new Position(4, 2));
        world.createStructure(StructureType.HOUSE, new Position(9, 5));

        for (final IStructure structure : world.getStructures()) {
            for (int i = 0; i < 10; i++) {
                structure.tryDeliverItem(ItemFactory.fromType(ItemType.LOG));
            }
            for (int i = 0; i < 10; i++) {
                structure.tryDeliverItem(ItemFactory.fromType(ItemType.ROCK));
            }
        }

        // Act
        final Optional<IStructure> foundStructure = world.getNearbyIncompleteStructure(new Position(0,
                                                                                                    0));

        // Assert
        assertThat(foundStructure.isEmpty()).isTrue();
    }

    private static class ResourceTestWorld extends World {

        ResourceTestWorld(final ThreadLocalRandom random) {
            super(10, 0, random);
        }

        @Override
        protected ITerrain[][] setUpTerrain(final int worldSize, final int seed) {
            final ITerrain[][] terrainMatrix = new ITerrain[worldSize][worldSize];
            for (int y = 0; y < worldSize; y++) {
                for (int x = 0; x < worldSize; x++) {
                    terrainMatrix[y][x] = TerrainFactory.createTerrain(TerrainType.GRASS, x, y);
                }
            }
            return terrainMatrix;
        }

        @Override
        protected IResource[][] setUpResources(final int worldSize, final int seed) {
            final IResource[][] resourceMatrix = new IResource[worldSize][worldSize];
            for (int y = 0; y < worldSize; y++) {
                for (int x = 0; x < worldSize; x++) {
                    resourceMatrix[y][x] = null;
                }
            }
            resourceMatrix[0][0] = ResourceFactory.createResource(ResourceType.TREE, 0, 0);
            resourceMatrix[worldSize - 1][worldSize - 1] = ResourceFactory.createResource(ResourceType.TREE,
                                                                                          worldSize
                                                                                          - 1,
                                                                                          worldSize
                                                                                          - 1);
            return resourceMatrix;
        }

    }

}
