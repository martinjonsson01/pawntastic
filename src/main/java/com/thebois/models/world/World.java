package com.thebois.models.world;

import java.util.ArrayList;
import java.util.List;

import com.thebois.models.IFinder;
import com.thebois.models.Position;
import com.thebois.models.beings.Colony;
import com.thebois.models.beings.IBeingGroup;
import com.thebois.models.beings.roles.IRoleAllocator;
import com.thebois.utils.MatrixUtils;

/**
 * World creates a matrix and keeps track of all the structures and resources in the game world.
 */
public class World implements IFinder {

    private final ITerrain[][] terrainMatrix;
    private final int pawnCount;
    private Colony colony;

    /**
     * Initiates the world with the given size.
     *
     * @param worldSize The amount of tiles in length for X and Y, e.g. worldSize x worldSize.
     * @param pawnCount The amount of beings to initialize the players' BeingGroup with
     */
    public World(final int worldSize, final int pawnCount) {
        terrainMatrix = new ITerrain[worldSize][worldSize];
        this.pawnCount = pawnCount;

        for (int y = 0; y < worldSize; y++) {
            for (int x = 0; x < worldSize; x++) {
                terrainMatrix[y][x] = new Grass(x, y);
            }
        }

        initColony();
    }

    private void initColony() {
        colony = new Colony(findEmptyPositions(pawnCount));
    }

    private Iterable<Position> findEmptyPositions(final int count) {
        final List<Position> emptyPositions = new ArrayList<>();
        MatrixUtils.forEachElement(terrainMatrix, terrain -> {
            if (emptyPositions.size() >= count) return;
            if (terrain.getType().equals(TerrainType.GRASS)) {
                emptyPositions.add(terrain.getPosition());
            }
        });
        return emptyPositions;
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
     * Returns the players' colony.
     *
     * @return the colony.
     */
    public IBeingGroup getColony() {
        return colony.deepClone();
    }

    /**
     * Returns the role allocator for the players' colony.
     *
     * @return the role allocator.
     */
    public IRoleAllocator getRoleAllocator() {
        return (IRoleAllocator) colony.deepClone();
    }

    /**
     * Updates the state of the world.
     */
    public void update() {
        colony.update();
    }

}
