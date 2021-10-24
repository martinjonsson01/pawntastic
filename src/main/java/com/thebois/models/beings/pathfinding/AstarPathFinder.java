package com.thebois.models.beings.pathfinding;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.google.common.collect.Lists;

import com.thebois.models.Position;
import com.thebois.models.world.ITile;
import com.thebois.models.world.IWorld;

/**
 * Finds paths between locations using the A* algorithm.
 *
 * @author Martin
 */
public class AstarPathFinder implements IPathFinder {

    private final IWorld world;
    /**
     * For a given key ITile, the value, cameFrom[ITile], is the ITile preceding it on the path from
     * the start. It describes from which ITile the pathfinder came to reach the given tile.
     *
     * <p>
     * If you get the value cameFrom[lastTileInPath] then that is the tile immediately before the
     * destination tile that is also in the path. Once the destination has been reached, to
     * reconstruct the final path, you use this over and over again to eventually reach the starting
     * tile. Then you have the complete path. See reconstructPath.
     * </p>
     */
    private Map<ITile, ITile> cameFrom;
    /**
     * Contains the ITiles on the fringes of the search. These are the ITiles to evaluate next. The
     * "discovered" nodes.
     *
     * <p>
     * At each step of the algorithm, the node with the lowest cost is popped from the queue and its
     * neighbours are updated accordingly.
     * </p>
     */
    private PriorityQueue<ITile> openSet;

    /**
     * Initializes a pathfinder that finds paths through a specified world.
     *
     * @param world The world to consider obstacles in when pathfinding.
     */
    public AstarPathFinder(final IWorld world) {
        this.world = world;
    }

    @Override
    public Collection<Position> path(final Position start, final Position destination) {

        final ITile startTile = world.getTileAt(start);
        final ITile destinationTile = world.getTileAt(destination);

        this.cameFrom = new HashMap<>();
        final TileCostCalculator costCalculator = new TileCostCalculator();
        final Comparator<ITile> costComparator = new TileCostComparator(costCalculator,
                                                                        destinationTile);
        // The priority queue automatically sorts elements according to the comparator.
        this.openSet = new PriorityQueue<>(costComparator);

        openSet.add(startTile);
        costCalculator.setCostFromStartTo(startTile, 0f);

        while (openSet.peek() != null) {
            final ITile current = openSet.peek();

            if (current.equals(destinationTile)) return reconstructPath(current);

            // Only remove if not at destination yet.
            openSet.remove();

            for (final ITile neighbour : world.getNeighboursOf(current)) {
                final float costFromCurrentToNeighbour = costCalculator.costOf(current, neighbour);
                final float tentativeCostFromStart =
                    costCalculator.getCostFromStart(current) + costFromCurrentToNeighbour;

                // If path from start to neighbour is less than the currently cheapest path there.
                if (tentativeCostFromStart < costCalculator.getCostFromStart(neighbour)) {
                    // This path to neighbor is better than any previous one. Record it!
                    cameFrom.put(neighbour, current);
                    costCalculator.setCostFromStartTo(neighbour, tentativeCostFromStart);

                    // To re-sort the priority queue, remove and add the neighbouring tile.
                    // If neighbour isn't in the open set, removing it does nothing.
                    openSet.remove(neighbour);
                    openSet.add(neighbour);
                }
            }
        }

        // Pathfinding failed. No path exists.
        return List.of();
    }

    /**
     * Reconstructs the path that lead to the provided ITile.
     *
     * @param endOfPath The end of the path to reconstruct.
     *
     * @return The entire path leading to the provided ITile.
     */
    private Collection<Position> reconstructPath(final ITile endOfPath) {
        final ArrayList<Position> backwardsPath = new ArrayList<>();
        backwardsPath.add(endOfPath.getPosition());
        ITile current = endOfPath;
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            backwardsPath.add(current.getPosition());
        }
        return Lists.reverse(backwardsPath);
    }

}
