package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.thebois.models.Position;
import com.thebois.models.world.Grass;
import com.thebois.models.world.IWorld;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ColonyTests {

    @Test
    public void constructWithTilesCreatesOneBeingPerPosition() {
        // Arrange
        final int beingCount = 25;
        final List<Position> positions = new ArrayList<>(beingCount);
        for (int i = 0; i < beingCount; i++) {
            positions.add(new Position(0, 0));
        }
        final IWorld mockWorld = mock(IWorld.class);
        when(mockWorld.getTileAt(any())).thenReturn(new Grass(new Position()));

        // Act
        final Colony colony = new Colony(positions, mockWorld);

        // Assert
        assertThat(colony.getBeings().size()).isEqualTo(beingCount);
    }

    @Test
    public void updateCallsUpdateOnAllBeings() {
        // Arrange
        final int beingCount = 25;
        final Collection<IBeing> pawns = new ArrayList<>(beingCount);
        for (int i = 0; i < beingCount; i++) {
            pawns.add(Mockito.mock(IBeing.class));
        }
        final Colony colony = new Colony(pawns);

        // Act
        colony.update();

        // Assert
        for (final IBeing pawn : pawns) {
            verify(pawn, times(1)).update();
        }
    }

}
