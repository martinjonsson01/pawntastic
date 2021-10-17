package com.thebois.models.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;
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
import com.thebois.models.Position;
import com.thebois.models.beings.Colony;
import com.thebois.models.beings.roles.RoleFactory;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceType;
import com.thebois.models.world.resources.Tree;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.terrains.Grass;
import com.thebois.models.world.terrains.ITerrain;

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
                         Arguments.of(mockTile(1, 1), List.of(mockPosition(1, 0),
                                                              mockPosition(0, 1),
                                                              mockPosition(2, 1),
                                                              mockPosition(1, 2))));
    }

    private static ITile mockTile(final int positionX, final int positionY) {
        return new Grass(positionX, positionY);
    }

    private static Position mockPosition(final int positionX, final int positionY) {
        return new Position(positionX, positionY);
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

    @BeforeEach
    public void setup() {
        RoleFactory.setWorld(mock(IWorld.class));
        RoleFactory.setResourceFinder(mock(IResourceFinder.class));
    }

    @AfterEach
    public void teardown() {
        RoleFactory.setWorld(null);
        RoleFactory.setResourceFinder(null);
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
        final World world = createTestWorld(7, mock(Random.class));
        world.createStructure(2, 4);
        world.createStructure(4, 4);
        world.createStructure(2, 2);
        world.createStructure(3, 2);
        world.createStructure(4, 2);
        final ITile tile = mockTile(3, 3);

        // Act
        final Optional<Position> actualNeighbour = world.getClosestNeighbourOf(tile, from);

        // Assert
        assertThat(actualNeighbour).hasValue(closestNeighbour);
    }

    private World createTestWorld(final int size, final Random random) {
        return new TestWorld(size, random);
    }

    @Test
    public void getClosestNeighbourOfReturnsEmptyWhenAllNeighboursOccupied() {
        // Arrange
        final World world = createTestWorld(3, mock(Random.class));
        world.createStructure(1, 0);
        world.createStructure(0, 1);
        world.createStructure(2, 1);
        world.createStructure(1, 2);
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
        final IResourceFinder finder = createTestWorld(10, mock(Random.class));
        final Position origin = new Position();

        // Act
        final Optional<IResource> maybeResource = finder.getNearbyOfType(origin, ResourceType.TREE);

        // Assert
        assertThat(maybeResource).isEmpty();
    }

    @Test
    public void getNearbyOfTypeResourceReturnsItWhenSingleNearby() {
        // Arrange
        final World world = new ResourceTestWorld(mock(Random.class));
        final IResource expectedResource = world.getResources().stream().findFirst().orElseThrow();
        final Position origin = new Position();
        final IResourceFinder finder = world;

        // Act
        final Optional<IResource> maybeResource = finder.getNearbyOfType(origin, ResourceType.TREE);

        // Assert
        assertThat(maybeResource).hasValue(expectedResource);
    }

    @Test
    public void getNearbyOfTypeResourceReturnsClosestWhenMultipleNearby() {
        // Arrange
        final World world = new ResourceTestWorld(mock(Random.class));
        final Position expectedResourcePosition = new Position(9, 9);
        final IResource expectedResource = world.getResources().stream().filter(resource -> resource
            .getPosition()
            .equals(expectedResourcePosition)).findFirst().orElseThrow();
        final Position from = new Position(8, 8);
        final IResourceFinder finder = world;

        // Act
        final Optional<IResource> maybeResource = finder.getNearbyOfType(from, ResourceType.TREE);

        // Assert
        assertThat(maybeResource).hasValue(expectedResource);
    }

    @Test
    public void getRandomVacantSpotReturnsRandomTileWhenThereAreNoObstacles() {
        // Arrange
        final Random mockRandom = mock(Random.class);
        final int randomCoordinate = 1;
        when(mockRandom.nextInt(anyInt())).thenReturn(randomCoordinate);
        final IWorld world = createTestWorld(3, mockRandom);
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
        final World world = createTestWorld(3, mockRandom);
        world.createStructure(firstBlockedRandomSpot);
        world.createStructure(secondBlockedRandomSpot);

        // Act
        final Position vacantSpot = world.getRandomVacantSpot().getPosition();

        // Assert
        assertThat(vacantSpot).isEqualTo(thirdEmptyRandomSpot);
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

    private World createWorld(final int size) {
        return createWorld(size, 0);
    }

    private World createWorld(final int size, final int seed) {
        return createWorld(size, seed, mock(Random.class));
    }

    private World createWorld(final int size, final int seed, final Random random) {
        return new World(size, seed, random);
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
        final boolean isBuilt = world.createStructure(position);
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
        final boolean isBuilt = world.createStructure(placementPosition);
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
        world.createStructure(position1);
        world.createStructure(position2);
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
        final World world = createWorld(10, 15, new Random());
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
        final Random random = mock(Random.class);
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
                world.createStructure(x, y);
            }
        }
    }

    private static class ResourceTestWorld extends World {

        ResourceTestWorld(final Random random) {
            super(10, 0, random);
        }

        @Override
        protected ITerrain[][] setUpTerrain(final int worldSize, final int seed) {
            final ITerrain[][] terrainMatrix = new ITerrain[worldSize][worldSize];
            for (int y = 0; y < worldSize; y++) {
                for (int x = 0; x < worldSize; x++) {
                    terrainMatrix[y][x] = new Grass(x, y);
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
            resourceMatrix[0][0] = new Tree(0, 0);
            resourceMatrix[worldSize - 1][worldSize - 1] = new Tree(worldSize - 1, worldSize - 1);
            return resourceMatrix;
        }

    }

}
