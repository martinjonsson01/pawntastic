package com.thebois.models.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import com.thebois.Pawntastic;
import com.thebois.abstractions.IResourceFinder;
import com.thebois.listeners.events.ObstaclePlacedEvent;
import com.thebois.models.IStructureFinder;
import com.thebois.models.Position;
import com.thebois.models.world.generation.ResourceGenerator;
import com.thebois.models.world.generation.TerrainGenerator;
import com.thebois.models.world.resources.IResource;
import com.thebois.models.world.resources.ResourceType;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.structures.StructureFactory;
import com.thebois.models.world.structures.StructureType;
import com.thebois.models.world.terrains.ITerrain;
import com.thebois.utils.MatrixUtils;

/**
 * World creates a matrix and keeps track of all the structures and resources in the game world.
 */
public class World implements IWorld, IStructureFinder, IResourceFinder, Serializable {

    private final ITerrain[][] terrainMatrix;
    private final IStructure[][] structureMatrix;
    private final IResource[][] resourceMatrix;
    private final ITile[][] canonicalMatrix;
    private Collection<IStructure> structuresCache = new ArrayList<>();
    private final int worldSize;
    private final Random random;

    /**
     * Initiates the world with the given size.
     *
     * @param worldSize The amount of tiles in length for X and Y, e.g. worldSize x worldSize.
     * @param seed      The seed used to generate the world.
     * @param random    A generator of random numbers.
     */
    public World(final int worldSize, final int seed, final Random random) {
        this.worldSize = worldSize;
        this.random = random;
        terrainMatrix = setUpTerrain(worldSize, seed);
        structureMatrix = setUpStructures();
        resourceMatrix = setUpResources(worldSize, seed);
        canonicalMatrix = new ITile[worldSize][worldSize];
        updateCanonicalMatrix();
    }

    protected ITerrain[][] setUpTerrain(final int size, final int seed) {
        return new TerrainGenerator(worldSize, seed).generateTerrainMatrix();
    }

    private IStructure[][] setUpStructures() {
        final IStructure[][] newStructureMatrix = new IStructure[worldSize][worldSize];
        MatrixUtils.populateElements(newStructureMatrix, (x, y) -> null);
        return newStructureMatrix;
    }

    protected IResource[][] setUpResources(final int size, final int seed) {
        return new ResourceGenerator(worldSize, seed).generateResourceMatrix();
    }

    /**
     * The canonical matrix is a mash-up of all the different layers of the world. It replaces less
     * important tiles like grass with a structure if it is located at the same coordinates.
     *
     * <p>
     * Warning: expensive method, do not call every frame!
     * </p>
     */
    private void updateCanonicalMatrix() {
        // Fill with terrain.
        replaceTilesInCanonicalMatrix(terrainMatrix);
        // Replace with any possible resource.
        replaceTilesInCanonicalMatrix(resourceMatrix);
        // Replace with any possible structure.
        replaceTilesInCanonicalMatrix(structureMatrix);
    }

    private void replaceTilesInCanonicalMatrix(final ITile[][] tiles) {
        MatrixUtils.forEachElement(tiles, this::replaceTileInCanonicalMatrix);
    }

    private void replaceTileInCanonicalMatrix(final ITile tile) {
        if (tile != null) {
            final Position position = tile.getPosition();
            final int y = (int) position.getY();
            final int x = (int) position.getX();
            canonicalMatrix[y][x] = tile;
        }
    }

    /**
     * Creates a list of tiles that are not occupied by a structure or resource.
     *
     * @param count The amount of empty positions that needs to be found.
     *
     * @return List of empty positions.
     *
     * @throws IllegalArgumentException When it is impossible to find the requested amount of
     *                                  positions.
     */
    public Iterable<Position> findEmptyPositions(final int count) {
        if (count > worldSize * worldSize) {
            throw new IllegalArgumentException(
                "Can not find more empty positions than there are tiles in the world.");
        }

        final List<Position> emptyPositions = new ArrayList<>();
        while (emptyPositions.size() < count) {
            final ITile vacantTile = getRandomVacantSpot();
            final Position vacantPosition = vacantTile.getPosition();

            emptyPositions.add(vacantPosition);
        }
        return emptyPositions;
    }

    private Position createRandomPosition() {
        final int randomX = random.nextInt(worldSize);
        final int randomY = random.nextInt(worldSize);
        return new Position(randomX, randomY);
    }

    private boolean isVacant(final Position position) {
        final int x = (int) position.getX();
        final int y = (int) position.getY();
        return canonicalMatrix[y][x].getCost() < Float.MAX_VALUE;
    }

    /**
     * Returns the matrix of the world.
     *
     * @return ITile[][]
     */
    public Collection<ITerrain> getTerrainTiles() {
        final Collection<ITerrain> copy = new ArrayList<>();
        MatrixUtils.forEachElement(terrainMatrix,
                                   maybeTerrain -> copy.add(maybeTerrain.deepClone()));
        return copy;
    }

    /**
     * Returns the structures in a Collection as the interface IStructure.
     *
     * @return The list to be returned.
     */
    public Collection<IStructure> getStructures() {
        return structuresCache;
    }

    private void generateStructuresCache() {
        structuresCache = MatrixUtils.toCollection(this.structureMatrix);
    }

    /**
     * Returns the resources in a Collection as the interface IResource.
     *
     * @return The list to be returned.
     */
    public Collection<IResource> getResources() {
        return MatrixUtils.toCollection(this.resourceMatrix);
    }

    /**
     * Builds a structure at a given position if possible.
     *
     * @param type     The type of structure to be built.
     * @param position The position where the structure should be built.
     *
     * @return Whether the structure was built.
     */
    public boolean createStructure(final StructureType type, final Position position) {
        return createStructure(type, (int) position.getX(), (int) position.getY());
    }

    /**
     * Builds a structure of given type at a given position if possible.
     *
     * @param type The type of structure to be built.
     * @param x    The X coordinate where the structure should be built.
     * @param y    The Y coordinate where the structure should be built.
     *
     * @return Whether the structure was built.
     */
    public boolean createStructure(final StructureType type, final int x, final int y) {
        final Position position = new Position(x, y);
        if (isPositionPlaceable(position)) {
            structureMatrix[y][x] = StructureFactory.createStructure(type, x, y);

            updateCanonicalMatrix();
            postObstacleEvent(x, y);
            generateStructuresCache();
            return true;
        }
        return false;
    }

    private boolean isPositionPlaceable(final Position position) {
        final int posIntX = (int) position.getX();
        final int posIntY = (int) position.getY();
        if (posIntY < 0 || posIntY >= structureMatrix.length) {
            return false;
        }
        if (posIntX < 0 || posIntX >= structureMatrix[posIntY].length) {
            return false;
        }
        return isVacant(position);
    }

    private void postObstacleEvent(final int x, final int y) {
        final ObstaclePlacedEvent obstacleEvent = new ObstaclePlacedEvent(x, y);
        Pawntastic.getEventBus().post(obstacleEvent);
    }

    @Override
    public Optional<IStructure> getNearbyStructureOfType(
        final Position origin,
        final StructureType type) {
        return getStructures()
            .stream()
            .filter(structure -> structure.getType().equals(type))
            .min((o1, o2) -> Float.compare(origin.distanceTo(o1.getPosition()),
                                           origin.distanceTo(o2.getPosition())));
    }

    @Override
    public Optional<IStructure> getNearbyIncompleteStructure(
        final Position origin) {
        return getStructures()
            .stream()
            .filter(structure -> !structure.isCompleted())
            .min((o1, o2) -> Float.compare(origin.distanceTo(o1.getPosition()),
                                           origin.distanceTo(o2.getPosition())));
    }

    @Override
    public Optional<IResource> getNearbyOfType(final Position origin, final ResourceType type) {
        return getResources()
            .stream()
            .filter(resource -> resource.getType().equals(type))
            .min((o1, o2) -> Float.compare(origin.distanceTo(o1.getPosition()),
                                           origin.distanceTo(o2.getPosition())));
    }

    @Override
    public Collection<ITile> getNeighboursOf(final ITile tile) {
        final ArrayList<ITile> tiles = new ArrayList<>(8);
        final Position position = tile.getPosition();
        final int y = (int) position.getY();
        final int x = (int) position.getX();

        final int startY = Math.max(0, y - 1);
        final int endY = Math.min(worldSize - 1, y + 1);
        final int startX = Math.max(0, x - 1);
        final int endX = Math.min(worldSize - 1, x + 1);

        for (int neighbourY = startY; neighbourY <= endY; neighbourY++) {
            for (int neighbourX = startX; neighbourX <= endX; neighbourX++) {
                final ITile neighbour = canonicalMatrix[neighbourY][neighbourX];
                if (position.equals(neighbour.getPosition())) continue;
                if (isDiagonalTo(tile, neighbour)) continue;
                tiles.add(neighbour);
            }
        }

        return tiles;
    }

    @Override
    public ITile getTileAt(final Position position) {
        return getTileAt((int) position.getX(), (int) position.getY());
    }

    @Override
    public ITile getTileAt(final int x, final int y) {
        if (x < 0 || y < 0 || x >= worldSize || y >= worldSize) {
            throw new IndexOutOfBoundsException("Given position is outside of the world.");
        }
        return canonicalMatrix[y][x];
    }

    @Override
    public ITile getRandomVacantSpot() {
        Position randomPosition;
        do {
            randomPosition = createRandomPosition();
        } while (!isVacant(randomPosition));

        return getTileAt(randomPosition);
    }

    @Override
    public Optional<Position> getClosestNeighbourOf(final ITile tile, final Position from) {
        final Collection<ITile> neighbours = getNeighboursOf(tile);

        final Optional<ITile> firstVacantNeighbour = neighbours
            .stream()
            .filter(neighbourTile -> isVacant(neighbourTile.getPosition()))
            .findFirst();
        if (firstVacantNeighbour.isEmpty()) return Optional.empty();

        Position closest = firstVacantNeighbour.get().getPosition();

        for (final ITile neighbour : neighbours) {
            final Position current = neighbour.getPosition();
            final boolean isCloser = from.distanceTo(current) < from.distanceTo(closest);
            if (isCloser && isVacant(current)) {
                closest = current;
            }
        }

        return Optional.of(closest);
    }

    private boolean isDiagonalTo(final ITile tile, final ITile neighbour) {
        final int tileX = (int) tile.getPosition().getX();
        final int tileY = (int) tile.getPosition().getY();
        final int neighbourX = (int) neighbour.getPosition().getX();
        final int neighbourY = (int) neighbour.getPosition().getY();
        final int deltaX = Math.abs(tileX - neighbourX);
        final int deltaY = Math.abs(tileY - neighbourY);
        return deltaX == 1 && deltaY == 1;
    }

}
