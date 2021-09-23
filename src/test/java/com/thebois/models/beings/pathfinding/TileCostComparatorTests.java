package com.thebois.models.beings.pathfinding;

import java.util.Comparator;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.thebois.models.world.ITile;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TileCostComparatorTests {

    @Test
    public void compareReturnsOneWhenFirstCostIsGreater() {
        // Arrange
        final ITile first = Mockito.mock(ITile.class);
        final ITile second = Mockito.mock(ITile.class);
        final TileCostCalculator costCalculator = Mockito.mock(TileCostCalculator.class);
        when(costCalculator.costOf(first)).thenReturn(1f);
        when(costCalculator.costOf(second)).thenReturn(0f);
        final Comparator<ITile> cut = new TileCostComparator(costCalculator);

        // Act
        final int comparison = cut.compare(first, second);

        // Assert
        assertThat(comparison).isEqualTo(1);
    }

    @Test
    public void compareReturnsZeroWhenCostsAreEqual() {
        // Arrange
        final ITile first = Mockito.mock(ITile.class);
        final ITile second = Mockito.mock(ITile.class);
        final TileCostCalculator costCalculator = Mockito.mock(TileCostCalculator.class);
        when(costCalculator.costOf(first)).thenReturn(1f);
        when(costCalculator.costOf(second)).thenReturn(1f);
        final Comparator<ITile> cut = new TileCostComparator(costCalculator);

        // Act
        final int comparison = cut.compare(first, second);

        // Assert
        assertThat(comparison).isEqualTo(0);
    }

    @Test
    public void compareReturnsNegativeOneWhenSecondCostIsGreater() {
        // Arrange
        final ITile first = Mockito.mock(ITile.class);
        final ITile second = Mockito.mock(ITile.class);
        final TileCostCalculator costCalculator = Mockito.mock(TileCostCalculator.class);
        when(costCalculator.costOf(first)).thenReturn(0f);
        when(costCalculator.costOf(second)).thenReturn(1f);
        final Comparator<ITile> cut = new TileCostComparator(costCalculator);

        // Act
        final int comparison = cut.compare(first, second);

        // Assert
        assertThat(comparison).isEqualTo(-1);
    }

}
