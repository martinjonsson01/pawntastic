package com.thebois.models.beings.pathfinding;

import java.util.Map;

import com.thebois.models.world.ITile;

/**
 * Calculates the cost of moving to a tile.
 */
class TileCostCalculator {

    private final Map<ITile, Float> costFromStart;

    /**
     * Initializes with the positions to use when calculating the cost.
     *
     * @param costFromStart Contains the cost from start to a position
     */
    TileCostCalculator(final Map<ITile, Float> costFromStart) {
        this.costFromStart = costFromStart;
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
    private float getCostFromStart(final ITile tile) {
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
