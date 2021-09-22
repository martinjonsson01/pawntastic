package com.thebois.models.beings;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class ColonyTests {

    @Test
    public void constructWithPositionsCreatesOneBeingPerPosition() {
        // Arrange
        final int beingCount = 25;
        final List<Position> positions = new ArrayList<>(beingCount);
        for (int i = 0; i < beingCount; i++) {
            positions.add(new Position());
        }

        // Act
        final Colony colony = new Colony(positions);

        // Assert
        assertThat(colony.getBeings().size()).isEqualTo(beingCount);
    }

}
