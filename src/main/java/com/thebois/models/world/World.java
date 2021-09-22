package com.thebois.models.world;

import java.util.ArrayList;
import java.util.Optional;

import com.thebois.models.IFinder;
import com.thebois.models.Position;
import com.thebois.models.world.structures.House;
import com.thebois.models.world.structures.IStructure;

/**
 * World creates a matrix and keeps track of all the structures and resources in the game world.
 */
public class World implements IFinder {

    private final ITerrain[][] terrainMatrix;
    private final Optional<IStructure>[][] structureMatrix;

    /**
     * Initiates the world with the given size.
     *
     * @param worldSize The amount of tiles in length for X and Y, e.g. worldSize x worldSize.
     */
    public World(int worldSize) {
        // Terrain
        terrainMatrix = new ITerrain[worldSize][worldSize];
        for (int y = 0; y < worldSize; y++) {
            for (int x = 0; x < worldSize; x++) {
                terrainMatrix[y][x] = new Grass(x, y);
            }
        }

        // Structures
        structureMatrix = new Optional[worldSize][worldSize];
        for (int y = 0; y < structureMatrix.length; y++) {
            for (int x = 0; x < structureMatrix[y].length; x++) {
                structureMatrix[y][x] = Optional.empty();
            }
        }
    }

    /**
     * Locates an object in the world and returns it.
     *
     * @return Object.
     */
    public Object find() {
        return null;
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
     * Returns the structures in an arraylist as the interface IStructures.
     *
     * @return The list to be returned.
     */
    public ArrayList<IStructure> getStructures() {
        final ArrayList<IStructure> copy = new ArrayList<>();
        for (final Optional<IStructure>[] matrix : structureMatrix) {
            for (final Optional<IStructure> structure : matrix) {
                structure.ifPresent(iStructure -> copy.add(iStructure.deepClone()));
            }
        }
        return copy;
    }

    /**
     * Builds a structure at a given position if possible.
     *
     * @param posX The X coordinate where the structure should be built.
     * @param posY The Y coordinate where the structure should be built.
     *
     * @return Whether or not the structure was built.
     */
    public boolean createStructure(int posX, final int posY) {
        final Position position = new Position(posX, posY);
        if (isPositionPlaceable(position)) {
            structureMatrix[posY][posX] = Optional.of(new House(position));
            return true;
        }
        return false;
    }

    /**
     * Builds a structure at a given position if possible.
     *
     * @param position The position where the structure should be built.
     *
     * @return Whether or not the structure was built.
     */
    public boolean createStructure(Position position) {
        return createStructure((int) position.getPosX(), (int) position.getPosY());
    }

    private boolean isPositionPlaceable(final Position position) {
        final int posIntX = (int) position.getPosX();
        final int posIntY = (int) position.getPosY();
        return structureMatrix[posIntY][posIntX].isEmpty();
    }

}

