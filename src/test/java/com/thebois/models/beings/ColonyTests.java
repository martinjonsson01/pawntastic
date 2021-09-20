package com.thebois.models.beings;

import org.junit.jupiter.api.Test;

import com.thebois.models.Position;

import static org.assertj.core.api.Assertions.*;

public class ColonyTests {

    @Test
    public void createBeingEqualTo3Test() {

        // Arrange
        final Colony colony = new Colony();
        // Act
        colony.createBeing();
        colony.createBeing();
        colony.createBeing();
        // Assert
        assertThat(colony.getBeings().size()).isEqualTo(3);
        assertThat(colony.getBeings().size()).isNotEqualTo(4);
    }

    @Test
    public void createBeingNotEqualTo4Test() {

        // Arrange
        final Colony colony = new Colony();

        // Act
        colony.createBeing();
        colony.createBeing();
        colony.createBeing();
        colony.createBeing();
        colony.createBeing();

        // Assert
        assertThat(colony.getBeings().size()).isNotEqualTo(4);
    }

    @Test
    public void createBeingIsBeingRealTest() {

        // arrange
        final Colony colony = new Colony();

        // Act
        colony.createBeing();

        // Assert
        assertThat(colony.getBeings().iterator().next().getPosition()).isEqualTo(new Position());
    }

}
