package com.thebois.models.beings.pathfinding;

import java.util.Comparator;

import com.thebois.models.world.ITile;

/**
 * Compares two tiles to determine which costs more to move to.
 */
class TileCostComparator implements Comparator<ITile> {

    private final TileCostCalculator costCalculator;

    TileCostComparator(final TileCostCalculator costCalculator) {
        this.costCalculator = costCalculator;
    }

    @Override
    public int compare(final ITile first, final ITile second) {
        final float firstCost = costCalculator.costOf(first);
        final float secondCost = costCalculator.costOf(second);
        return Float.compare(firstCost, secondCost);
    }

}
