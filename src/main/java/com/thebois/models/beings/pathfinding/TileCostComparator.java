package com.thebois.models.beings.pathfinding;

import java.io.Serializable;
import java.util.Comparator;

import com.thebois.models.world.ITile;

/**
 * Compares two tiles to determine which costs more to move to.
 */
class TileCostComparator implements Comparator<ITile>, Serializable {

    private final TileCostCalculator costCalculator;
    private final ITile destination;

    TileCostComparator(final TileCostCalculator costCalculator, final ITile destination) {
        this.costCalculator = costCalculator;
        this.destination = destination;
    }

    @Override
    public int compare(final ITile first, final ITile second) {
        final float firstCost = costCalculator.costOf(first, destination);
        final float secondCost = costCalculator.costOf(second, destination);
        return Float.compare(firstCost, secondCost);
    }

}
