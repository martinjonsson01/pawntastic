package com.thebois.models.world;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.thebois.Pawntastic;
import com.thebois.listeners.events.ObstaclePlacedEvent;
import com.thebois.models.IFinder;
import com.thebois.models.Position;
import com.thebois.models.world.structures.House;
import com.thebois.models.world.structures.IStructure;
import com.thebois.utils.MatrixUtils;

/**
 * World creates a matrix and keeps track of all the structures and resources in the game world.
 */
public class World implements IWorld, IFinder {

    private final ITerrain[][] terrainMatrix;
    private final Optional<IStructure>[][] structureMatrix;
    private final int worldSize;
    private final ITile[][] canonicalMatrix;

    /**
     * Initiates the world with the given size.
     *
     * @param worldSize The amount of tiles in length for X and Y, e.g. worldSize x worldSize.
     */
    public World(final int worldSize) {
        this.worldSize = worldSize;

        terrainMatrix = new ITerrain[worldSize][worldSize];
        for (int y = 0; y < worldSize; y++) {
            for (int x = 0; x < worldSize; x++) {
                terrainMatrix[y][x] = new Grass(x, y);
            }
        }
        // Structures
        // noinspection unchecked
        structureMatrix = new Optional[worldSize][worldSize];
        for (int y = 0; y < structureMatrix.length; y++) {
            for (int x = 0; x < structureMatrix[y].length; x++) {
                structureMatrix[y][x] = Optional.empty();
            }
        }

        canonicalMatrix = new ITile[worldSize][worldSize];
        updateCanonicalMatrix();
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
        MatrixUtils.forEachElement(terrainMatrix, tile -> {
            final Position position = tile.getPosition();
            final int posY = (int) position.getPosY();
            final int posX = (int) position.getPosX();
            canonicalMatrix[posY][posX] = terrainMatrix[posY][posX].deepClone();
        });
        // Replace terrain with any possible structure.
        MatrixUtils.forEachElement(structureMatrix,
                                   maybeStructure -> maybeStructure.ifPresent(structure -> {
                                       final Position position = structure.getPosition();
                                       final int posY = (int) position.getPosY();
                                       final int posX = (int) position.getPosX();
                                       canonicalMatrix[posY][posX] = structure.deepClone();
                                   }));
    }

    /**
     * Creates a list of tiles that are not occupied by a structure or resource.
     *
     * @param count The amount of empty positions that needs to be found.
     *
     * @return List of empty positions.
     */
    public Iterable<Position> findEmptyPositions(final int count) {
        final List<Position> emptyPositions = new ArrayList<>();
        MatrixUtils.forEachElement(canonicalMatrix, tile -> {
            if (emptyPositions.size() >= count) return;
            emptyPositions.add(tile.getPosition());
        });
        return emptyPositions;
    }

    /**
     * Returns the matrix of the world.
     *
     * @return ITile[][]
     */
    public ArrayList<ITerrain> getTerrainTiles() {
        final ArrayList<ITerrain> copy = new ArrayList<>();
        for (final ITerrain[] matrix : terrainMatrix) {
            for (final ITerrain iTerrain : matrix) {
                copy.add(iTerrain.deepClone());
            }
        }
        return copy;
    }

    /**
     * Returns the structures in a Collection as the interface IStructures.
     *
     * @return The list to be returned.
     */
    public Collection<IStructure> getStructures() {
//        final Collection<IStructure> copy = new ArrayList<>();
//        for (final Optional<IStructure>[] matrix : structureMatrix) {
//            for (final Optional<IStructure> structure : matrix) {
//                structure.ifPresent(copy::add);
//            }
//        }
//        return copy;

        return MatrixUtils.matrixToCollection(this.structureMatrix);
    }

    /**
     * Builds a structure at a given position if possible.
     *
     * @param position The position where the structure should be built.
     *
     * @return Whether or not the structure was built.
     */
    public boolean createStructure(final Position position) {
        return createStructure((int) position.getPosX(), (int) position.getPosY());
    }

    /**
     * Builds a structure at a given position if possible.
     *
     * @param posX The X coordinate where the structure should be built.
     * @param posY The Y coordinate where the structure should be built.
     *
     * @return Whether or not the structure was built.
     */
    public boolean createStructure(final int posX, final int posY) {
        final Position position = new Position(posX, posY);
        if (isPositionPlaceable(position)) {
            structureMatrix[posY][posX] = Optional.of(new House(position));

            updateCanonicalMatrix();
            postObstacleEvent(posX, posY);
            return true;
        }
        return false;
    }

    private boolean isPositionPlaceable(final Position position) {
        final int posIntX = (int) position.getPosX();
        final int posIntY = (int) position.getPosY();
        if (posIntY < 0 || posIntY >= structureMatrix.length) {
            return false;
        }
        if (posIntX < 0 || posIntX >= structureMatrix[posIntY].length) {
            return false;
        }
        return structureMatrix[posIntY][posIntX].isEmpty();
    }

    private void postObstacleEvent(final int posX, final int posY) {
        final ObstaclePlacedEvent obstacleEvent = new ObstaclePlacedEvent(posX, posY);
        Pawntastic.BUS.post(obstacleEvent);
    }

    @Override
    public Optional<IStructure> findNearestStructure(final Position position) {
        final int searchRow = Math.round(position.getPosY());
        final int searchCol = Math.round(position.getPosX());

        final int maxSearchRadius = this.worldSize / 2;

        final Collection<IStructure> foundStructures = MatrixUtils.matrixSpiralSearch(
            this.structureMatrix,
            searchRow,
            searchCol,
            maxSearchRadius,
            1);

        if (foundStructures.iterator().hasNext()) {
            return Optional.of(foundStructures.iterator().next());
        }
        else {
            return Optional.empty();
        }
    }

    @Override
    public Collection<IStructure> findNearestStructures(final Position position,
                                                                  final int count) {

        final int searchRow = Math.round(position.getPosY());
        final int searchCol = Math.round(position.getPosX());
        final int maxSearchRadius = this.worldSize / 2;

        return MatrixUtils.matrixSpiralSearch(
            structureMatrix,
            searchRow,
            searchCol,
            maxSearchRadius,
            count);
    }

    @Override
    public Optional<IStructure> getStructureAt(final Position position) {
        final int row = Math.round(position.getPosY());
        final int col = Math.round(position.getPosX());
        return this.structureMatrix[row][col];
    }

    @Override
    public Iterable<ITile> getNeighboursOf(final ITile tile) {
        final ArrayList<ITile> tiles = new ArrayList<>(8);
        final Position position = tile.getPosition();
        final int posY = (int) position.getPosY();
        final int posX = (int) position.getPosX();

        final int startY = Math.max(0, posY - 1);
        final int endY = Math.min(worldSize - 1, posY + 1);
        final int startX = Math.max(0, posX - 1);
        final int endX = Math.min(worldSize - 1, posX + 1);

        for (int neighbourY = startY; neighbourY <= endY; neighbourY++) {
            for (int neighbourX = startX; neighbourX <= endX; neighbourX++) {
                final ITile neighbour = canonicalMatrix[neighbourY][neighbourX];
                if (tile.equals(neighbour)) continue;
                if (isDiagonalTo(tile, neighbour)) continue;
                tiles.add(neighbour);
            }
        }

        return tiles;
    }

    @Override
    public ITile getTileAt(final Position position) {
        return getTileAt((int) position.getPosX(), (int) position.getPosY());
    }

    @Override
    public ITile getTileAt(final int posX, final int posY) {
        if (posX < 0 || posY < 0 || posX >= worldSize || posY >= worldSize) {
            throw new IndexOutOfBoundsException("Given position is outside of the world.");
        }
        return terrainMatrix[posY][posX];
    }

    private boolean isDiagonalTo(final ITile tile, final ITile neighbour) {
        final int tileX = (int) tile.getPosition().getPosX();
        final int tileY = (int) tile.getPosition().getPosY();
        final int neighbourX = (int) neighbour.getPosition().getPosX();
        final int neighbourY = (int) neighbour.getPosition().getPosY();
        final int deltaX = Math.abs(tileX - neighbourX);
        final int deltaY = Math.abs(tileY - neighbourY);
        return deltaX == 1 && deltaY == 1;
    }

}
