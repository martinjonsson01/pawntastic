package com.thebois.models.world;

import java.util.ArrayList;

import com.thebois.models.IFinder;
import com.thebois.models.Position;
import com.thebois.models.world.structures.IStructure;
import com.thebois.models.world.structures.Structure;
import com.thebois.models.world.structures.StructureType;

/**
 * World creates a matrix and keeps track of all the structures and resources in the game world.
 */
public class World implements IFinder {

    private ITerrain[][] terrainMatrix;
    private ArrayList<IStructure> structures;

    /**
     * Initiates the world with the given size.
     *
     * @param worldSize The amount of tiles in length for X and Y, e.g. worldSize x worldSize.
     */
    public World(int worldSize) {
        terrainMatrix = new ITerrain[worldSize][worldSize];

        for (int y = 0; y < worldSize; y++) {
            for (int x = 0; x < worldSize; x++) {
                terrainMatrix[y][x] = new Grass(x, y);
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
     * Builds a structure at a given position if possible.
     *
     * @param position The position where the struture should be built.
     *
     * @return Whether or not the structure was built.
     */
    public boolean createStructure(Position position) {
        if (isPositionBuildable(position)) {
            structures.add(new Structure(position, StructureType.HOUSE));
            return true;
        }
        return false;
    }

    private boolean isPositionBuildable(final Position position) {
        for (IStructure structure : structures) {
            if (structure.getPosition().equals(position)) {
                return false;
            }
        }
        return true;
    }

}
