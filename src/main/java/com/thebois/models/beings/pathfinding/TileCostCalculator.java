package com.thebois.models.beings.pathfinding;

import java.util.HashMap;
import java.util.Map;

import com.thebois.models.world.ITile;

/**
 * Calculates the cost of moving to a tile.
 */
class TileCostCalculator {

    /**
     * For a ITile, costFromStart[ITile] is the cost of the cheapest path from start to the ITile
     * currently known.
     */
    private final Map<ITile, Float> costFromStart;

    /**
     * Initializes with the positions to use when calculating the cost.
     */
    TileCostCalculator() {
        this.costFromStart = new HashMap<>();
    }

    /**
     * Sets the currently cheapest cost from the start tile to the provided tile.
     *
     * @param tile The tile destination to which the cost is calculated from the start tile.
     * @param cost The cost of moving from start to the provided tile.
     */
    public void setCostFromStartTo(final ITile tile, final float cost) {
        if (costFromStart.containsKey(tile)) {
            costFromStart.replace(tile, cost);
        }
        else {
            costFromStart.put(tile, cost);
        }
    }

    /**
     * The total cost of including the provided position in the path from the start to the
     * destination.
     *
     * <p>
     * Usually described as f(n) = g(n) + h(n) in the A* algorithm.
     * </p>
     *
     * @param tile        The position (node n) to calculate the cost of
     * @param destination The position to find a path to
     *
     * @return The cost of the position
     */
    public float costOf(final ITile tile, final ITile destination) {
        return getCostFromStart(tile) + costToDestination(tile, destination);
    }

    /**
     * The cost of moving from start to the provided position.
     *
     * <p>
     * Usually described as g(n) in the A* algorithm.
     * </p>
     *
     * @param tile The position to calculate the cost of moving to
     *
     * @return The cost of moving from the start to the provided position
     */
    public float getCostFromStart(final ITile tile) {
        if (costFromStart.containsKey(tile)) return costFromStart.get(tile);
        // Default cost is infinity.
        return Float.MAX_VALUE;
    }

    /**
     * The heuristic estimation of the cost of moving from the provided position to the
     * destination.
     *
     * <p>
     * Usually described as h(n) in the A* algorithm.
     * </p>
     *
     * @param tile        The position to calculate the cost of moving from
     * @param destination The position to find a path to
     *
     * @return The cost of moving from the provided position to the destination
     */
    private float costToDestination(final ITile tile, final ITile destination) {
        return tile.getPosition().manhattanDistanceTo(destination.getPosition());
    }

}
