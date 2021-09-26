package com.thebois.models.beings.pathfinding;

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import com.thebois.models.Position;
import com.thebois.models.world.ITile;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TileCostCalculatorTests {

    /* Get tiles in order from greater to lesser (with a destination in front) */
    public static Stream<Arguments> getOrderedTiles() {
        return Stream.of(
            Arguments.of(mockTile(0, 0), mockTile(1, 1), mockTile(0, 0)),
            Arguments.of(mockTile(0, 0), mockTile(10, 10), mockTile(9, 9)),
            Arguments.of(mockTile(0, 0), mockTile(-11, -11), mockTile(-10, -10)),
            Arguments.of(mockTile(0, 0), mockTile(1, 0), mockTile(0, 0)));
    }

    private static ITile mockTile(final int positionX, final int positionY) {
        final ITile tile = Mockito.mock(ITile.class);
        when(tile.getPosition()).thenReturn(new Position(positionX, positionY));
        return tile;
    }

    @ParameterizedTest
    @MethodSource("getOrderedTiles")
    public void costOfReturnsGreaterCostWhenTileFurtherAwayFromDestination(
        final ITile destination, final ITile greater, final ITile lesser) {
        // Arrange
        final TileCostCalculator cut = new TileCostCalculator();
        cut.setCostFromStartTo(greater, 0f);
        cut.setCostFromStartTo(lesser, 0f);

        // Act
        final float greaterCost = cut.costOf(greater, destination);
        final float lesserCost = cut.costOf(lesser, destination);

        // Assert
        assertThat(greaterCost).isGreaterThan(lesserCost);
    }

    @Test
    public void costOfRetainsLatestSetCostFromStart() {
        // Arrange
        final TileCostCalculator cut = new TileCostCalculator();
        final ITile tile = mockTile(0, 0);
        final float lastSetCost = 123.4f;

        // Act
        cut.setCostFromStartTo(tile, -23123.654f);
        cut.setCostFromStartTo(tile, 43431.1f);
        cut.setCostFromStartTo(tile, lastSetCost);

        // Assert
        final float cost = cut.costOf(tile, tile);
        assertThat(cost).isEqualTo(lastSetCost);
    }

}
