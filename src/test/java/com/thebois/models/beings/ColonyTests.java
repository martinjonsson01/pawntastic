package com.thebois.models.beings;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class ColonyTests {

    @Test
    public void constructorTest() {

        // Arrange
        final int numberOfPawns = 25;

        // Act
        final Colony colony = new Colony(numberOfPawns);

        // Assert
        assertThat(colony.getBeings().size()).isEqualTo(numberOfPawns);
    }

}
